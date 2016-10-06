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

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.orm.SQLDialect;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;
import org.bridje.orm.impl.sql.DDLBuilder;

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
    public String createTable(Table<?> table)
    {
        DDLBuilder b = createDDLBuilder();
        b.createTable(identifier(table.getName()));
        table.getColumns().stream()
                .map((f) -> buildColumnStmt(f, b))
                .forEach(b::column);
        b.primaryKey(identifier(table.getKey().getName()));
        return b.toString();
    }

    @Override
    public String createColumn(TableColumn<?, ?> column)
    {
        DDLBuilder b = createDDLBuilder();
        b.alterTable(identifier(column.getTable().getName()))
                .addColumn(buildColumnStmt(column, b));
        
        return b.toString();
    }

    @Override
    public String createIndex(TableColumn<?, ?> column)
    {
        DDLBuilder b = createDDLBuilder();
        String idxName = identifier("idx_" + column.getTable().getName() + "_" + column.getName());
        return b.createIndex(idxName, identifier(column.getTable().getName()), identifier(column.getName()));
    }

    public String buildColumnStmt(TableColumn<?, ?> column, DDLBuilder b)
    {
        return b.buildColumnStmt(identifier(column.getName()), 
                findSqlType(column), 
                column.getLength(), 
                column.getPrecision(), 
                column.isKey(), 
                column.isAutoIncrement(), 
                column.isRequired(),
                findDefaultValue(column));
    }

    private DDLBuilder createDDLBuilder()
    {
        return new DDLBuilder();
    }

    @Override
    public String identifier(String name)
    {
        return "`" + name + "`";
    }

    public String findDefaultValue(TableColumn col)
    {
        String def = null;
        if(!col.isKey())
        {
            if(col.getSqlType() == JDBCType.TIMESTAMP
                    || col.getSqlType() == JDBCType.TIMESTAMP_WITH_TIMEZONE)
            {
                def = "'0000-00-00 00:00:00'";
            }
            else
            {
                def = "NULL";
            }
        }
        return def;
    }

    private String findSqlType(TableColumn<?, ?> column)
    {
        switch(column.getSqlType())
        {
            case VARCHAR:
            case NVARCHAR:
                if(column.getLength() > 21845)
                {
                    return "TEXT";
                }
                return "VARCHAR";
            case LONGNVARCHAR:
            case LONGVARCHAR:
                return "LONGTEXT";
        }
        return column.getSqlType().getName();
    }
}

