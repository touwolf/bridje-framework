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

package org.bridje.sql;

import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.dialect.SQLDialect;
import org.bridje.sql.expr.LiteralExpr;
import org.bridje.sql.expr.SQLStatement;
import org.bridje.sql.expr.SQLWritable;
import org.bridje.sql.expr.SelectExpr;
import org.bridje.sql.flow.ColumnsStep;
import org.bridje.sql.flow.FinalStep;
import org.bridje.sql.flow.InsertIntoStep;
import org.bridje.sql.flow.ValuesStep;

class InsertBuilder implements InsertIntoStep, ColumnsStep, ValuesStep, FinalStep
{
    private final Table table;
    
    private Column<?>[] columns;

    private SelectExpr select;
    
    private List<LiteralExpr<?>[]> values;

    public InsertBuilder(Table table)
    {
        this.table = table;
    }

    @Override
    public ColumnsStep columns(Column<?>... columns)
    {
        this.columns = columns;
        return this;
    }

    @Override
    public FinalStep select(SelectExpr table)
    {
        this.select = table;
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
    public SQLStatement toSQL(SQLDialect dialect)
    {
        SQLBuilder builder = new SQLBuilder(dialect);
        toSQL(builder);
        return new SQLStatement(builder.toString(), builder.getParameters().toArray());
    }

    public void toSQL(SQLBuilder builder)
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
            for (LiteralExpr<?>[] row : values)
            {
                if(!first) builder.append(", ");
                builder.append('(');
                builder.appendAll(row, ", ");
                builder.append(") ");
            }
        }
    }

    private LiteralExpr<?>[] toLiterals(Object[] row)
    {
        LiteralExpr<?>[] result = new LiteralExpr<?>[row.length];
        for (int i = 0; i < row.length; i++)
        {
            Object object = row[i];
            if(object instanceof SQLWritable)
            {
                if(!(object instanceof Literal))
                {
                    throw new IllegalArgumentException("Only literals are allowed, value " + object.toString() + " is not a literal.");
                }
                result[i] = (Literal<?>)object;
            }
            else
            {
                result[i] = new Literal<>(object);
            }
        }
        return result;
    }
}
