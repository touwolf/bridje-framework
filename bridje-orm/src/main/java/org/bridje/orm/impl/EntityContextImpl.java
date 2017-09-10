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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.orm.EntityContext;
import org.bridje.orm.Query;
import org.bridje.orm.SQLDialect;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;
import org.bridje.orm.impl.sql.DeleteBuilder;
import org.bridje.orm.impl.sql.InsertBuilder;
import org.bridje.orm.impl.sql.SelectBuilder;
import org.bridje.orm.impl.sql.UpdateBuilder;

class EntityContextImpl implements EntityContext
{
    private static final Logger LOG = Logger.getLogger(EntityContextImpl.class.getName());

    private final DataSource ds;

    private final EntitysCache cache;

    private final SQLDialect dialect;

    private final OrmServiceImpl orm;

    public EntityContextImpl(OrmServiceImpl orm, DataSource ds, SQLDialect dialect)
    {
        this.orm = orm;
        this.ds = ds;
        this.cache = new EntitysCache();
        this.dialect = dialect;
    }

    @Override
    public void fixTable(Table<?>... tables) throws SQLException
    {
        if (tables != null)
        {
            for (Table<?> table : tables)
            {
                LOG.log(Level.INFO, "Checking table {0}", table.getName());
                if (tableExists(table))
                {
                    fixColumns(table);
                }
                else
                {
                    createTable(table);
                }
            }
        }
    }

    public <T> T find(Class<T> entity, Object id) throws SQLException
    {
        return find(orm.findTable(entity), id);
    }

    @Override
    public <T> T find(Table<T> table, Object id) throws SQLException
    {
        if(id == null) return null;
        TableImpl<T> tableImpl = (TableImpl<T>) table;
        T cachedEntity = cache.get(tableImpl.getEntity(), id);
        if (cachedEntity != null) return cachedEntity;
        SelectBuilder qb = new SelectBuilder(dialect);
        qb.select(tableImpl.allFieldsCommaSep())
                .from(dialect.identifier(tableImpl.getName()))
                .where(tableImpl.buildIdCondition())
                .limit(0, 1);

        Object serializedId = ((TableColumnImpl) table.getKey()).serialize(id);
        return doQuery(qb.toString(), (rs) -> tableImpl.parse(rs), serializedId);
    }

    @Override
    public <T> T refresh(T entity) throws SQLException
    {
        TableImpl<T> table = (TableImpl<T>) orm.findTable(entity.getClass());
        SelectBuilder qb = new SelectBuilder(dialect);
        qb.select(table.allFieldsCommaSep())
                .from(dialect.identifier(table.getName()))
                .where(table.buildIdCondition())
                .limit(0, 1);
        Object id = ((TableColumnImpl) table.getKey()).getQueryParameter(entity);
        return doQuery(qb.toString(), (rs) -> table.parse(entity, rs), id);
    }

    @Override
    public <T> T insert(T entity) throws SQLException
    {
        TableImpl<T> table = orm.findTable((Class<T>) entity.getClass());
        InsertBuilder ib = new InsertBuilder();
        ib.insertInto(dialect.identifier(table.getName()))
                .fields(table.nonAiFieldsCommaSepNoTable())
                .valuesParams(table.getNonAiColumns().size());

        if (table.getKey().isAutoIncrement())
        {
            doUpdate(ib.toString(),
                    rs -> table.updateKeyField(entity, rs),
                    table.buildInsertParameters(entity));
        }
        else
        {
            doUpdate(ib.toString(), table.buildInsertParameters(entity));
        }
        cache.put(entity, table.findKeyValue(entity));
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
        TableImpl<T> table = orm.findTable((Class<T>) entity.getClass());
        UpdateBuilder ub = new UpdateBuilder(dialect);
        ub.update(dialect.identifier(table.getName()));
        table.nonAiFieldsStream(dialect.identifier(table.getName()) + ".").forEach(ub::set);
        ub.where(table.buildIdCondition());
        Object updateId = id;
        if (updateId == null) updateId = table.findKeyValue(entity);
        Object serializedId = ((TableColumnImpl) table.getKey()).serialize(updateId);
        doUpdate(ub.toString(), table.buildUpdateParameters(entity, serializedId));
        cache.remove(entity.getClass(), updateId);
        cache.put(entity, id);
        return entity;
    }

    @Override
    public <T> T delete(T entity) throws SQLException
    {
        TableImpl<T> table = orm.findTable((Class<T>) entity.getClass());
        DeleteBuilder db = new DeleteBuilder();
        db.delete(dialect.identifier(table.getName())).where(table.buildIdCondition());
        Object updateId = table.findKeyValue(entity);
        if (updateId != null)
        {
            Object serializedId = ((TableColumnImpl) table.getKey()).serialize(updateId);
            doUpdate(db.toString(), serializedId);
            cache.remove(entity.getClass(), updateId);
        }
        return entity;
    }

