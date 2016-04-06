/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.orm.impl.core;

import org.bridje.orm.impl.sql.SelectBuilder;
import org.bridje.orm.impl.sql.InsertBuilder;
import org.bridje.orm.impl.sql.DeleteBuilder;
import org.bridje.orm.impl.sql.UpdateBuilder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.orm.EntityContext;
import org.bridje.orm.Table;
import org.bridje.orm.Query;
import org.bridje.orm.dialects.ColumnData;
import org.bridje.orm.dialects.SQLDialect;

/**
 * 
 */
class EntityContextImpl implements EntityContext
{
    private final DataSource ds;

    private final OrmMetaInfService metainf;

    private final EntitysCache cache;
    
    private final SQLDialect dialect;
    
    public EntityContextImpl(DataSource ds, SQLDialect dialect)
    {
        this.ds = ds;
        this.metainf = Ioc.context().find(OrmMetaInfService.class);
        this.cache = new EntitysCache();
        this.dialect = dialect;
    }

    @Override
    public <T> void fixTable(Class<T> entityClass) throws SQLException
    {
        EntityInf<T> entityInf = metainf.findEntityInf(entityClass);
        if(tableExists(entityInf))
        {
            fixColumns(entityInf);
        }
        else
        {
            createTable(entityInf);
        }
    }

    @Override
    public <T> T find(Class<T> entityClass, Object id) throws SQLException
    {
        T cachedEntity = cache.get(entityClass, id);
        if(cachedEntity != null)
        {
            return cachedEntity;
        }
        EntityInf<T> ei = metainf.findEntityInf(entityClass);
        SelectBuilder qb = new SelectBuilder();
        qb.select(ei.allFieldsCommaSep(ei.getTableName() + "."))
            .from(ei.getTableName())
            .where(ei.buildIdCondition())
            .limit(0, 1);

        return doQuery(qb.toString(), (rs) -> ei.parse(rs, this), id);
    }

    @Override
    public <T> T refresh(T entity) throws SQLException
    {
        EntityInf<T> ei = metainf.findEntityInf(entity.getClass());
        SelectBuilder qb = new SelectBuilder();
        qb.select(ei.allFieldsCommaSep(ei.getTableName() + "."))
            .from(ei.getTableName())
            .where(ei.buildIdCondition())
            .limit(0, 1);
        Object id = ei.getKeyField().getValue(entity);
        return doQuery(qb.toString(), (rs) -> ei.parse(entity, rs, this), id);
    }

    @Override
    public <T> T insert(T entity) throws SQLException
    {
        EntityInf<T> entityInf = metainf.findEntityInf(entity.getClass());
        InsertBuilder ib = new InsertBuilder();
        ib.insertInto(entityInf.getTableName())
                .fields(entityInf.allFieldsCommaSep(""))
                .valuesParams(entityInf.allFieldsCount());

        if(entityInf.getKeyField().isAutoIncrement())
        {
            doUpdate(ib.toString(), (rs) -> entityInf.updateKeyField(entity, rs, this) , entityInf.buildInsertParameters(entity));
        }
        else
        {
            doUpdate(ib.toString(), entityInf.buildInsertParameters(entity));
        }
        cache.put(entity, entityInf.findKeyValue(entity));
        return entity;
    }
    
    @Override
    public <T> T update(T entity) throws SQLException
    {
        return update(entity, null);
    }
    
    @Override
    public <T> T update(T entity, Object id) throws SQLException
    {
        EntityInf<T> entityInf = metainf.findEntityInf(entity.getClass());
        UpdateBuilder ub = new UpdateBuilder();

        ub.update(entityInf.getTableName());
        entityInf.allFieldsStream(entityInf.getTableName() + ".").forEach(ub::set);
        ub.where(entityInf.buildIdCondition());

        Object updateId = id;
        if(updateId == null)
        {
            updateId = entityInf.findKeyValue(entity);
        }
        doUpdate(ub.toString(), entityInf.buildUpdateParameters(entity, updateId));
        cache.remove(entity.getClass(), updateId);
        cache.put(entity, id);
        return entity;
    }

    @Override
    public <T> T delete(T entity) throws SQLException
    {
        EntityInf<T> entityInf = metainf.findEntityInf(entity.getClass());
        DeleteBuilder db = new DeleteBuilder();

        db.delete(entityInf.getTableName())
            .where(entityInf.buildIdCondition());

        doUpdate(db.toString(), entityInf.findKeyValue(entity));
        cache.remove(entity.getClass(), entityInf.findKeyValue(entity));
        return entity;
    }

