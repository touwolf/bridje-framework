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

package org.bridje.sql.dialect;

import org.bridje.sql.Column;
import org.bridje.sql.Table;

public class MySQLDialect implements SQLDialect
{
    @Override
    public void writeObjectName(StringBuilder builder, String name)
    {
        builder.append('`');
        builder.append(name);
        builder.append('`');
    }

    @Override
    public void writeLimit(StringBuilder builder, int offset, int count)
    {
        builder.append(" LIMIT ");
        builder.append(offset);
        if(count > 0)
        {
            builder.append(", ");
            builder.append(count);
        }
    }

    @Override
    public void createTable(StringBuilder builder, Table table)
    {
        builder.append("CREATE TABLE ");
        writeObjectName(builder, table.getName());
        builder.append("\n");
    }

    @Override
    public void createColumn(StringBuilder builder, Column<?> column, boolean isKey)
    {
        builder.append(" ");
        writeObjectName(builder, column.getName());
        builder.append(" ");
        writeObjectName(builder, createType(column));
        writeObjectName(builder, createIsNull(column));
        writeObjectName(builder, createDefault(column));
        writeObjectName(builder, createAutoIncrement(column));
        builder.append("\n");
    }

    @Override
    public void primaryKey(StringBuilder builder, Column<?> column)
    {
        builder.append(" PRIMARY KEY (");
        writeObjectName(builder, column.getName());
        builder.append(")\n");
    }

    @Override
    public void alterTable(StringBuilder builder, Table table)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addColumn(StringBuilder builder, Column<?> column)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dropColumn(StringBuilder builder, Column<?> column)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alterColumn(StringBuilder builder, Column<?> column)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String createType(Column<?> column)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String createIsNull(Column<?> column)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String createDefault(Column<?> column)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String createAutoIncrement(Column<?> column)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