    public <T> T doQuery(String query, QueryConsumer<T> consumer, Object... parameters) throws SQLException
    {
        T result;
        try (Connection conn = ds.getConnection())
        {
            if(conn == null) throw new SQLException("Could not create a new connection with the database.");
            try(PreparedStatement stmt = conn.prepareStatement(query))
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
        }
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, "Invalid Query: {0}", query);
            throw e;
        }
        return result;
    }

    public <T> T doUpdate(String query, QueryConsumer<T> consumer, Object... parameters) throws SQLException
    {
        T result = null;
        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            for (int i = 0; i < parameters.length; i++)
            {
                setParam(stmt, parameters[i], i + 1);
            }
            if (stmt.executeUpdate() > 0)
            {
                try (ResultSet rs = stmt.getGeneratedKeys())
                {
                    result = consumer.parse(rs);
                }
            }
        }
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, "Invalid Query: {0}", query);
            throw e;
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
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, "Invalid Query: {0}", query);
            throw e;
        }
    }

    private <T> boolean tableExists(Table<T> table) throws SQLException
    {
        try (Connection conn = ds.getConnection())
        {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, null, table.getName(), null))
            {
                if (resultSet.next()) return true;
            }
        }
        return false;
    }

    private <T> void fixColumns(Table<T> table) throws SQLException
    {
        List<TableColumn<T, ?>> fields = table.getColumns();
        for (TableColumn<T, ?> field : fields)
        {
            fixColumn(field);
        }
        List<String> columnNames = findColumnNames(table.getName());
        for (String columnName : columnNames)
        {
            TableColumn tbCol = table.findColumnByName(columnName);
            if (tbCol == null)
            {
                LOG.log(Level.WARNING, "Column {0} of table {1} no longer exists in the model.", new Object[]{ columnName, table.getName()});
            }
        }
    }

    private <T> void fixIndexs(Table<T> table) throws SQLException
    {
        List<TableColumn<T, ?>> fields = table.getColumns();
        for (TableColumn<T, ?> field : fields)
        {
            fixIndex(field);
        }
    }

    private <E, T> void fixColumn(TableColumn<E, T> column) throws SQLException
    {
        if (!columnExists(column.getTable().getName(), column.getName()))
        {
            String query = dialect.createColumn(column);
            LOG.log(Level.INFO, "Creating Column: \n{0}", query);
            doUpdate(query);
        }
    }

    private <E, T> void fixIndex(TableColumn<E, T> column) throws SQLException
    {
        if (column.isIndexed() && !indexExists(column.getTable().getName(), column.getName()))
        {
            String query = dialect.createIndex(column);
            LOG.log(Level.INFO, "Creating Index: \n{0}", query);
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
                if (resultSet.next()) return true;
            }
        }
        return false;
    }

    private <T, C> List<String> findColumnNames(String tableName) throws SQLException
    {
        List<String> result = new ArrayList<>();
        try (Connection conn = ds.getConnection())
        {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet resultSet = metaData.getColumns(null, null, tableName, null))
            {
                while (resultSet.next())
                {
                    result.add(resultSet.getString("COLUMN_NAME"));
                }
            }
        }
        return result;
    }

    private <T, C> boolean indexExists(String tableName, String columnName) throws SQLException
    {
        try (Connection conn = ds.getConnection())
        {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet resultSet = metaData.getIndexInfo(null, null, tableName, false, true))
            {
                while (resultSet.next())
                {
                    String col = resultSet.getString("COLUMN_NAME");
                    if (col != null && col.equalsIgnoreCase(columnName))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private <T> void createTable(Table<T> table) throws SQLException
    {
        String query = dialect.createTable(table);
        LOG.log(Level.INFO, "Creating Table: \n{0}", query);
        doUpdate(query);
        fixIndexs(table);
    }

    @Override
    public <T> Query<T> query(Table<T> table)
    {
        return new QueryImpl<>(this, (TableImpl<T>) table);
    }

    private void setParam(PreparedStatement stmt, Object parameter, int index) throws SQLException
    {
        if (parameter instanceof Character)
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

    @Override
    public void clearCache()
    {
        cache.clear();
    }

    @Override
    public SQLDialect getDialect()
    {
        return dialect;
    }

    @Override
    public <T> Table<T> findTable(Class<T> entity)
    {
        return orm.findTable(entity);
    }
}
