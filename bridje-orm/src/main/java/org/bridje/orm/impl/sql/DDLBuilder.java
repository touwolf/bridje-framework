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

package org.bridje.orm.impl.sql;

import java.io.StringWriter;

/**
 * A helper class for building DDL statements more easy.
 */
public class DDLBuilder
{
    private final StringBuilder sb;

    private boolean firstColumn;

    private boolean skipNullStmtInColumns;

    private String autoIncrementStmt = "AUTO_INCREMENT";
    
    /**
     * Default Constructor
     */
    public DDLBuilder()
    {
        sb = new StringBuilder();
        firstColumn = true;
    }

    /**
     * If the NULL statement must be add or not to the COLUMNS statement.
     * 
     * @return true the NULL statement will be skipped false the NULL statement will be present.
     */
    public boolean isSkipNullStmtInColumns()
    {
        return skipNullStmtInColumns;
    }

    /**
     * If the NULL statement must be add or not to the COLUMNS statement.
     * 
     * @param skipNullStmtInColumns true the NULL statement will be skipped false the NULL statement will be present.
     */
    public void setSkipNullStmtInColumns(boolean skipNullStmtInColumns)
    {
        this.skipNullStmtInColumns = skipNullStmtInColumns;
    }

    /**
     * Get the string representing the auto increment field clause in the COLUMN statement.
     * 
     * @return The auto increment statement to be use by this builder.
     */
    public String getAutoIncrementStmt()
    {
        return autoIncrementStmt;
    }

    /**
     * Set the string representing the auto increment field clause in the COLUMN statement.
     * 
     * @param autoIncrementStmt The auto increment statement to be use by this builder.
     */
    public void setAutoIncrementStmt(String autoIncrementStmt)
    {
        this.autoIncrementStmt = autoIncrementStmt;
    }

    /**
     * Adds the CREATE TABLE statement to the builder.
     * 
     * @param tableName The name of the table to create.
     * @return this builder.
     */
    public DDLBuilder createTable(String tableName)
    {
        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(\n");
        return this;
    }
    
    /**
     * Adds the ALTER TABLE statement to the builder.
     * 
     * @param tableName The name of the table to alter.
     * @return this builder.
     */
    public DDLBuilder alterTable(String tableName)
    {
        sb.append("ALTER TABLE ");
        sb.append(tableName);
        sb.append('\n');
        return this;
    }

    /**
     * Adds the CREATE INDEX statement to the builder.
     * 
     * @param idxName The index name.
     * @param tableName The name of the table to alter.
     * @param columnName The column for the index.
     * @return this builder.
     */
    public String createIndex(String idxName, String tableName, String columnName)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("CREATE INDEX ");
        stb.append(idxName);
        stb.append(" ON ");
        stb.append(tableName);
        stb.append(" ( ");
        stb.append(columnName);
        stb.append(" ASC)");
        return stb.toString();
    }
    
    /**
     * Adds a new column statement to the builder.
     * 
     * @param columnStmt The column statement to add.
     * @return this builder.
     */
    public DDLBuilder column(String columnStmt)
    {
        sb.append("    ");
        sb.append(columnStmt);
        sb.append(", \n");
        return this;
    }

    /**
     * Adds a new ADD COLUMN statement to the builder.
     * 
     * @param columnStmt The column statement to add.
     * @return this builder.
     */
    public DDLBuilder addColumn(String columnStmt)
    {
        if(!firstColumn)
        {
            sb.append(", \n");
        }
        firstColumn = false;
        sb.append("    ADD ");
        sb.append(columnStmt);
        sb.append('\n');
        return this;
    }
    
    /**
     * Adds the PRIMARY KEY statement to the column.
     * 
     * @param columnName The primary key column name.
     * @return this builder.
     */
    public DDLBuilder primaryKey(String columnName)
    {
        sb.append("    PRIMARY KEY (");
        sb.append(columnName);
        sb.append(")\n)");
        return this;
    }
    
    @Override
    public String toString()
    {
        return sb.toString();
    }
    
    /**
     * Creates a new column statement.
     * 
     * @param columnName The column name.
     * @param sqlType The SQL type.
     * @param length The length for char and numeric base types
     * @param precision The precision for decimal types.
     * @param isKey If this column is a key column.
     * @param autoIncement If this column is auto increment column.
     * @param def The default value for the column.
     * @return The column statement.
     */
    public String buildColumnStmt(String columnName, String sqlType, int length, int precision, boolean isKey, boolean autoIncement, String def)
    {
        StringWriter sw = new StringWriter();
        
        sw.append(columnName);
        sw.append(' ');
        sw.append(sqlType);
        if(length > 0)
        {
            sw.append("(");
            sw.append(String.valueOf(length));
            if(precision > 0)
            {
                sw.append(", ");
                sw.append(String.valueOf(precision));
            }
            sw.append(")");
        }
        if(isKey)
        {
            sw.append(" NOT NULL");
            if(autoIncement)
            {
                sw.append(' ');
                sw.append(autoIncrementStmt);
            }
        }
        else
        {
            if(!skipNullStmtInColumns)
            {
                sw.append(" NULL");
            }
            if(def != null)
            {
                sw.append(" DEFAULT ");
                sw.append(def);
            }
        }
        
        return sw.toString();
    }
}