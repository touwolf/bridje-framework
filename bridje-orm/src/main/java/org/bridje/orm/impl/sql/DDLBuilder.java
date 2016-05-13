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
 *
 */
public class DDLBuilder
{
    private final StringBuilder sb;

    private boolean firstColumn;

    private boolean skipNullStmtInColumns = false;

    private String autoIncrementStmt = "AUTO_INCREMENT";
    
    public DDLBuilder()
    {
        sb = new StringBuilder();
        firstColumn = true;
    }

    public boolean isSkipNullStmtInColumns()
    {
        return skipNullStmtInColumns;
    }

    public void setSkipNullStmtInColumns(boolean skipNullStmtInColumns)
    {
        this.skipNullStmtInColumns = skipNullStmtInColumns;
    }

    public String getAutoIncrementStmt()
    {
        return autoIncrementStmt;
    }

    public void setAutoIncrementStmt(String autoIncrementStmt)
    {
        this.autoIncrementStmt = autoIncrementStmt;
    }

    public DDLBuilder createTable(String tableName)
    {
        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(\n");
        return this;
    }
    
    public DDLBuilder alterTable(String tableName)
    {
        sb.append("ALTER TABLE ");
        sb.append(tableName);
        sb.append('\n');
        return this;
    }

    public String createIndex(String idxName, String tableName, String columnName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE INDEX ");
        sb.append(idxName);
        sb.append(" ON ");
        sb.append(tableName);
        sb.append(" ( ");
        sb.append(columnName);
        sb.append(" ASC)");
        return sb.toString();
    }
    
    public DDLBuilder column(String columnStmt)
    {
        sb.append("    ");
        sb.append(columnStmt);
        sb.append(", \n");
        return this;
    }

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