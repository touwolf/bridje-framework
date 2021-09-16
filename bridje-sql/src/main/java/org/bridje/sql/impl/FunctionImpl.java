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

import java.util.Arrays;
import java.util.Objects;
import org.bridje.sql.*;

class FunctionImpl<T, E> extends ExpressionBase<T, E> implements BooleanExpr<T, E>, StringExpr<T, E>, ArithmeticExpr<T, E>, DateExpr<T, E>
{
    private final String name;

    private final Expression<?, ?>[] params;

    public FunctionImpl(String name, SQLType<T, E> type, Expression<?, ?>... params)
    {
        super(type);
        this.name = name;
        this.params = params;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.append(name);
        builder.append('(');
        builder.appendAll(params, ", ");
        builder.append(')');
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.name);
        hash = 61 * hash + Arrays.deepHashCode(this.params);
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
        final FunctionImpl<?, ?> other = (FunctionImpl<?, ?>) obj;
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        return Arrays.deepEquals(this.params, other.params);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> gt(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.GT, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> ge(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.GE, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> lt(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.LT, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> le(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.LE, operand, SQLType.BOOLEAN);
    }


    @Override
    public BooleanExpr<Boolean, Boolean> gt(DateExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.GT, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> ge(DateExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.GE, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> lt(DateExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.LT, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> le(DateExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.LE, operand, SQLType.BOOLEAN);
    }
}
