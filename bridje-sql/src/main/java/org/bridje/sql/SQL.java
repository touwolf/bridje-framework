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
import java.time.LocalDateTime;
import java.util.Date;
import org.bridje.sql.impl.SQLFactory;

/**
 * This class provides an starting point to create SQL objects and queries.
 */
public class SQL
{
    private static final SQLFactory FACT = SQLFactory.getInstance();

    /**
     * Builds a new SQL type.
     *
     * @param <T>       The java type for the new SQL type.
     * @param javaType  The java class for the new SQL type.
     * @param jdbcType  The JDBC type for the new SQL type.
     * @param length    The length for strings and numeric types.
     * @param precision The precision for numeric types.
     *
     * @return The new create SQL type.
     */
    public static final <T> SQLType<T, T> buildType(Class<T> javaType, JDBCType jdbcType, int length, int precision)
    {
        return FACT.buildType(javaType, javaType, jdbcType, length, precision, null, null);
    }

    /**
     * Builds a new SQL type.
     *
     * @param <T>      The java type for the new SQL type.
     * @param javaType The java class for the new SQL type.
     * @param jdbcType The JDBC type for the new SQL type.
     * @param length   The length for strings and numeric types.
     *
     * @return The new create SQL type.
     */
    public static final <T> SQLType<T, T> buildType(Class<T> javaType, JDBCType jdbcType, int length)
    {
        return FACT.buildType(javaType, javaType, jdbcType, length, null, null);
    }

    /**
     * Builds a new SQL type.
     *
     * @param <T>      The java type for the new SQL type.
     * @param javaType The java class for the new SQL type.
     * @param jdbcType The JDBC type for the new SQL type.
     *
     * @return The new create SQL type.
     */
    public static final <T> SQLType<T, T> buildType(Class<T> javaType, JDBCType jdbcType)
    {
        return FACT.buildType(javaType, javaType, jdbcType, null, null);
    }