    public <T> T doQuery(String query, QueryConsumer<T> consumer, Object... parameters) throws SQLException
    {
        T result;
        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            for (int i = 0; i < parameters.length; i++)
            {
                setParam(stmt, parameters[i], i + 1);
            }
            try (ResultSet resultSet = stmt.executeQuery())
            {
                result = consumer.parse(resultSet);
            }
        }
        return result;
    }

    public <T> T doUpdate(String query, QueryConsumer<T> consumer, Object... parameters) throws SQLException
    {
        T result = null;
        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            for (int i = 0; i < parameters.length; i++)
            {
                setParam(stmt, parameters[i], i + 1);
            }
            if(stmt.executeUpdate() > 0)
            {
                try(ResultSet rs = stmt.getGeneratedKeys())
                {
                    result = consumer.parse(rs);
                }
            }
        }
        return result;
    }

    public int doUpdate(String query, Object... parameters) throws SQLException
    {
        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            for (int i = 0; i < parameters.length; i++)
            {
                setParam(stmt, parameters[i], i + 1);
            }
            return stmt.executeUpdate();
        }
    }

    private <T> boolean tableExists(EntityInf<T> entityInf) throws SQLException
    {
        try (Connection conn = ds.getConnection())
        {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, null, entityInf.getTableName(), null))
            {
                if(resultSet.next())
                {
                    return true;
                }
            }
        }
        return false;
    }

    private <T> void fixColumns(EntityInf<T> entityInf) throws SQLException
    {
        List<FieldInf<T, ?>> fields = entityInf.getFields();
        for (FieldInf<T, ?> field : fields)
        {
            fixColumn(field);
        }
        List<RelationInf<T, ?>> relations = entityInf.getRelations();
        for (RelationInf<T, ?> relation : relations)
        {
            fixColumn(relation);
        }
    }

    private <T> void fixIndexs(EntityInf<T> entityInf) throws SQLException
    {
        List<FieldInf<T, ?>> fields = entityInf.getFields();
        for (FieldInf<T, ?> field : fields)
        {
            fixIndex(field);
        }
        List<RelationInf<T, ?>> relations = entityInf.getRelations();
        for (RelationInf<T, ?> relation : relations)
        {
            fixIndex(relation);
        }
    }
    
    private <T, C> void fixColumn(ColumnData column) throws SQLException
    {
        if(!columnExists(column.getTableData().getTableName(), column.getColumnName()))
        {
            String query = dialect.createColumn(column);
            doUpdate(query);
        }
    }
    
    private <T, C> void fixIndex(ColumnData column) throws SQLException
    {
        if(column.isIndexed() && !indexExists(column.getTableData().getTableName(), column.getColumnName()))
        {
            String query = dialect.createIndex(column);
            doUpdate(query);
        }
    }

    private <T, C> boolean columnExists(String tableName, String columnName) throws SQLException
    {
        try (Connection conn = ds.getConnection())
        {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet resultSet = metaData.getColumns(null, null, tableName, columnName))
            {
                if(resultSet.next())
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private <T, C> boolean indexExists(String tableName, String columnName) throws SQLException
    {
        try (Connection conn = ds.getConnection())
        {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet resultSet = metaData.getIndexInfo(null, null, tableName, false, true))
            {
                while(resultSet.next())
                {
                    String col = resultSet.getString("COLUMN_NAME");
                    if(col != null && col.equalsIgnoreCase(columnName))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private <T> void createTable(EntityInf<T> entityInf) throws SQLException
    {
        String query = dialect.createTable(entityInf);
        doUpdate(query);
        fixIndexs(entityInf);
    }

    @Override
    public <T> Query<T> query(Table<T> table)
    {
        EntityInf<T> eInf = metainf.findEntityInf(table.getEntityClass());
        return new QueryImpl<>(this, eInf);
    }

    private void setParam(PreparedStatement stmt, Object parameter, int index) throws SQLException
    {
        if(parameter instanceof Character) 
        {
            stmt.setString(index, parameter.toString());
        }
        else
        {
            stmt.setObject(index, parameter);
        }
    }

    public EntitysCache getEnittysCache()
    {
        return cache;
    }

    public OrmMetaInfService getMetainf()
    {
        return metainf;
    }

    @Override
    public void clearCache()
    {
        cache.clear();
    }
}
