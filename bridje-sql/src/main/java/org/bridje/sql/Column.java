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

import java.sql.JDBCType;
import java.util.Objects;
import org.bridje.sql.expr.BooleanExpr;
import org.bridje.sql.expr.Expression;
import org.bridje.sql.expr.OrderExpr;
import org.bridje.sql.expr.SortType;

public class Column<T> implements Expression<T>
{
    private final Table table;

    private final String name;

    private final boolean allowNull;

    private final JDBCType jdbcType;

    private final Class<T> javaType;
    
    private final int length;
    
    private final int presicion;

    private final boolean autoIncrement;

    private final T defValue;

    public Column(Table table, String name, boolean allowNull, JDBCType jdbcType, Class<T> javaType, int length, int presicion, boolean autoIncrement, T defValue)
    {
        this.table = table;
        this.name = name;
        this.allowNull = allowNull;
        this.jdbcType = jdbcType;
        this.javaType = javaType;
        this.length = length;
        this.presicion = presicion;
        this.autoIncrement = autoIncrement;
        this.defValue = defValue;
    }

    public Table getTable()
    {
        return table;
    }

    public String getName()
    {
        return name;
    }

    public boolean isAllowNull()
    {
        return allowNull;
    }

    public JDBCType getJdbcType()
    {
        return jdbcType;
    }

    public Class<T> getJavaType()
    {
        return javaType;
    }

    public int getLength()
    {
        return length;
    }

    public int getPresicion()
    {
        return presicion;
    }

    public T getDefValue()
    {
        return defValue;
    }

    public boolean isAutoIncrement()
    {
        return autoIncrement;
    }

    @Override
    public BooleanExpr<Boolean> eq(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, operand);
    }

    @Override
    public BooleanExpr<Boolean> ne(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.NE, operand);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.table);
        hash = 11 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Column<?> other = (Column<?>) obj;
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        if (!Objects.equals(this.table, other.table))
        {
            return false;
        }
        return true;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        if(!builder.isSimpleColumnNames())
        {
            table.writeSQL(builder);
            builder.append('.');
        }
        builder.appendObjectName(name);
    }

    @Override
    public OrderExpr asc()
    {
        return new OrderBy(SortType.ASC, this);
    }

    @Override
    public OrderExpr desc()
    {
        return new OrderBy(SortType.DESC, this);
    }
}