    /**
     * Builds a new SQL type.
     *
     * @param <T>          The final java type of the expression.
     * @param <E>          The ResultSet read java type of the expression.
     * @param javaType     The java class for the new SQL type.
     * @param javaReadType The read java class for the new SQL type.
     * @param jdbcType     The JDBC type for the new SQL type.
     * @param length       The length for strings and numeric types.
     * @param precision    The precision for numeric types.
     * @param parser       The object that will parse the values from the
     *                     database for the new SQL type.
     * @param writer       The object that will write the values to the database
     *                     for the new SQL type.
     *
     * @return The new create SQL type.
     */
    public static final <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, int length, int precision, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return FACT.buildType(javaType, javaReadType, jdbcType, length, precision, parser, writer);
    }

    /**
     * Builds a new SQL type.
     *
     * @param <T>          The final java type of the expression.
     * @param <E>          The ResultSet read java type of the expression.
     * @param javaType     The java class for the new SQL type.
     * @param javaReadType The read java class for the new SQL type.
     * @param jdbcType     The JDBC type for the new SQL type.
     * @param length       The length for strings and numeric types.
     * @param parser       The object that will parse the values from the
     *                     database for the new SQL type.
     * @param writer       The object that will write the values to the database
     *                     for the new SQL type.
     *
     * @return The new create SQL type.
     */
    public static final <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, int length, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return FACT.buildType(javaType, javaReadType, jdbcType, length, parser, writer);
    }

    /**
     * Builds a new SQL type.
     *
     * @param <T>          The final java type of the expression.
     * @param <E>          The ResultSet read java type of the expression.
     * @param javaType     The java class for the new SQL type.
     * @param javaReadType The read java class for the new SQL type.
     * @param jdbcType     The JDBC type for the new SQL type.
     * @param parser       The object that will parse the values from the
     *                     database for the new SQL type.
     * @param writer       The object that will write the values to the database
     *                     for the new SQL type.
     *
     * @return The new create SQL type.
     */
    public static final <T, E> SQLType<T, E> buildType(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        return FACT.buildType(javaType, javaReadType, jdbcType, parser, writer);
    }

    /**
     * Creates a new database schema builder, that can be use to collect all the
     * data for the new schema.
     *
     * @param name The name of the schema.
     *
     * @return A builder that can collect the data for the schema.
     */
    public static final BuildSchemaStep buildSchema(String name)
    {
        return FACT.buildSchema(name);
    }

    /**
     * Creates a new SQL table builder, that can be use to collect all the data
     * for the new table.
     *
     * @param name The name of the table.
     *
     * @return A builder that can collect the data for the table.
     */
    public static final BuildTableStep buildTable(String name)
    {
        return FACT.buildTable(name);
    }

    /**
     * Create a new SQL column that can be use for creating a SQL table.
     *
     * @param <T>       The final java type of the expression.
     * @param <E>       The ResultSet read java type of the expression.
     * @param name      The name for the column.
     * @param type      The SQL type for the colum.
     * @param allowNull If the columns allow null values.
     * @param defValue  The default value for the column.
     *
     * @return The new created column.
     */
    public static final <T, E> Column<T, E> buildColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildColumn(name, type, allowNull, defValue);
    }


    /**
     * Create a new SQL column that can be use for creating a SQL table.
     *
     * @param <T>       The final java type of the expression.
     * @param <E>       The ResultSet read java type of the expression.
     * @param name      The name for the column.
     * @param table     The table for the column.
     * @param type      The SQL type for the colum.
     * @param allowNull If the columns allow null values.
     * @param defValue  The default value for the column.
     *
     * @return The new created column.
     */
    public static final <T, E> Column<T, E> buildColumn(String name, Table table, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildColumn(name, table, type, allowNull, defValue);
    }

    /**
     * Create a new SQL column that can be use for creating a SQL table.
     *
     * @param <T>       The final java type of the expression.
     * @param <E>       The ResultSet read java type of the expression.
     * @param name      The name for the column.
     * @param type      The SQL type for the colum.
     * @param allowNull If the columns allow null values.
     *
     * @return The new created column.
     */
    public static final <T, E> NumberColumn<T, E> buildAiColumn(String name, SQLType<T, E> type, boolean allowNull)
    {
        return FACT.buildAiColumn(name, type, allowNull);
    }

    /**
     * Create a new SQL column that can be use for creating a SQL table.
     *
     * @param <T>       The final java type of the expression.
     * @param <E>       The ResultSet read java type of the expression.
     * @param name      The name for the column.
     * @param type      The SQL type for the colum.
     * @param allowNull If the columns allow null values.
     * @param defValue  The default value for the column.
     *
     * @return The new created column.
     */
    public static final <T, E> NumberColumn<T, E> buildNumberColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildNumberColumn(name, type, allowNull, defValue);
    }

    /**
     * Create a new SQL column that can be use for creating a SQL table.
     *
     * @param <T>       The final java type of the expression.
     * @param <E>       The ResultSet read java type of the expression.
     * @param name      The name for the column.
     * @param type      The SQL type for the colum.
     * @param allowNull If the columns allow null values.
     * @param defValue  The default value for the column.
     *
     * @return The new created column.
     */
    public static final <T, E> DateColumn<T, E> buildDateColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildDateColumn(name, type, allowNull, defValue);
    }

    /**
     * Create a new SQL column that can be use for creating a SQL table.
     *
     * @param <T>       The final java type of the expression.
     * @param <E>       The ResultSet read java type of the expression.
     * @param name      The name for the column.
     * @param type      The SQL type for the colum.
     * @param allowNull If the columns allow null values.
     * @param defValue  The default value for the column.
     *
     * @return The new created column.
     */
    public static final <T, E> StringColumn<T, E> buildStringColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildStringColumn(name, type, allowNull, defValue);
    }

    /**
     * Create a new SQL column that can be use for creating a SQL table.
     *
     * @param <T>       The final java type of the expression.
     * @param <E>       The ResultSet read java type of the expression.
     * @param name      The name for the column.
     * @param type      The SQL type for the colum.
     * @param allowNull If the columns allow null values.
     * @param defValue  The default value for the column.
     *
     * @return The new created column.
     */
    public static final <T, E> BooleanColumn<T, E> buildBooleanColumn(String name, SQLType<T, E> type, boolean allowNull, T defValue)
    {
        return FACT.buildBooleanColumn(name, type, allowNull, defValue);
    }

    /**
     * Create a new index.
     *
     * @param name    The name of the index.
     * @param table   The table that the index belongs to.
     * @param columns The columns for the index.
     *
     * @return The new created index.
     */
    public static final Index buildIndex(String name, Table table, Column<?, ?>... columns)
    {
        return FACT.buildIndex(name, table, columns);
    }

    /**
     * Create a new index.
     *
     * @param table   The table that the index belongs to.
     * @param columns The columns for the index.
     *
     * @return The new created index.
     */
    public static final Index buildIndex(Table table, Column<?, ?>... columns)
    {
        return FACT.buildIndex(table, columns);
    }

    /**
     * Create a new unique index.
     *
     * @param name    The name of the index.
     * @param table   The table that the index belongs to.
     * @param columns The columns for the index.
     *
     * @return The new created unique index.
     */
    public static final Index buildUnique(String name, Table table, Column<?, ?>... columns)
    {
        return FACT.buildUnique(name, table, columns);
    }

    /**
     * Create a new unique index.
     *
     * @param table   The table that the index belongs to.
     * @param columns The columns for the index.
     *
     * @return The new created unique index.
     */
    public static final Index buildUnique(Table table, Column<?, ?>... columns)
    {
        return FACT.buildUnique(table, columns);
    }

    /**
     * Creates a new builder that can be use to create a foreign key.
     *
     * @param name    The name for the foreign key.
     * @param table   The table that the fk belongs to.
     * @param columns The columns for the foreign key.
     *
     * @return The new created builder.
     */
    public static final BuildForeignKeyStep buildForeignKey(String name, Table table, Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(name, table, columns);
    }

    /**
     * Creates a new builder that can be use to create a foreign key.
     *
     * @param table   The table that the fk belongs to.
     * @param columns The columns for the foreign key.
     *
     * @return The new created builder.
     */
    public static final BuildForeignKeyStep buildForeignKey(Table table, Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(table, columns);
    }

    /**
     * Create a new index.
     *
     * @param name    The name of the index.
     * @param columns The columns for the index.
     *
     * @return The new created index.
     */
    public static final Index buildIndex(String name, Column<?, ?>... columns)
    {
        return FACT.buildIndex(name, columns);
    }

    /**
     * Create a new index.
     *
     * @param columns The columns for the index.
     *
     * @return The new created index.
     */
    public static final Index buildIndex(Column<?, ?>... columns)
    {
        return FACT.buildIndex(columns);
    }

    /**
     * Create a new unique index.
     *
     * @param name    The name of the index.
     * @param columns The columns for the index.
     *
     * @return The new created unique index.
     */
    public static final Index buildUnique(String name, Column<?, ?>... columns)
    {
        return FACT.buildUnique(name, columns);
    }

    /**
     * Create a new unique index.
     *
     * @param columns The columns for the index.
     *
     * @return The new created unique index.
     */
    public static final Index buildUnique(Column<?, ?>... columns)
    {
        return FACT.buildUnique(columns);
    }

    /**
     * Creates a new builder that can be use to create a foreign key.
     *
     * @param name    The name for the foreign key.
     * @param columns The columns for the foreign key.
     *
     * @return The new created builder.
     */
    public static final BuildForeignKeyStep buildForeignKey(String name, Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(name, columns);
    }

    /**
     * Creates a new builder that can be use to create a foreign key.
     *
     * @param columns The columns for the foreign key.
     *
     * @return The new created builder.
     */
    public static final BuildForeignKeyStep buildForeignKey(Column<?, ?>... columns)
    {
        return FACT.buildForeignKey(columns);
    }

    /**
     * Creates a new "select" SQL query.
     *
     * @param columns The columns for the select.
     *
     * @return The next step in the select query builder.
     */
    public static final SelectStep select(Expression<?, ?>... columns)
    {
        return FACT.select(columns);
    }

    /**
     * Creates a new "insert" SQL query.
     *
     * @param table The table to insert to.
     *
     * @return The next step in the insert query builder.
     */
    public static final InsertIntoStep insertInto(Table table)
    {
        return FACT.insertInto(table);
    }

    /**
     * Creates a new "update" SQL query.
     *
     * @param table The table to update.
     *
     * @return The next step in the update query builder.
     */
    public static final UpdateStep update(Table table)
    {
        return FACT.update(table);
    }

    /**
     * Creates a new "delete" SQL query.
     *
     * @param tables The tables to delete.
     *
     * @return The next step in the delete query builder.
     */
    public static final DeleteStep delete(Table... tables)
    {
        return FACT.delete(tables);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Number, Number> val(Number value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Byte, Byte> val(byte value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Byte, Byte> val(Byte value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Short, Short> val(short value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Short, Short> val(Short value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Integer, Integer> val(int value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Integer, Integer> val(Integer value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Long, Long> val(long value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Long, Long> val(Long value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Float, Float> val(float value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Float, Float> val(Float value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Double, Double> val(double value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final ArithmeticExpr<Double, Double> val(Double value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final StringExpr<String, String> val(String value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final BooleanExpr<Boolean, Boolean> val(Boolean value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final BooleanExpr<Boolean, Boolean> val(boolean value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final Expression<Character, Character> val(char value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final Expression<Character, Character> val(Character value)
    {
        return FACT.val(value);
    }

    /**
     * Creates a new literal.
     *
     * @param <T>   The final java type of the expression.
     * @param <E>   The ResultSet read java type of the expression.
     * @param value The value for the literal.
     *
     * @return The new created literal.
     */
    public static final <T, E> ArithmeticExpr<T, E> number(T value)
    {
        return FACT.number(value);
    }

    /**
     * Creates a new literal.
     * 
     * @param <T>   The final java type of the expression.
     * @param value The value for the literal.
     * 
     * @return The new created literal.
     */
    public static final <T> BooleanExpr<T, T> bool(T value)
    {
        return FACT.bool(value);
    }

    /**
     * Creates a new literal.
     * 
     * @param <T>   The final java type of the expression.
     * @param value The value for the literal.
     * 
     * @return The new created literal.
     */
    public static final <T> StringExpr<T, T> str(T value)
    {
        return FACT.str(value);
    }

    /**
     * Creates a new literal.
     * 
     * @param <T>   The final java type of the expression.
     * @param value The value for the literal.
     * 
     * @return The new created literal.
     */
    public static final <T> Expression<T, T> custom(T value)
    {
        return FACT.custom(value);
    }

    /**
     * Creates a new param.
     * 
     * @param <T>  The final java type of the expression.
     * @param <E>  The ResultSet read java type of the expression.
     * @param type The type for the param.
     * 
     * @return The new created param.
     */
    public static final <T, E> Expression<T, E> param(SQLType<T, E> type)
    {
        return FACT.param(type);
    }

    /**
     * Creates a new count all expression.
     * 
     * @return The new created expression.
     */
    public static final ArithmeticExpr<Integer, Integer> count()
    {
        return FACT.count();
    }

    /**
     * Creates a new array of elements.
     * 
     * @param <T>  The final java type of the expression.
     * @param <E>  The ResultSet read java type of the expression.
     * @param elements The elements for the array.
     * 
     * @return The new created array.
     */
    public static final <T, E> ArrayExpr<T, E> array(Expression<T, E>... elements)
    {
        return FACT.array(elements);
    }

    /**
     * Creates a new limit expression.
     * 
     * @param offset The offset for the limit expression.
     * @param count The count for the limit expression.
     * 
     * @return The new created limit expression.
     */
    public static final Limit limit(int offset, int count)
    {
        return FACT.limit(offset, count);
    }

    /**
     * Creates a new limit expression.
     * 
     * @param count The count for the limit expression.
     * 
     * @return The new created limit expression.
     */
    public static final Limit limit(int count)
    {
        return FACT.limit(count);
    }

    /**
     * Gets a date time expresion that represents the current date and time.
     * 
     * @return A date time expresion that represents the current date and time.
     */
    public static final DateExpr<LocalDateTime, Date> now()
    {
        return FACT.now();
    }
}
