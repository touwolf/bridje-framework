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

public class SchemaFixer
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
        fixIndexes(connection, schema.getIndexes());
        fixForeignKeys(connection, schema.getForeignKeys());       
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
        SQLStatement sqlStmt = new SQLStatementImpl(null, sql, params.toArray(), false);
        executeStmt(connection, sqlStmt);
    }
    
    private void createTable(Connection connection, Table table) throws SQLException
    {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dialect.createTable(sb, table);
        for (Column<?> column : table.getColumns())
        {
            dialect.createColumn(sb, params, column, column.isKey());
        }
        dialect.primaryKey(sb, table.getPrimaryKey());
        String sql = sb.toString();
        SQLStatement sqlStmt = new SQLStatementImpl(null, sql, params.toArray(), false);
        executeStmt(connection, sqlStmt);
    }

    private void fixIndex(Connection connection, Index table) throws SQLException
    {
        /*
        DatabaseMetaData metadata = connection.getMetaData();
        Column<?>[] columns = table.getColumns();
        for (Column<?> column : columns)
        {
            if(column.getIndex() != null && column.getIndex() != ColumnIndexType.NONE)
            {
                if(!indexExists(metadata, column.getTable(), column))
                {
                    StringBuilder sb = new StringBuilder();
                    String idxName = "idx_" + column.getName();
                    dialect.createIndex(sb, idxName, column.getTable(), new Column<?>[]{column}, column.getIndex() == ColumnIndexType.UNIQUE);
                    String sql = sb.toString();
                    SQLStatement sqlStmt = new SQLStatementImpl(null, sql, new Object[0], false);
                    update(connection, sqlStmt);
                }
            }
        }
        */
    }

    private boolean indexExists(DatabaseMetaData metadata, Table table, Column<?>... columns) throws SQLException
    {
        List<String> columnNames = new ArrayList<>();
        for (Column<?> column : columns)
        {
            columnNames.add(column.getName());
        }
        Map<String, List<String>> idxMap = new HashMap<>();
        try (ResultSet resultSet = metadata.getIndexInfo(null, null, table.getName(), false, true))
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
        idxMap.forEach( (k, v) -> System.out.println(k + " " + Arrays.toString(v.toArray())));
        return idxMap.values()
                    .stream()
                    .filter( v -> Arrays.equals(v.toArray(), columnNames.toArray()))
                    .count() >= 1;
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

    private void fixForeignKey(Connection connection, ForeignKey fk)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
