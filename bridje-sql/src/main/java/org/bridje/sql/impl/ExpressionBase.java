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
import org.bridje.sql.ArrayExpr;
import org.bridje.sql.BooleanExpr;
import org.bridje.sql.DateExpr;
import org.bridje.sql.Expression;
import org.bridje.sql.OrderExpr;
import org.bridje.sql.SQLType;
import org.bridje.sql.SortType;
import org.bridje.sql.StringExpr;

abstract class ExpressionBase<T, E> implements BooleanExpr<T, E>, StringExpr<T, E>, ArithmeticExpr<T, E>, DateExpr<T, E>
{
    private final SQLType<T, E> sqlType;

    public ExpressionBase(SQLType<T, E> sqlType)
    {
        this.sqlType = sqlType;
    }

    @Override
    public SQLType<T, E> getSQLType()
    {
        return sqlType;
    }

    @Override
    public BooleanExpr<T, E> and(T operand)
    {
        return new BinaryExpr<>(this, Operators.AND, new LiteralImpl<>(operand, getSQLType()), getSQLType());
    }

    @Override
    public BooleanExpr<T, E> or(T operand)
    {
        return new BinaryExpr<>(this, Operators.OR, new LiteralImpl<>(operand, getSQLType()), getSQLType());
    }

    @Override
    public BooleanExpr<T, E> and(BooleanExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.AND, operand, getSQLType());
    }

    @Override
    public BooleanExpr<T, E> or(BooleanExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.OR, operand, getSQLType());
    }

    @Override
    public BooleanExpr<T, E> not()
    {
        return new UnaryExpr<>(Operators.NOT, this, getSQLType());
    }

    @Override
    public BooleanExpr<Boolean, Boolean> eq(T operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, new LiteralImpl<>(operand, getSQLType()), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> ne(T operand)
    {
        return new BinaryExpr<>(this, Operators.NE, new LiteralImpl<>(operand, getSQLType()), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> eq(Expression<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> ne(Expression<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.NE, operand, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> isNull()
    {
        return new IsNullExpr(this, SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> in(ArrayExpr<T, E> array)
    {
        return new BinaryExpr<>(this, Operators.IN, array, SQLType.BOOLEAN);
    }

    @Override
    public StringExpr<T, E> trim()
    {
        return new FunctionImpl("trim", getSQLType(), this);
    }

    @Override
    public ArithmeticExpr<Integer, Integer> length()
    {
        return new FunctionImpl("LENGTH", SQLType.INTEGER, this);
    }

    @Override
    public ArithmeticExpr<Integer, Integer> year()
    {
        return new FunctionImpl("YEAR", SQLType.INTEGER, this);
    }

    @Override
    public ArithmeticExpr<Integer, Integer> month()
    {
        return new FunctionImpl("MONTH", SQLType.INTEGER, this);
    }

    @Override
    public ArithmeticExpr<Integer, Integer> dayOfMonth()
    {
        return new FunctionImpl("DAYOFMONTH", SQLType.INTEGER, this);
    }

    @Override
    public ArithmeticExpr<Integer, Integer> dayOfWeek()
    {
        return new FunctionImpl("DAYOFWEEK", SQLType.INTEGER, this);
    }

    @Override
    public ArithmeticExpr<Integer, Integer> dayOfYear()
    {
        return new FunctionImpl("DAYOFYEAR", SQLType.INTEGER, this);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> like(String operand)
    {
        return new BinaryExpr<>(this, Operators.LIKE, new LiteralImpl<>(operand, SQLType.STRING), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> like(StringExpr<?, ?> operand)
    {
        return new BinaryExpr<>(this, Operators.LIKE, operand, SQLType.BOOLEAN);
    }

    @Override
    public ArithmeticExpr<T, E> plus(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> minus(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> mul(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> div(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> mod(ArithmeticExpr<T, E> operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, operand, getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> plus(T operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, new LiteralImpl<>(operand, getSQLType()), getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> minus(T operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, new LiteralImpl<>(operand, getSQLType()), getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> mul(T operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, new LiteralImpl<>(operand, getSQLType()), getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> div(T operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, new LiteralImpl<>(operand, getSQLType()), getSQLType());
    }

    @Override
    public ArithmeticExpr<T, E> mod(T operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, new LiteralImpl<>(operand, getSQLType()), getSQLType());
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

    @Override
    public BooleanExpr<Boolean, Boolean> gt(T operand)
    {
        return new BinaryExpr<>(this, Operators.GT, new LiteralImpl<>(operand, getSQLType()), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> ge(T operand)
    {
        return new BinaryExpr<>(this, Operators.GE, new LiteralImpl<>(operand, getSQLType()), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> lt(T operand)
    {
        return new BinaryExpr<>(this, Operators.LT, new LiteralImpl<>(operand, getSQLType()), SQLType.BOOLEAN);
    }

    @Override
    public BooleanExpr<Boolean, Boolean> le(T operand)
    {
        return new BinaryExpr<>(this, Operators.LE, new LiteralImpl<>(operand, getSQLType()), SQLType.BOOLEAN);
    }

    @Override
    public ArithmeticExpr<Long, Long> sum()
    {
         return new FunctionImpl("SUM", SQLType.LONG, this);
    }

    @Override
    public ArithmeticExpr<Long, Long> avg()
    {
        return new FunctionImpl("AVG", SQLType.LONG, this);
    }

    @Override
    public ArithmeticExpr<Long, Long> count()
    {
        return new FunctionImpl("COUNT", SQLType.LONG, this);
    }

    @Override
    public ArithmeticExpr<T, E> min()
    {
        return new FunctionImpl("MIN", getSQLType(), this);
    }

    @Override
    public ArithmeticExpr<T, E> max()
    {
        return new FunctionImpl("MAX", getSQLType(), this);
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
