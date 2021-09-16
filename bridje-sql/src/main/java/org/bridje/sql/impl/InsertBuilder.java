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

import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.*;

class InsertBuilder extends BuilderBase implements InsertIntoStep, ColumnsStep, ValuesStep, FinalStep, Query
{
    private final Table table;

    private Column<?, ?>[] columns;

    private SelectExpr select;

    private List<Expression<?, ?>[]> values;

    public InsertBuilder(Table table)
    {
        this.table = table;
    }

    @Override
    public ColumnsStep columns(Column<?, ?>... columns)
    {
        this.columns = columns;
        return this;
    }

    @Override
    public FinalStep select(SelectExpr select)
    {
        this.select = select;
        return this;
    }

    @Override
    public ValuesStep values(Object... row)
    {
        if(values == null) values = new ArrayList<>();
        values.add(toLiterals(row));
        return this;
    }

    @Override
    public SQLStatement toStatement(SQLDialect dialect, Object... parameters)
    {
        SQLBuilder builder = new SQLBuilderImpl(dialect);
        writeSQL(builder);
        String sql = builder.toString();
        return new SQLStatementImpl(table.getAutoIncrement(),
                            sql, createParams(builder, parameters), true);
    }

    @Override
    public Query toQuery()
    {
        return this;
    }

    @Override
    public Expression<?, ?>[] getResultFields()
    {
        return table.getAutoIncrement();
    }

    public void writeSQL(SQLBuilder builder)
    {
        builder.append("INSERT INTO ");
        builder.append(table);
        if(columns != null)
        {
            builder.append(" (");
            builder.setSimpleColumnNames(true);
            builder.appendAll(columns, ", ");
            builder.setSimpleColumnNames(false);
            builder.append(')');
        }
        if(select != null)
        {
            builder.append(' ');
            builder.append(select);
        }
        else if(values != null)
        {
            builder.append(" VALUES ");
            boolean first = true;
            for (Expression<?, ?>[] row : values)
            {
                if(!first) builder.append(", ");
                builder.append('(');
                builder.appendAll(row, ", ");
                builder.append(") ");
                first = false;
            }
        }
    }

    private Expression<?, ?>[] toLiterals(Object[] row)
    {
        Expression<?, ?>[] result = new Expression<?, ?>[row.length];
        for (int i = 0; i < row.length; i++)
        {
            Object object = row[i];
            if(object instanceof SQLWritable)
            {
                if(object instanceof Param
                        || object instanceof Literal)
                {
                    result[i] = (Param<?, ?>)object;
                }
                else
                {
                    throw new IllegalArgumentException("Only literals are allowed, value " + object.toString() + " is not a literal.");
                }
            }
            else
            {
                result[i] = new LiteralImpl<>(object, (SQLTypeImpl<Object,Object>)columns[i].getSQLType());
            }
        }
        return result;
    }

    @Override
    public boolean isWithGeneratedKeys()
    {
        return true;
    }
}
