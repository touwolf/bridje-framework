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

package org.bridje.orm.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Logger LOG = Logger.getLogger(EntityContextImpl.class.getName());

    private final DataSource ds;

    private final OrmMetaInfService metainf;

    private final EntitysCache enittysCache;
    
    private final SQLDialect dialect;
    
    public EntityContextImpl(DataSource ds, SQLDialect dialect)
    {
        this.ds = ds;
        this.metainf = Ioc.context().find(OrmMetaInfService.class);
        this.enittysCache = new EntitysCache();
        this.dialect = dialect;
    }

    @Override
    public <T> void fixTable(Class<T> entityClass)
    {
        try
        {
            EntityInf<T> entityInf = metainf.findEntityInf(entityClass);
            if(entityInf == null)
            {
                throw new IllegalArgumentException(entityClass.getName() + " is not an entity class.");
            }
            if(tableExists(entityInf))
            {
                fixColumns(entityInf);
            }
            else
            {
                createTable(entityInf);
            }
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public <T> T find(Class<T> entityClass, Object id)
    {
        try
        {
            T cachedEntity = enittysCache.get(entityClass, id);
            if(cachedEntity != null)
            {
                return cachedEntity;
            }
            EntityInf<T> entityInf = metainf.findEntityInf(entityClass);
            SelectBuilder qb = new SelectBuilder();
            qb.select(entityInf.allFieldsCommaSep())
                .from(entityInf.getTableName())
                .where(entityInf.buildIdCondition())
                .limit(0, 1);

            return doQuery(qb.toString(), (rs) -> entityInf.parseEntity(rs, this), id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> T refresh(T entity)
    {
        try
        {
            EntityInf<T> entityInf = metainf.findEntityInf(entity.getClass());
            SelectBuilder qb = new SelectBuilder();
            qb.select(entityInf.allFieldsCommaSep())
                .from(entityInf.getTableName())
                .where(entityInf.buildIdCondition())
                .limit(0, 1);
            Object id = entityInf.getKeyField().getValue(entity);
            return doQuery(qb.toString(), (rs) -> entityInf.parseEntity(entity, rs, this), id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> T insert(T entity)
    {
        try
        {
            EntityInf<T> entityInf = metainf.findEntityInf(entity.getClass());

            InsertBuilder ib = new InsertBuilder();

            ib.insertInto(entityInf.getTableName())
                    .fields(entityInf.allFieldsCommaSep())
                    .valuesParams(entityInf.allFieldsCount());

            doUpdate(ib.toString(), entityInf.buildInsertParameters(entity));
            enittysCache.put(entity, entityInf.findKeyValue(entity));
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return entity;
    }
    
    @Override
    public <T> T update(T entity)
    {
        try
        {
            EntityInf<T> entityInf = metainf.findEntityInf(entity.getClass());

            UpdateBuilder ub = new UpdateBuilder();

            ub.update(entityInf.getTableName());
            entityInf.allFieldsStream().forEach(ub::set);
            ub.where(entityInf.buildIdCondition());

            doUpdate(ub.toString(), entityInf.buildUpdateParameters(entity));
            enittysCache.put(entity, entityInf.findKeyValue(entity));
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return entity;
    }

    @Override
    public <T> T delete(T entity)
    {
        try
        {
            EntityInf<T> entityInf = metainf.findEntityInf(entity.getClass());
            DeleteBuilder db = new DeleteBuilder();

            db.delete(entityInf.getTableName())
                .where(entityInf.buildIdCondition());

            doUpdate(db.toString(), entityInf.findKeyValue(entity));
            enittysCache.remove(entity.getClass(), entityInf.findKeyValue(entity));
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return entity;
    }

    public <T> T doQuery(String query, QueryConsumer<T> consumer, Object... parameters) throws SQLException
    {
        LOG.log(Level.INFO, query);
        T result = null;
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

    public int doUpdate(String query, Object... parameters) throws SQLException
    {
        LOG.log(Level.INFO, query);
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

    private <T> void fixColumns(EntityInf<T> entityInf)
    {
        entityInf.getFields().stream().forEach(this::fixColumn);
        entityInf.getRelations().stream().forEach(this::fixColumn);
    }
    
    private <T, C> void fixColumn(ColumnData column)
    {
        try
        {
            if(!columnExists(column.getTableData().getTableName(), column.getColumnName()))
            {
                String query = dialect.createColumn(column);
                doUpdate(query);
            }
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
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

    private <T> void createTable(EntityInf<T> entityInf) throws SQLException
    {
        String query = dialect.createTable(entityInf);
        doUpdate(query);
    }

    @Override
    public <T> Query<T> query(Table<T> entityTable)
    {
        EntityInf<T> entityInf = metainf.findEntityInf(entityTable.getEntityClass());
        return new QueryImpl<>(this, entityInf);
    }

    private void setParam(PreparedStatement stmt, Object parameter, int index) throws SQLException
    {
        if(parameter != null && parameter instanceof Character) 
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
        return enittysCache;
    }

    @Override
    public void clearCache()
    {
        enittysCache.clear();
    }
}
