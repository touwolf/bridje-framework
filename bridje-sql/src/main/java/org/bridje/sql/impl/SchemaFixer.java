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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.sql.Column;
import org.bridje.sql.ForeignKey;
import org.bridje.sql.Index;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLStatement;
import org.bridje.sql.Schema;
import org.bridje.sql.Table;

class SchemaFixer
{
    private static final Logger LOG = Logger.getLogger(SchemaFixer.class.getName());

    private final Connection connection;
    
    private final Schema schema;

    private final SQLDialect dialect;

    public SchemaFixer(Connection connection, SQLDialect dialect, Schema schema)
    {
        this.dialect = dialect;
        this.connection = connection;
        this.schema = schema;
    }

    public void doFix() throws SQLException
    {
        fixTables(connection, schema.getTables());
        fixForeignKeys(connection, schema.getForeignKeys());
        fixIndexes(connection, schema.getIndexes());
    }

    private void fixTables(Connection connection, Table[] tables) throws SQLException
    {
        for (Table table : tables)
        {
            fixTable(connection, table);
        }
    }

    private void fixIndexes(Connection connection, Index[] indexes) throws SQLException
    {
        for (Index index : indexes)
        {
            fixIndex(connection, index);
        }
    }

    private void fixForeignKeys(Connection connection, ForeignKey[] foreignKeys) throws SQLException
    {
        for (ForeignKey fk : foreignKeys)
        {
            fixForeignKey(connection, fk);
        }
    }

    private void fixTable(Connection connection, Table table) throws SQLException
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
        fixForeignKeys(connection, table.getForeignKeys());
        fixIndexes(connection, table.getIndexes());
    }

    private boolean tableExists(DatabaseMetaData metadata, Table table) throws SQLException
    {
        try (ResultSet resultSet = metadata.getTables(null, null, table.getName(), null))
        {
            if (resultSet.next()) return true;
        }
        return false;
    }

    private boolean columnExists(DatabaseMetaData metadata, Column<?> column) throws SQLException
    {
        try (ResultSet resultSet = metadata.getColumns(null, null, column.getTable().getName(), column.getName()))
        {
            if (resultSet.next()) return true;
        }
        return false;
    }

    private void fixColumns(Connection connection, Table table) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        for (Column<?> column : table.getColumns())
        {
            if(!columnExists(metadata, column))
            {
                List<Object> params = new ArrayList<>();
                String sql = dialect.addColumn(column, params);
                SQLStatement sqlStmt = new SQLStatementImpl(null, sql, params.toArray(), false);
                executeStmt(connection, sqlStmt);
            }
        }
    }
    
    private void createTable(Connection connection, Table table) throws SQLException
    {
        List<Object> params = new ArrayList<>();
        String sql = dialect.createTable(table, params);
        SQLStatement sqlStmt = new SQLStatementImpl(null, sql, params.toArray(), false);
        executeStmt(connection, sqlStmt);
    }

    private void fixIndex(Connection connection, Index index) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        if(!indexExists(metadata, index))
        {
            List<Object> params = new ArrayList<>();
            String sql = dialect.createIndex(index, params);
            SQLStatement sqlStmt = new SQLStatementImpl(null, sql, new Object[0], false);
            executeStmt(connection, sqlStmt);
        }
    }

    private boolean indexExists(DatabaseMetaData metadata, Index index) throws SQLException
    {
        List<String> columnNames = new ArrayList<>();
        for (Column<?> column : index.getColumns())
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

    private void fixForeignKey(Connection connection, ForeignKey fk) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        if(!foreignKeyExists(metadata, fk))
        {
            List<Object> params = new ArrayList<>();
            String sql = dialect.createForeignKey(fk, params);
            SQLStatement sqlStmt = new SQLStatementImpl(null, sql, new Object[0], false);
            executeStmt(connection, sqlStmt);
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
