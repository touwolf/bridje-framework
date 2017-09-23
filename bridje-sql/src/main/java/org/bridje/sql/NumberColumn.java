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

import org.bridje.sql.expr.ArithmeticExpr;
import org.bridje.sql.expr.SQLType;

public class NumberColumn<T> extends Column<T> implements ArithmeticExpr<T>
{
    NumberColumn(Table table, String name, SQLType<T> type, boolean allowNull, boolean autoIncrement, T defValue)
    {
        super(table, name, type, allowNull, autoIncrement, defValue);
    }

    @Override
    public ArithmeticExpr<T> plus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, operand, getType());
    }

    @Override
    public ArithmeticExpr<T> minus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, operand, getType());
    }

    @Override
    public ArithmeticExpr<T> mul(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, operand, getType());
    }

    @Override
    public ArithmeticExpr<T> div(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, operand, getType());
    }

    @Override
    public ArithmeticExpr<T> mod(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, operand, getType());
    }

    @Override
    public ArithmeticExpr<T> plus(T operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, new Literal<>(operand), getType());
    }

    @Override
    public ArithmeticExpr<T> minus(T operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, new Literal<>(operand), getType());
    }

    @Override
    public ArithmeticExpr<T> mul(T operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, new Literal<>(operand), getType());
    }

    @Override
    public ArithmeticExpr<T> div(T operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, new Literal<>(operand), getType());
    }

    @Override
    public ArithmeticExpr<T> mod(T operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, new Literal<>(operand), getType());
    }
}
