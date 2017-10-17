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
import org.bridje.sql.impl.SQLFactory;

/**
 * 
 */
public class SQL
{
    private static final SQLFactory FACT = SQLFactory.getInstance();

    /**
     * 
     * @param <T>
     * @param javaType
     * @param jdbcType
     * @param length
     * @param precision
     * @return 
     */
    public static final <T> SQLType<T, T> buildType(Class<T> javaType, JDBCType jdbcType, int length, int precision)
    {
        return FACT.buildType(javaType, javaType, jdbcType, length, precision, null, null);
    }

    /**
     * 
     * @param <T>
     * @param javaType
     * @param jdbcType
     * @param length
     * @return 
     */
    public static final <T> SQLType<T, T> buildType(Class<T> javaType, JDBCType jdbcType, int length)
    {
        return FACT.buildType(javaType, javaType, jdbcType, length, null, null);
    }

    /**
     * 
     * @param <T>
     * @param javaType
     * @param jdbcType
     * @return 
     */
    public static final <T> SQLType<T, T> buildType(Class<T> javaType, JDBCType jdbcType)
    {
        return FACT.buildType(javaType, javaType, jdbcType, null, null);
    }

    /**
     * 
     * @param <T>
     * @param <E>
     * @param javaType
     * @param javaReadType
     * @param jdbcType
     * @param length
     * @param precision
     * @param parser
     * @param writer
     * @return 
     */
    public static final <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, int length, int precision, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return FACT.buildType(javaType, javaReadType, jdbcType, length, precision, parser, writer);
    }

    /**
     * 
     * @param <T>
     * @param <E>
     * @param javaType
     * @param javaReadType
     * @param jdbcType
     * @param length
     * @param parser
     * @param writer
     * @return 
     */
    public static final <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, int length, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return FACT.buildType(javaType, javaReadType, jdbcType, length, parser, writer);
    }

    /**
     * 
     * @param <T>
     * @param <E>
     * @param javaType
     * @param javaReadType
     * @param jdbcType
     * @param parser
     * @param writer
     * @return 
     */
    public static final <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return FACT.buildType(javaType, javaReadType, jdbcType, parser, writer);
    }

    /**
     * 
     * @param name
     * @return 
     */
    public static final BuildSchemaStep buildSchema(String name)
    {
        return FACT.buildSchema(name);
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public static final BuildTableStep buildTable(String name)
    {
        return FACT.buildTable(name);
    }

    /**
     * 
     * @param <T> The final java type of the expression.
     * @param <E> The result set read java type of the expression.
     * @param name
     * @param type
     * @param allowNull
     * @param defValue
     * @return 
     */
    public static final <T, E> Column<T, E> buildColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildColumn(name, type, allowNull, defValue);
    }
    
    /**
     * 
     * @param <T> The final java type of the expression.
     * @param <E> The result set read java type of the expression.
     * @param name
     * @param type
     * @param allowNull
     * @return 
     */
    public static final <T, E> NumberColumn<T, E> buildAiColumn(String name, SQLType<T, E> type, boolean allowNull)
    {
        return FACT.buildAiColumn(name, type, allowNull);
    }

    /**
     * 
     * @param <T> The final java type of the expression.
     * @param <E> The result set read java type of the expression.
     * @param name
     * @param type
     * @param allowNull
     * @param defValue
     * @return 
     */
    public static final <T, E> NumberColumn<T, E> buildNumberColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildNumberColumn(name, type, allowNull, defValue);
    }

    /**
     * 
     * @param <T> The final java type of the expression.
     * @param <E> The result set read java type of the expression.
     * @param name
     * @param type
     * @param allowNull
     * @param defValue
     * @return 
     */
    public static final <T, E> DateColumn<T, E> buildDateColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildDateColumn(name, type, allowNull, defValue);
    }

    /**
     * 
     * @param <T> The final java type of the expression.
     * @param <E> The result set read java type of the expression.
     * @param name
     * @param type
     * @param allowNull
     * @param defValue
     * @return 
     */
    public static final <T, E> StringColumn<T, E> buildStringColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildStringColumn(name, type, allowNull, defValue);
    }

    /**
     * 
     * @param <T> The final java type of the expression.
     * @param <E> The result set read java type of the expression.
     * @param name
     * @param type
     * @param allowNull
     * @param defValue
     * @return 
     */
    public static final <T, E> BooleanColumn<T, E> buildBoolColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildBoolColumn(name, type, allowNull, defValue);
    }

    /**
     * 
     * @param name
     * @param table
     * @param columns
     * @return 
     */
    public static final Index buildIndex(String name, Table table, Column<?, ?>... columns)
    {
        return FACT.buildIndex(name, table, columns);
    }

    /**
     * 
     * @param table
     * @param columns
     * @return 
     */
    public static final Index buildIndex(Table table, Column<?, ?>... columns)
    {
        return FACT.buildIndex(table, columns);
    }

    /**
     * 
     * @param name
     * @param table
     * @param columns
     * @return 
     */
    public static final Index buildUnique(String name, Table table, Column<?, ?>... columns)
    {
        return FACT.buildUnique(name, table, columns);
    }

    /**
     * 
     * @param table
     * @param columns
     * @return 
     */
    public static final Index buildUnique(Table table, Column<?, ?>... columns)
    {
        return FACT.buildUnique(table, columns);
    }

    /**
     * 
     * @param name
     * @param table
     * @param columns
     * @return 
     */
    public static final BuildForeignKeyStep buildForeignKey(String name, Table table, Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(name, table, columns);
    }

    /**
     * 
     * @param table
     * @param columns
     * @return 
     */
    public static final BuildForeignKeyStep buildForeignKey(Table table, Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(table, columns);
    }
    
    /**
     * 
     * @param name
     * @param columns
     * @return 
     */
    public static final Index buildIndex(String name, Column<?, ?>... columns)
    {
        return FACT.buildIndex(name, columns);
    }

    /**
     * 
     * @param columns
     * @return 
     */
    public static final Index buildIndex(Column<?, ?>... columns)
    {
        return FACT.buildIndex(columns);
    }

    /**
     * 
     * @param name
     * @param columns
     * @return 
     */
    public static final Index buildUnique(String name, Column<?, ?>... columns)
    {
        return FACT.buildUnique(name, columns);
    }

    /**
     * 
     * @param columns
     * @return 
     */
    public static final Index buildUnique(Column<?, ?>... columns)
    {
        return FACT.buildUnique(columns);
    }

    /**
     * 
     * @param name
     * @param columns
     * @return 
     */
    public static final BuildForeignKeyStep buildForeignKey(String name, Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(name, columns);
    }

    /**
     * 
     * @param columns
     * @return 
     */
    public static final BuildForeignKeyStep buildForeignKey(Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(columns);
    }

    /**
     * 
     * @param columns
     * @return 
     */
    public static final SelectStep select(Expression<?, ?>... columns)
    {
        return FACT.select(columns);
    }

    /**
     * 
     * @param table
     * @return 
     */
    public static final InsertIntoStep insertInto(Table table)
    {
        return FACT.insertInto(table);
    }

    /**
     * 
     * @param table
     * @return 
     */
    public static final UpdateStep update(Table table)
    {
        return FACT.update(table);
    }

    /**
     * 
     * @param tables
     * @return 
     */
    public static final DeleteStep delete(Table... tables)
    {
        return FACT.delete(tables);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Number, Number> val(Number value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Byte, Byte> val(byte value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Byte, Byte> val(Byte value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Short, Short> val(short value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Short, Short> val(Short value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Integer, Integer> val(int value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Integer, Integer> val(Integer value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Long, Long> val(long value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Long, Long> val(Long value)
    {
        return FACT.val(value);
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Float, Float> val(float value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Float, Float> val(Float value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Double, Double> val(double value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final ArithmeticExpr<Double, Double> val(Double value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final StringExpr<String, String> val(String value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final BooleanExpr<Boolean, Boolean> val(Boolean value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final BooleanExpr<Boolean, Boolean> val(boolean value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final Expression<Character, Character> val(char value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static final Expression<Character, Character> val(Character value)
    {
        return FACT.val(value);
    }

    /**
     * 
     * @param <T>
     * @param <E>
     * @param value
     * @return 
     */
    public static final <T, E> ArithmeticExpr<T, E> number(T value)
    {
        return FACT.number(value);
    }

    /**
     * 
     * @param <T>
     * @param value
     * @return 
     */
    public static final <T> BooleanExpr<T, T> bool(T value)
    {
        return FACT.bool(value);
    }

    /**
     * 
     * @param <T>
     * @param value
     * @return 
     */
    public static final <T> StringExpr<T, T> str(T value)
    {
        return FACT.str(value);
    }

    /**
     * 
     * @param <T>
     * @param value
     * @return 
     */
    public static final <T> Expression<T, T> custom(T value)
    {
        return FACT.custom(value);
    }

    /**
     * 
     * @param <T>
     * @param <E>
     * @param type
     * @return 
     */
    public static final <T, E> Expression<T, E> param(SQLType<T, E> type)
    {
        return FACT.param(type);
    }

    /**
     * 
     * @return 
     */
    public static final ArithmeticExpr<Integer, Integer> count()
    {
        return FACT.count();
    }

    /**
     * 
     * @param offset
     * @param count
     * @return 
     */
    public static final Limit limit(int offset, int count)
    {
        return FACT.limit(offset, count);
    }
}
