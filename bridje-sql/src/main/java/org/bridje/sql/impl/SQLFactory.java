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
import java.time.LocalDateTime;
import java.util.Date;
import org.bridje.sql.ArithmeticExpr;
import org.bridje.sql.ArrayExpr;
import org.bridje.sql.BooleanColumn;
import org.bridje.sql.BooleanExpr;
import org.bridje.sql.BuildForeignKeyStep;
import org.bridje.sql.BuildSchemaStep;
import org.bridje.sql.BuildTableStep;
import org.bridje.sql.Column;
import org.bridje.sql.DateColumn;
import org.bridje.sql.DateExpr;
import org.bridje.sql.DeleteStep;
import org.bridje.sql.Expression;
import org.bridje.sql.Index;
import org.bridje.sql.InsertIntoStep;
import org.bridje.sql.Limit;
import org.bridje.sql.NumberColumn;
import org.bridje.sql.SQLType;
import org.bridje.sql.SQLValueParser;
import org.bridje.sql.SQLValueWriter;
import org.bridje.sql.SelectStep;
import org.bridje.sql.StringColumn;
import org.bridje.sql.StringExpr;
import org.bridje.sql.Table;
import org.bridje.sql.UpdateStep;

public class SQLFactory
{
    private static ArithmeticExpr<Integer, Integer> COUNT_EXPR;

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

