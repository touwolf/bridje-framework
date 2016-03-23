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
package org.bridje.orm.impl.dialects;

import java.io.StringWriter;
import org.bridje.orm.dialects.SQLDialect;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.orm.dialects.ColumnData;
import org.bridje.orm.dialects.TableData;

/**
 *
 */
@Component
class MySQLDialect implements SQLDialect
{
    private static final Logger LOG = Logger.getLogger(MySQLDialect.class.getName());

    @Override
    public boolean canHandle(DataSource dataSource)
    {
        try(Connection conn = dataSource.getConnection())
        {
            return conn.getMetaData().getDriverName().contains("MySQL");
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public <T> String createTable(TableData table)
    {
        StringBuilder sw = new StringBuilder();
        sw.append("CREATE TABLE `");
        sw.append(table.getTableName());
        sw.append("` (\n");
        sw.append(table.getColumns().stream().map((f) -> buildColumnStmt(f)).collect(Collectors.joining(",\n")));
        sw.append(", \nPRIMARY KEY (`");
        sw.append(table.getKeyColumn().getColumnName());
        sw.append("`)\n) ENGINE=InnoDB;");

        return sw.toString();
    }

    @Override
    public <T> String createColumn(ColumnData column)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE ");
        sb.append(column.getTableData().getTableName());
        sb.append("\nADD ");
        sb.append(buildColumnStmt(column));
        sb.append(";");
        return sb.toString();
    }

    public String buildColumnStmt(ColumnData column)
    {
        StringWriter sw = new StringWriter();
        
        sw.append("`");
        sw.append(column.getColumnName());
        sw.append("` ");
        sw.append(column.getSqlType().getName());
        if(column.getLength() > 0)
        {
            sw.append("(");
            sw.append(String.valueOf(column.getLength()));
            if(column.getPrecision() > 0 
                    && (column.getSqlType() == JDBCType.FLOAT || column.getSqlType() == JDBCType.DOUBLE || column.getSqlType() == JDBCType.DECIMAL) )
            {
                sw.append(", ");
                sw.append(String.valueOf(column.getPrecision()));
            }
            sw.append(")");
        }
        if(column.isKey())
        {
            sw.append(" NOT NULL");
        }
        else
        {
            if(column.getSqlType() == JDBCType.TIMESTAMP
                    || column.getSqlType() == JDBCType.TIMESTAMP_WITH_TIMEZONE)
            {
                sw.append(" DEFAULT '0000-00-00 00:00:00'");
            }
            else
            {
                sw.append(" DEFAULT NULL");
            }
        }
        
        return sw.toString();
    }
}
