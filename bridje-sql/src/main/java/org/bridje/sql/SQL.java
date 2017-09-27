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

public class SQL
{
    public static final <T> SQLType<T> buildType(Class<T> javaType, JDBCType jdbcType, int length, int precision)
    {
        return null;
    }
    
    public static final <T> SQLType<T> buildType(Class<T> javaType, JDBCType jdbcType, int length)
    {
        return null;
    }

    public static final <T> SQLType<T> buildType(Class<T> javaType, JDBCType jdbcType)
    {
        return null;
    }

    public static final BuildTableStep buildTable(String name)
    {
        return null;
    }

    public static final SelectStep select(Expression<?>... columns)
    {
        return null;
    }

    public static final InsertIntoStep insertInto(Table table)
    {
        return null;
    }

    public static final UpdateStep update(Table table)
    {
        return null;
    }

    public static final DeleteStep delete(Table table)
    {
        return null;
    }

    public static final CreateTableStep createTable(Table table)
    {
        return null;
    }

    public static final AlterTableStep alterTable(Table table)
    {
        return null;
    }

    public static final CreateIndexStep createIndex(String name, Table on)
    {
        return null;
    }

    public static final CreateIndexStep createUniqueIndex(String name, Table on)
    {
        return null;
    }

    public static final FinalStep dropIndex(String name, Table on)
    {
        return null;
    }
    
    public static final ArithmeticExpr<Number> val(Number value)
    {
        return null;
    }

    public static final ArithmeticExpr<Byte> val(byte value)
    {
        return null;
    }

    public static final ArithmeticExpr<Byte> val(Byte value)
    {
        return null;
    }

    public static final ArithmeticExpr<Short> val(short value)
    {
        return null;
    }

    public static final ArithmeticExpr<Short> val(Short value)
    {
        return null;
    }

    public static final ArithmeticExpr<Integer> val(int value)
    {
        return null;
    }

    public static final ArithmeticExpr<Integer> val(Integer value)
    {
        return null;
    }

    public static final ArithmeticExpr<Long> val(long value)
    {
        return null;
    }

    public static final ArithmeticExpr<Long> val(Long value)
    {
        return null;
    }
    
    public static final ArithmeticExpr<Float> val(float value)
    {
        return null;
    }

    public static final ArithmeticExpr<Float> val(Float value)
    {
        return null;
    }

    public static final ArithmeticExpr<Double> val(double value)
    {
        return null;
    }

    public static final ArithmeticExpr<Double> val(Double value)
    {
        return null;
    }

    public static final StringExpr<String> val(String value)
    {
        return null;
    }

    public static final BooleanExpr<Boolean> val(Boolean value)
    {
        return null;
    }

    public static final BooleanExpr<Boolean> val(boolean value)
    {
        return null;
    }

    public static final Expression<Character> val(char value)
    {
        return null;
    }

    public static final Expression<Character> val(Character value)
    {
        return null;
    }

    public static final <T> ArithmeticExpr<T> number(T value)
    {
        return null;
    }

    public static final <T> BooleanExpr<T> bool(T value)
    {
        return null;
    }

    public static final <T> StringExpr<T> str(T value)
    {
        return null;
    }

    public static final <T> Expression<T> custom(T value)
    {
        return null;
    }

    public static final <T> Expression<T> param()
    {
        return null;
    }
}
