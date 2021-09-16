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

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.sql.*;

class SchemaClearer
{
    private static final Logger LOG = Logger.getLogger(SchemaClearer.class.getName());

    private final Connection connection;

    private final Schema schema;

    private final SQLDialect dialect;

    public SchemaClearer(Connection connection, SQLDialect dialect, Schema schema)
    {
        this.dialect = dialect;
        this.connection = connection;
        this.schema = schema;
    }

    public void doClear() throws SQLException
    {
        clearForeignKeys(connection, schema.getForeignKeys());
        clearIndexes(connection, schema.getIndexes());
        clearTables(connection, schema.getTables());
    }

    private void clearTables(Connection connection, Table[] tables) throws SQLException
    {
        for (Table table : tables)
        {
            clearForeignKeys(connection, table);
        }

        for (Table table : tables)
        {
            clearTable(connection, table);
        }
    }

    private void clearIndexes(Connection connection, Index[] indexes) throws SQLException
    {
        for (Index index : indexes)
        {
            clearIndex(connection, index);
        }
    }

    private void clearForeignKeys(Connection connection, ForeignKey[] foreignKeys) throws SQLException
    {
        for (ForeignKey fk : foreignKeys)
        {
            clearForeignKey(connection, fk);
        }
    }

    private void clearTable(Connection connection, Table table) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();
        if(tableExists(metaData, table))
        {
            clearIndexes(connection, table.getIndexes());
            createTable(connection, table);
        }
    }

    private void clearForeignKeys(Connection connection, Table table) throws SQLException
    {
        clearForeignKeys(connection, table.getForeignKeys());
    }

    private boolean tableExists(DatabaseMetaData metadata, Table table) throws SQLException
    {
        try (ResultSet resultSet = metadata.getTables(null, null, table.getName(), null))
        {
            if (resultSet.next()) return true;
        }
        return false;
    }

    private void createTable(Connection connection, Table table) throws SQLException
    {
        try
        {
            List<Object> params = new ArrayList<>();
            String sql = dialect.dropTable(table, params);
            SQLStatement sqlStmt = new SQLStatementImpl(null, sql, params.toArray(), false);
            executeStmt(connection, sqlStmt);
        }
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, String.format("Could not create table %s.", table.getName()), e);
        }
    }

    private void clearIndex(Connection connection, Index index) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        if(indexExists(metadata, index))
        {
            try
            {
                List<Object> params = new ArrayList<>();
                String sql = dialect.dropIndex(index, params);
                SQLStatement sqlStmt = new SQLStatementImpl(null, sql, new Object[0], false);
                executeStmt(connection, sqlStmt);
            }
            catch (SQLException e)
            {
                LOG.log(Level.SEVERE, String.format("Could not create index %s on table %s.", index.getName(), index.getTable().getName()), e);
            }
        }
    }

    private boolean indexExists(DatabaseMetaData metadata, Index index) throws SQLException
    {
        List<String> columnNames = new ArrayList<>();
        for (Column<?, ?> column : index.getColumns())
        {
            columnNames.add(column.getName());
        }
        Map<String, List<String>> idxMap = new HashMap<>();
        try (ResultSet resultSet = metadata.getIndexInfo(null, null, index.getTable().getName(), false, true))
        {
            while (resultSet.next())
            {
                String indexName = resultSet.getString("INDEX_NAME");
                String colName = resultSet.getString("COLUMN_NAME");
                List<String> lst = idxMap.get(indexName);
                if(lst == null)
                {
                    lst = new ArrayList<>();
                    idxMap.put(indexName, lst);
                }
                lst.add(colName);
            }
        }
        return idxMap.values()
                    .stream()
                    .filter( v -> Arrays.equals(v.toArray(), columnNames.toArray()))
                    .count() >= 1;
    }

    private void clearForeignKey(Connection connection, ForeignKey fk) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        if(foreignKeyExists(metadata, fk))
        {
            try
            {
                List<Object> params = new ArrayList<>();
                String sql = dialect.dropForeignKey(fk, params);
                SQLStatement sqlStmt = new SQLStatementImpl(null, sql, new Object[0], false);
                executeStmt(connection, sqlStmt);
            }
            catch (SQLException e)
            {
                LOG.log(Level.SEVERE, String.format("Could not create foreign key %s on table %s.", fk.getName(), fk.getTable().getName()), e);
            }
        }
    }

    private boolean foreignKeyExists(DatabaseMetaData metadata, ForeignKey fk) throws SQLException
    {
        try (ResultSet resultSet = metadata.getExportedKeys(null, null, fk.getReferences().getName()))
        {
            while (resultSet.next())
            {
                String fkTableName = resultSet.getString("FKTABLE_NAME");
                if(fk.getTable().getName().equals(fkTableName))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private PreparedStatement prepareStatement(Connection cnn, SQLStatement sqlStmt) throws SQLException
    {
        PreparedStatement stmt;
        if(sqlStmt.isWithGeneratedKeys())
        {
            stmt = cnn.prepareStatement(sqlStmt.getSQL(), Statement.RETURN_GENERATED_KEYS);
        }
        else
        {
            stmt = cnn.prepareStatement(sqlStmt.getSQL());
        }

        Object[] params = sqlStmt.getParameters();
        for (int i = 0; i < params.length; i++)
        {
            stmt.setObject(i+1, params[i]);
        }
        return stmt;
    }

    private int executeStmt(Connection cnn, SQLStatement sqlStmt) throws SQLException
    {
        LOG.log(Level.INFO, sqlStmt.getSQL());
        try(PreparedStatement stmt = prepareStatement(cnn, sqlStmt))
        {
            return stmt.executeUpdate();
        }
    }
}
