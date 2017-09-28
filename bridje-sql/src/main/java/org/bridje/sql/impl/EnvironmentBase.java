/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.sql.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.Column;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLQuery;
import org.bridje.sql.SQLResultSet;
import org.bridje.sql.Table;

abstract class EnvironmentBase implements SQLEnvironment
{
    private final SQLDialect dialect;

    public EnvironmentBase(SQLDialect dialect)
    {
        this.dialect = dialect;
    }

    @Override
    public SQLDialect getDialect()
    {
        return dialect;
    }

    @Override
    public int executeUpdate(SQLQuery query, Object... parameters) throws SQLException
    {
        return executeUpdate(query.toStatement(getDialect(), parameters));
    }

    @Override
    public SQLResultSet execute(SQLQuery query, Object... parameters) throws SQLException
    {
        return execute(query.toStatement(getDialect(), parameters));
    }

    protected int executeUpdate(Connection cnn, String sql, Object[] params) throws SQLException
    {
        try(PreparedStatement stmt = prepareStatement(cnn, sql, params))
        {
            return stmt.executeUpdate();
        }
    }

    protected SQLResultSet execute(Connection cnn, String sql, Object[] params) throws SQLException
    {
        try(PreparedStatement stmt = prepareStatement(cnn, sql, params))
        {
            try(ResultSet rs = stmt.executeQuery())
            {
                return null;
            }
        }
    }

    protected PreparedStatement prepareStatement(Connection cnn, String sql, Object[] params) throws SQLException
    {
        PreparedStatement stmt = cnn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++)
        {
            stmt.setObject(i+1, params[i]);
        }
        return stmt;
    }

    protected void fixTable(Connection connection, Table table) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        if(tableExists(metaData, table))
        {
            fixColumns(connection, table);
        }
        else
        {
            createTable(connection, table);
        }
        fixIndexes(connection, table);
    }

    protected boolean tableExists(DatabaseMetaData metadata, Table table) throws SQLException
    {
        try (ResultSet resultSet = metadata.getTables(null, null, table.getName(), null))
        {
            if (resultSet.next()) return true;
        }
        return false;
    }

    protected boolean columnExists(DatabaseMetaData metadata, Column<?> column) throws SQLException
    {
        try (ResultSet resultSet = metadata.getColumns(null, null, column.getTable().getName(), column.getName()))
        {
            if (resultSet.next()) return true;
        }
        return false;
    }

    protected boolean indexExists(DatabaseMetaData metadata, Column column) throws SQLException
    {
        try (ResultSet resultSet = metadata.getIndexInfo(null, null, column.getTable().getName(), false, true))
        {
            while (resultSet.next())
            {
                String col = resultSet.getString("COLUMN_NAME");
                if (col != null && col.equalsIgnoreCase(column.getName()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    protected void createTable(Connection connection, Table table) throws SQLException
    {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dialect.createTable(sb, table);
        for (Column<?> column : table.getColumns())
        {
            dialect.createColumn(sb, params, column, column.isKey());
        }
        dialect.primaryKey(sb, table.getKeys());
        String sql = sb.toString();
        executeUpdate(connection, sql, params.toArray());
    }

    protected void fixIndexes(Connection connection, Table table) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        Column<?>[] columns = table.getColumns();
        StringBuilder sb = new StringBuilder();
        dialect.alterTable(sb, table);
        for (Column<?> column : columns)
        {
            if(column.isIndexed())
            {
                if(!indexExists(metadata, column))
                {
                    String idxName = "idx_" + column.getName();
                    dialect.createIndex(sb, idxName, column.getTable(), new Column<?>[]{column});
                }
            }
        }
        String sql = sb.toString();
        executeUpdate(connection, sql, new Object[0]);
    }

    protected void fixColumns(Connection connection, Table table) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        List<Object> params = new ArrayList<>();
        Column<?>[] columns = table.getColumns();
        StringBuilder sb = new StringBuilder();
        dialect.alterTable(sb, table);
        int index = 0;
        for (Column<?> column : columns)
        {
            index++;
            if(!columnExists(metadata, column))
            {
                dialect.addColumn(sb, params, column, index == columns.length );
            }
        }
        String sql = sb.toString();
        executeUpdate(connection, sql, params.toArray());
    }
}
