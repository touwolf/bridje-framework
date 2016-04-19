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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.orm.SQLDialect;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;
import org.bridje.orm.impl.sql.DDLBuilder;

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
    public String createTable(Table<?> table)
    {
        DDLBuilder b = createDDLBuilder();
        b.createTable(table.getName());
        table.getColumns().stream()
                .map((f) -> buildColumnStmt(f, b))
                .forEach(b::column);
        b.primaryKey(table.getKey().getName());
        return b.toString();
    }

    @Override
    public String createColumn(TableColumn<?, ?> column)
    {
        DDLBuilder b = createDDLBuilder();
        b.alterTable(column.getTable().getName())
                .addColumn(buildColumnStmt(column, b));
        
        return b.toString();
    }

    @Override
    public String createIndex(TableColumn<?, ?> column)
    {
        DDLBuilder b = createDDLBuilder();
        return b.createIndex(column.getTable().getName(), column.getName());
    }

    public String buildColumnStmt(TableColumn<?, ?> column, DDLBuilder b)
    {
        return b.buildColumnStmt(column.getName(), 
                column.getSqlType().getName(), 
                column.getLength(), 
                column.getPrecision(), 
                column.isKey(), 
                column.isAutoIncrement(), 
                column.getDefaultValue());
    }

    private DDLBuilder createDDLBuilder()
    {
        return new DDLBuilder("`");
    }
}