    public <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, int length, int precision, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return new SQLTypeImpl(javaType, javaReadType, jdbcType, length, precision, parser, writer);
    }

    public <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, int length, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return new SQLTypeImpl(javaType, javaReadType, jdbcType, length, 0, parser, writer);
    }

    public <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return new SQLTypeImpl(javaType, javaReadType, jdbcType, 0, 0, parser, writer);
    }

    public BuildSchemaStep buildSchema(String name)
    {
        return new SchemaBuilder(name);
    }
    
    public BuildTableStep buildTable(String name)
    {
        return new TableBuilder(name);
    }
    
    public <T, E> Column<T, E> buildColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return new ColumnImpl<>(name, type, allowNull, null);
    }

    public <T, E> Column<T, E> buildColumn(String name, Table table, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        ColumnImpl result = new ColumnImpl<>(name, type, allowNull, null);
        result.setTable(table);
        return result;
    }

    public <T, E> NumberColumn<T, E> buildAiColumn(String name, SQLType<T, E> type, boolean allowNull)
    {
        ColumnImpl result = new ColumnImpl<>(name, type, allowNull, null);
        result.setAutoIncrement(true);
        return result;
    }

    public <T, E> NumberColumn<T, E> buildNumberColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return new ColumnImpl<>(name, type, allowNull, defValue);
    }
    
    public <T, E> DateColumn<T, E> buildDateColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return new ColumnImpl<>(name, type, allowNull, defValue);
    }

    public <T, E> StringColumn<T, E> buildStringColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return new ColumnImpl<>(name, type, allowNull, defValue);
    }

    public <T, E> BooleanColumn<T, E> buildBooleanColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return new ColumnImpl<>(name, type, allowNull, defValue);
    }

    public Index buildIndex(String name, Table table, Column<?, ?>[] columns)
    {
        return new IndexImpl(name, table, columns, false, false);
    }

    public Index buildIndex(Table table, Column<?, ?>... columns)
    {
        return new IndexImpl(null, table, columns, false, false);
    }

    public Index removeIndex(Column<?, ?>... columns)
    {
        return new IndexImpl(null, null, columns, false, true);
    }

    public Index removeUnique(Column<?, ?>... columns)
    {
        return new IndexImpl(null, null, columns, true, true);
    }

    public Index buildUnique(String name, Table table, Column<?, ?>... columns)
    {
        return new IndexImpl(name, table, columns, true, false);
    }

    public Index buildUnique(Table table, Column<?, ?>... columns)
    {
        return new IndexImpl(null, table, columns, true, false);
    }

    public BuildForeignKeyStep buildForeignKey(String name, Table table, Column<?, ?>[] columns)
    {
        return new ForeignKeyBuilder(name, table, columns);
    }
    
    public BuildForeignKeyStep buildForeignKey(Table table, Column<?, ?>[] columns)
    {
        return new ForeignKeyBuilder(null, table, columns);
    }

    public Index buildIndex(String name, Column<?, ?>[] columns)
    {
        return new IndexImpl(name, null, columns, false, false);
    }

    public Index buildIndex(Column<?, ?>... columns)
    {
        return new IndexImpl(null, null, columns, false, false);
    }

    public Index buildUnique(String name, Column<?, ?>... columns)
    {
        return new IndexImpl(name, null, columns, true, false);
    }

    public Index buildUnique(Column<?, ?>... columns)
    {
        return new IndexImpl(null, null, columns, true, false);
    }

    public BuildForeignKeyStep buildForeignKey(String name, Column<?, ?>[] columns)
    {
        return new ForeignKeyBuilder(name, null, columns);
    }
    
    public BuildForeignKeyStep buildForeignKey(Column<?, ?>[] columns)
    {
        return new ForeignKeyBuilder(null, null, columns);
    }
    
    public SelectStep select(Expression<?, ?>... columns)
    {
        return new SelectBuilder(columns);
    }

    public InsertIntoStep insertInto(Table table)
    {
        return new InsertBuilder(table);
    }

    public UpdateStep update(Table table)
    {
        return new UpdateBuilder(table);
    }

    public DeleteStep delete(Table... tables)
    {
        return new DeleteBuilder(tables);
    }

    public ArithmeticExpr<Number, Number> val(Number value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Byte, Byte> val(byte value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Byte, Byte> val(Byte value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Short, Short> val(short value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Short, Short> val(Short value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Integer, Integer> val(int value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Integer, Integer> val(Integer value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Long, Long> val(long value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Long, Long> val(Long value)
    {
        return new LiteralImpl<>(value);
    }
    
    public ArithmeticExpr<Float, Float> val(float value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Float, Float> val(Float value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Double, Double> val(double value)
    {
        return new LiteralImpl<>(value);
    }

    public ArithmeticExpr<Double, Double> val(Double value)
    {
        return new LiteralImpl<>(value);
    }

    public StringExpr<String, String> val(String value)
    {
        return new LiteralImpl<>(value);
    }

    public BooleanExpr<Boolean, Boolean> val(Boolean value)
    {
        return new LiteralImpl<>(value);
    }

    public BooleanExpr<Boolean, Boolean> val(boolean value)
    {
        return new LiteralImpl<>(value);
    }

    public Expression<Character, Character> val(char value)
    {
        return new LiteralImpl<>(value);
    }

    public Expression<Character, Character> val(Character value)
    {
        return new LiteralImpl<>(value);
    }

    public <T, E> ArithmeticExpr<T, E> number(T value)
    {
        return new LiteralImpl<>(value);
    }

    public <T, E> BooleanExpr<T, E> bool(T value)
    {
        return new LiteralImpl<>(value);
    }

    public <T, E> StringExpr<T, E> str(T value)
    {
        return new LiteralImpl<>(value);
    }

    public <T, E> DateExpr<T, E> date(T value)
    {
        return new LiteralImpl<>(value);
    }

    public <T, E> Expression<T, E> custom(T value)
    {
        return new LiteralImpl<>(value);
    }

    public <T, E> Expression<T, E> custom(T value, SQLType<T, E> type)
    {
        return new LiteralImpl<>(value, type);
    }

    public <T, E> Expression<T, E> param(SQLType<T, E> cls)
    {
        return new Param<>(cls);
    }

    public ArithmeticExpr<Integer, Integer> count()
    {
        if(COUNT_EXPR == null)
        {
            COUNT_EXPR = new SimpleExpressionImpl("count(*)", SQLType.INTEGER);
        }
        return COUNT_EXPR;
    }

    public Limit limit(int offset, int count)
    {
        return new LimitImpl(offset, count);
    }

    public Limit limit(int count)
    {
        return new LimitImpl(count);
    }

    public <T, E> ArrayExpr<T, E> array(Expression<T, E>... elements)
    {
        if(elements.length == 0) return new ArrayExprImpl(elements, null);
        return new ArrayExprImpl(elements, elements[0].getSQLType());
    }

    public <T, E> ArrayExpr<T, E> array(SQLType<T, E> type, T... elements)
    {
        Expression[] result = new Expression[elements.length];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = custom(elements[i], type);
        }
        return array(result);
    }

    public DateExpr<LocalDateTime, Date> now()
    {
        return new FunctionImpl("NOW", SQLType.DATETIME);
    }
}
