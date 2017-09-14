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
import org.bridje.sql.expr.BooleanExpr;
import org.bridje.sql.expr.Expression;
import org.bridje.sql.expr.OrderExpr;
import org.bridje.sql.expr.SortType;
import org.bridje.sql.expr.StringExpr;

abstract class ExpressionBase<T> implements BooleanExpr<T>, StringExpr<T>, ArithmeticExpr<T>
{

    @Override
    public BooleanExpr<T> and(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.AND, operand);
    }

    @Override
    public BooleanExpr<T> or(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.OR, operand);
    }

    @Override
    public BooleanExpr<T> not()
    {
        return new UnaryExpr<>(Operators.NOT, this);
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
    public StringExpr<T> trim()
    {
        return new FunctionExpr("trim", this);
    }

    @Override
    public ArithmeticExpr<Integer> length()
    {
        return new FunctionExpr("length", this);
    }

    @Override
    public ArithmeticExpr<T> plus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, operand);
    }

    @Override
    public ArithmeticExpr<T> minus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, operand);
    }

    @Override
    public ArithmeticExpr<T> mul(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, operand);
    }

    @Override
    public ArithmeticExpr<T> div(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, operand);
    }

    @Override
    public ArithmeticExpr<T> mod(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, operand);
    }

    @Override
    public ArithmeticExpr<T> plus(T operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, new Literal<>(operand));
    }

    @Override
    public ArithmeticExpr<T> minus(T operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, new Literal<>(operand));
    }

    @Override
    public ArithmeticExpr<T> mul(T operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, new Literal<>(operand));
    }

    @Override
    public ArithmeticExpr<T> div(T operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, new Literal<>(operand));
    }

    @Override
    public ArithmeticExpr<T> mod(T operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, new Literal<>(operand));
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
