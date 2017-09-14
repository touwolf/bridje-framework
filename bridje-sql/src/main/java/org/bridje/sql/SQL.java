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
import org.bridje.sql.flow.SelectStep;
import org.bridje.sql.expr.Expression;
import org.bridje.sql.expr.StringExpr;
import org.bridje.sql.flow.InsertIntoStep;

public class SQL
{
    public static final SelectStep select(Expression<?>... columns)
    {
        return new SelectBuilder(columns);
    }

    public static final InsertIntoStep insertInto(Table table)
    {
        return new InsertBuilder(table);
    }

    public static final UpdateBuilder update(Table table)
    {
        return new UpdateBuilder(table);
    }

    public static final ArithmeticExpr<Number> val(Number value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Byte> val(byte value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Byte> val(Byte value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Short> val(short value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Short> val(Short value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Integer> val(int value)
    {
        return new Literal(value);
    }
    
    public static final ArithmeticExpr<Integer> val(Integer value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Long> val(long value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Long> val(Long value)
    {
        return new Literal(value);
    }
    
    public static final ArithmeticExpr<Float> val(float value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Float> val(Float value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Double> val(double value)
    {
        return new Literal(value);
    }

    public static final ArithmeticExpr<Double> val(Double value)
    {
        return new Literal(value);
    }

    public static final StringExpr<String> val(String value)
    {
        return new Literal(value);
    }

    public static final BooleanExpr<Boolean> val(Boolean value)
    {
        return new Literal(value);
    }

    public static final BooleanExpr<Boolean> val(boolean value)
    {
        return new Literal(value);
    }

    public static final Expression<Character> val(char value)
    {
        return new Literal(value);
    }

    public static final Expression<Character> val(Character value)
    {
        return new Literal(value);
    }

    public static final <T> ArithmeticExpr<T> number(T value)
    {
        return new Literal(value);
    }

    public static final <T> BooleanExpr<T> bool(T value)
    {
        return new Literal(value);
    }

    public static final <T> StringExpr<T> str(T value)
    {
        return new Literal(value);
    }

    public static final <T> Expression<T> custom(T value)
    {
        return new Literal(value);
    }
}
