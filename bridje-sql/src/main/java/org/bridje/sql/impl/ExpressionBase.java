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
import org.bridje.sql.SortType;
import org.bridje.sql.StringExpr;

abstract class ExpressionBase<T> implements BooleanExpr<T>, StringExpr<T>, ArithmeticExpr<T>
{
    private final Class<T> type;

    public ExpressionBase(Class<T> type)
    {
        this.type = type;
    }
    
    @Override
    public Class<T> getType()
    {
        return type;
    }

    @Override
    public BooleanExpr<T> and(T operand)
    {
        return new BinaryExpr<>(this, Operators.AND, new Literal<>(operand), (Class<T>)operand.getClass());
    }

    @Override
    public BooleanExpr<T> or(T operand)
    {
        return new BinaryExpr<>(this, Operators.OR, new Literal<>(operand), (Class<T>)operand.getClass());
    }

    @Override
    public BooleanExpr<T> and(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.AND, operand, getType());
    }

    @Override
    public BooleanExpr<T> or(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.OR, operand, getType());
    }

    @Override
    public BooleanExpr<T> not()
    {
        return new UnaryExpr<>(Operators.NOT, this, getType());
    }

    @Override
    public BooleanExpr<Boolean> eq(T operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, new Literal<>(operand), Boolean.class);
    }

    @Override
    public BooleanExpr<Boolean> ne(T operand)
    {
        return new BinaryExpr<>(this, Operators.NE, new Literal<>(operand), Boolean.class);
    }

    @Override
    public BooleanExpr<Boolean> eq(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, operand, Boolean.class);
    }

    @Override
    public BooleanExpr<Boolean> ne(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.NE, operand, Boolean.class);
    }

    @Override
    public StringExpr<T> trim()
    {
        return new FunctionExpr("trim", getType(), this);
    }

    @Override
    public ArithmeticExpr<Integer> length()
    {
        return new FunctionExpr("length", Integer.class, this);
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
