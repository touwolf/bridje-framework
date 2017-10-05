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

import org.bridje.sql.ArithmeticExpr;
import org.bridje.sql.BooleanExpr;
import org.bridje.sql.Expression;
import org.bridje.sql.OrderExpr;
import org.bridje.sql.SQLType;
import org.bridje.sql.SortType;
import org.bridje.sql.StringExpr;

abstract class ExpressionBase<T> implements BooleanExpr<T>, StringExpr<T>, ArithmeticExpr<T>
{
    private final SQLType<T> sqlType;

    public ExpressionBase(SQLType<T> sqlType)
    {
        this.sqlType = sqlType;
    }
    
    @Override
    public SQLType<T> getSQLType()
    {
        return sqlType;
    }

    @Override
    public BooleanExpr<T> and(T operand)
    {
        return new BinaryExpr<>(this, Operators.AND, new Literal<>(operand), getSQLType());
    }

    @Override
    public BooleanExpr<T> or(T operand)
    {
        return new BinaryExpr<>(this, Operators.OR, new Literal<>(operand), getSQLType());
    }

    @Override
    public BooleanExpr<T> and(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.AND, operand, getSQLType());
    }

    @Override
    public BooleanExpr<T> or(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.OR, operand, getSQLType());
    }

    @Override
    public BooleanExpr<T> not()
    {
        return new UnaryExpr<>(Operators.NOT, this, getSQLType());
    }

    @Override
    public BooleanExpr<Boolean> eq(T operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, new Literal<>(operand), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean> ne(T operand)
    {
        return new BinaryExpr<>(this, Operators.NE, new Literal<>(operand), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean> eq(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean> ne(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.NE, operand, SQLType.BOOLEAN);
    }

    @Override
    public StringExpr<T> trim()
    {
        return new FunctionImpl("trim", getSQLType(), this);
    }

    @Override
    public ArithmeticExpr<Integer> length()
    {
        return new FunctionImpl("length", SQLType.INTEGER, this);
    }

    @Override
    public ArithmeticExpr<T> plus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T> minus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T> mul(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T> div(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T> mod(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T> plus(T operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, new Literal<>(operand), getSQLType());
    }

    @Override
    public ArithmeticExpr<T> minus(T operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, new Literal<>(operand), getSQLType());
    }

    @Override
    public ArithmeticExpr<T> mul(T operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, new Literal<>(operand), getSQLType());
    }

    @Override
    public ArithmeticExpr<T> div(T operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, new Literal<>(operand), getSQLType());
    }

    @Override
    public ArithmeticExpr<T> mod(T operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, new Literal<>(operand), getSQLType());
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
