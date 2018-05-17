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

package org.bridje.sql.dialects;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.sql.Column;
import org.bridje.sql.ForeignKey;
import org.bridje.sql.ForeignKeyStrategy;
import org.bridje.sql.Index;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.Table;

/**
 * A dialect for DERBY database.
 */
@Component
public class PostgreSQLDialect implements SQLDialect
{
    private static final Logger LOG = Logger.getLogger(PostgreSQLDialect.class.getName());

    @Override
    public boolean canHandle(Connection connection)
    {
        try
        {
            return connection.getMetaData().getDriverName().contains("PostgreSQL");
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public void writeObjectName(StringBuilder builder, String name)
    {
        builder.append('\"');
        builder.append(name);
        builder.append('\"');
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
    public String createTable(Table table, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        createTable(builder, table);
        Column<?, ?>[] columns = table.getColumns();
        for (Column<?, ?> column : columns)
        {
            createColumn(builder, params, column);
        }
        primaryKey(builder, table.getPrimaryKey());
        return builder.toString();
    }

    @Override
    public String addColumn(Column<?, ?> column, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        alterTable(builder, column.getTable());
        addColumn(builder, params, column, true);
        return builder.toString();
    }

    @Override
    public String dropColumn(Column<?, ?> column, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        alterTable(builder, column.getTable());
        dropColumn(builder, column, true);
        return builder.toString();
    }

    @Override
    public String changeColumn(String oldName, Column<?, ?> column, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        alterTable(builder, column.getTable());
        changeColumn(builder, params, column, oldName, true);
        return builder.toString();
    }

    @Override
    public String createIndex(Index index, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        createIndex(builder, index.getName(), index.getTable(), index.getColumns(), index.isUnique());
        return builder.toString();
    }

    @Override
    public String dropIndex(Index index, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        dropIndex(builder, index.getName(), index.getTable());
        return builder.toString();
    }

    @Override
    public String createForeignKey(ForeignKey fk, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        alterTable(builder, fk.getTable());
        addForeignKey(builder, fk);
        return builder.toString();
    }

    @Override
    public String dropForeignKey(ForeignKey fk, List<Object> params)
    {
        StringBuilder builder = new StringBuilder();
        
        return builder.toString();
    }

    public void createTable(StringBuilder builder, Table table)
    {
        builder.append("CREATE TABLE ");
        writeObjectName(builder, table.getName());
        builder.append(" (\n");
    }

    public void createColumn(StringBuilder builder, List<Object> params, Column<?, ?> column)
    {
        builder.append(" ");
        writeObjectName(builder, column.getName());
        builder.append(" ");
        builder.append(createType(column));
        builder.append(createIsNull(column));
        builder.append(createDefault(column, params));
        builder.append(",\n");
    }

    public void createIndex(StringBuilder builder, String name, Table table, Column<?, ?>[] columns, boolean unique)
    {
        builder.append("CREATE ");
        if(unique) builder.append("UNIQUE ");
        builder.append("INDEX ");
        writeObjectName(builder, name);
        builder.append(" ON ");
        writeObjectName(builder, table.getName());
        builder.append(" ( ");
        writeColumnsNames(builder, columns, ", ");
        builder.append(" ) ");
    }

    public void createUniqueIndex(StringBuilder builder, String name, Table table, Column<?, ?>[] columns)
    {
        builder.append("CREATE UNIQUE INDEX ");
        writeObjectName(builder, name);
        builder.append(" ON ");
        writeObjectName(builder, table.getName());
        builder.append(" ( ");
        writeColumnsNames(builder, columns, ", ");
        builder.append(" ) ");
    }

    public void primaryKey(StringBuilder builder, Column<?, ?>[] columns)
    {
        builder.append(" PRIMARY KEY (");
        writeColumnsNames(builder, columns, ", ");
        builder.append(")\n)");
    }

    public void alterTable(StringBuilder builder, Table table)
    {
        builder.append("ALTER TABLE ");
        writeObjectName(builder, table.getName());
        builder.append(" \n");
    }

    public void addColumn(StringBuilder builder, List<Object> params, Column<?, ?> column, boolean isLast)
    {
        builder.append(" ADD COLUMN ");
        writeObjectName(builder, column.getName());
        builder.append(" ");
        builder.append(createType(column));
        builder.append(createIsNull(column));
        builder.append(createDefault(column, params));
        if(!isLast) builder.append(",");
        builder.append("\n");
    }

    public void dropColumn(StringBuilder builder, Column<?, ?> column, boolean isLast)
    {
        builder.append(" DROP COLUMN ");
        writeObjectName(builder, column.getName());
        if(!isLast) builder.append(",");
        builder.append("\n");
    }

    public void changeColumn(StringBuilder builder, List<Object> params, Column<?, ?> column, String oldColumn, boolean isLast)
    {
        builder.append(" CHANGE COLUMN ");
        writeObjectName(builder, oldColumn);
        builder.append(" ");
        writeObjectName(builder, column.getName());
        builder.append(" ");
        builder.append(createType(column));
        builder.append(createIsNull(column));
        builder.append(createDefault(column, params));
        if(!isLast) builder.append(",");
        builder.append("\n");
    }

    public void dropIndex(StringBuilder builder, String name, Table table)
    {
        builder.append(" ALTER TABLE ");
        writeObjectName(builder, table.getName());
        builder.append(" DROP INDEX ");
        writeObjectName(builder, name);
    }

    private String createType(Column<?, ?> column)
    {
        if(column.isAutoIncrement()) return "SERIAL";
        switch(column.getSQLType().getJDBCType())
        {
            case BIT:
                return "BOOLEAN";
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
                if(column.getSQLType().getLength() > 0)
                {
                    return column.getSQLType().getJDBCType().getName() + "(" + column.getSQLType().getLength() + ")";
                }
                break;
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
                if(column.getSQLType().getLength() > 0 && column.getSQLType().getPrecision() > 0)
                {
                    return column.getSQLType().getJDBCType().getName() + "(" + column.getSQLType().getLength() + ", " + column.getSQLType().getPrecision() + ")";
                }
                break;
            case VARCHAR:
            case NVARCHAR:
                if(column.getSQLType().getLength() > 21844)
                {
                    return "TEXT";
                }
                if(column.getSQLType().getLength() > 65535)
                {
                    return "MEDIUMTEXT";
                }
                if(column.getSQLType().getLength() > 16777215)
                {
                    return "LONGTEXT";
                }
                if(column.getSQLType().getLength() > 0)
                {
                    return "VARCHAR(" + column.getSQLType().getLength() + ")";
                }
                return "VARCHAR";
            case CHAR:
            case NCHAR:
                if(column.getSQLType().getLength() > 0)
                {
                    return "CHAR(" + column.getSQLType().getLength() + ")";
                }
                return "CHAR";
            case LONGNVARCHAR:
            case LONGVARCHAR:
                return "LONG VARCHAR";
            default:
                break;
        }
        return column.getSQLType().getJDBCType().getName();
    }

    private String createIsNull(Column<?, ?> column)
    {
        if(column.isAllowNull() && !column.isKey()) return "";
        return " NOT NULL";
    }

    private String createDefault(Column<?, ?> column, List<Object> params)
    {
        if(column.isAutoIncrement()) return "";
        if(column.getDefValue() != null)
        {
            params.add(column.getDefValue());
            return "DEFAULT ?";
        }
        if(column.getSQLType().getJDBCType()== JDBCType.TIMESTAMP
                || column.getSQLType().getJDBCType() == JDBCType.TIMESTAMP_WITH_TIMEZONE)
        {
            return " DEFAULT '0000-00-00 00:00:00'";
        }
        return "";
    }

    private void addForeignKey(StringBuilder builder, ForeignKey fk)
    {
        builder.append("ADD CONSTRAINT ");
        writeObjectName(builder, fk.getName());
        builder.append(" FOREIGN KEY (");
        writeColumnsNames(builder, fk.getColumns(), ", ");
        builder.append(") REFERENCES ");
        writeObjectName(builder, fk.getReferences().getName());
        builder.append(" (");
        writeColumnsNames(builder, fk.getReferences().getPrimaryKey(), ", ");
        if(fk.getOnDelete() != ForeignKeyStrategy.SET_DEFAULT)
        {
            builder.append(") ON DELETE ");
            builder.append(fk.getOnDelete().name().replace("_", " "));
        }
        if(fk.getOnUpdate() == ForeignKeyStrategy.NO_ACTION)
        {
            builder.append(" ON UPDATE ");
            builder.append(fk.getOnUpdate().name().replace("_", " "));
        }
    }

    private void writeColumnsNames(StringBuilder builder, Column<?, ?>[] columns, String sep)
    {
        boolean isFirst = true;
        for (Column<?, ?> column : columns)
        {
            if(!isFirst) builder.append(sep);
            writeObjectName(builder, column.getName());
            isFirst = false;
        }        
    }

    @Override
    public void writeLimit(StringBuilder builder, int count)
    {
        builder.append(" LIMIT ");
        builder.append(count);
    }
}
