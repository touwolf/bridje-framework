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

import java.sql.JDBCType;
import org.bridje.sql.ArithmeticExpr;
import org.bridje.sql.BooleanExpr;
import org.bridje.sql.BuildTableStep;
import org.bridje.sql.DeleteStep;
import org.bridje.sql.Expression;
import org.bridje.sql.InsertIntoStep;
import org.bridje.sql.SQLType;
import org.bridje.sql.SelectStep;
import org.bridje.sql.StringExpr;
import org.bridje.sql.Table;
import org.bridje.sql.UpdateStep;

public class SQLFactory
{
    private static SQLFactory INST;

    public static SQLFactory getInstance()
    {
        if(INST == null)
        {
            INST = new SQLFactory();
        }
        return INST;
    }

    private SQLFactory()
    {
    }

    public <T> SQLType<T> buildType(Class<T> javaType, JDBCType jdbcType, int length, int precision)
    {
        return null;
    }
    
    public <T> SQLType<T> buildType(Class<T> javaType, JDBCType jdbcType, int length)
    {
        return null;
    }

    public <T> SQLType<T> buildType(Class<T> javaType, JDBCType jdbcType)
    {
        return null;
    }

    public BuildTableStep buildTable(String name)
    {
        return null;
    }

    public SelectStep select(Expression<?>... columns)
    {
        return null;
    }

    public InsertIntoStep insertInto(Table table)
    {
        return null;
    }

    public UpdateStep update(Table table)
    {
        return null;
    }

    public DeleteStep delete(Table table)
    {
        return null;
    }
    
    public ArithmeticExpr<Number> val(Number value)
    {
        return null;
    }

    public ArithmeticExpr<Byte> val(byte value)
    {
        return null;
    }

    public ArithmeticExpr<Byte> val(Byte value)
    {
        return null;
    }

    public ArithmeticExpr<Short> val(short value)
    {
        return null;
    }

    public ArithmeticExpr<Short> val(Short value)
    {
        return null;
    }

    public ArithmeticExpr<Integer> val(int value)
    {
        return null;
    }

    public ArithmeticExpr<Integer> val(Integer value)
    {
        return null;
    }

    public ArithmeticExpr<Long> val(long value)
    {
        return null;
    }

    public ArithmeticExpr<Long> val(Long value)
    {
        return null;
    }
    
    public ArithmeticExpr<Float> val(float value)
    {
        return null;
    }

    public ArithmeticExpr<Float> val(Float value)
    {
        return null;
    }

    public ArithmeticExpr<Double> val(double value)
    {
        return null;
    }

    public ArithmeticExpr<Double> val(Double value)
    {
        return null;
    }

    public StringExpr<String> val(String value)
    {
        return null;
    }

    public BooleanExpr<Boolean> val(Boolean value)
    {
        return null;
    }

    public BooleanExpr<Boolean> val(boolean value)
    {
        return null;
    }

    public Expression<Character> val(char value)
    {
        return null;
    }

    public Expression<Character> val(Character value)
    {
        return null;
    }

    public <T> ArithmeticExpr<T> number(T value)
    {
        return null;
    }

    public <T> BooleanExpr<T> bool(T value)
    {
        return null;
    }

    public <T> StringExpr<T> str(T value)
    {
        return null;
    }

    public <T> Expression<T> custom(T value)
    {
        return null;
    }

    public final <T> Expression<T> param(Class<T> cls)
    {
        return null;
    }
}
