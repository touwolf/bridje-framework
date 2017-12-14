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

import java.sql.SQLException;

/**
 * Represents a reuslt set for the execution of a select query in the database.
 */
public interface SQLResultSet extends AutoCloseable
{
    /**
     * Fetch the next record.
     * 
     * @return true if more records exists, false otherwise.
     * 
     * @throws SQLException If any SQL error ocurrs.
     */
    boolean next() throws SQLException;

    /**
     * Gets the value for the given expression.
     *
     * @param <T>  The final java type of the expression.
     * @param <E>  The result set read java type of the expression.
     * @param expr The expression to find.
     *
     * @return The value found.
     *
     * @throws SQLException If any SQL error ocurrs.
     */
    <T, E> T get(Expression<T, E> expr) throws SQLException;

    /**
     * Gets the value for the given column.
     *
     * @param <T>    The final java type of the expression.
     * @param <E>    The result set read java type of the expression.
     * @param column The column number. starting in 1
     * @param type   The type of the result.
     *
     * @return The value found.
     *
     * @throws SQLException If any SQL error ocurrs.
     */
    <T, E> T get(int column, SQLType<T, E> type) throws SQLException;

    /**
     * Gets the value for the given expression.
     *
     * @param <T>    The final java type of the expression.
     * @param <E>    The result set read java type of the expression.
     * @param expr   The expression to find.
     * @param parser The parser to use.
     *
     * @return The value found.
     *
     * @throws SQLException If any SQL error ocurrs.
     */
    <T, E> T get(Expression<T, E> expr, SQLValueParser<T, E> parser) throws SQLException;

    /**
     * Gets the value for the given column.
     *
     * @param <T>    The final java type of the expression.
     * @param <E>    The result set read java type of the expression.
     * @param column The column number. starting in 1
     * @param type   The type of the result.
     * @param parser The parser to use.
     *
     * @return The value found.
     *
     * @throws SQLException If any SQL error ocurrs.
     */
    <T, E> T get(int column, SQLType<T, E> type, SQLValueParser<T, E> parser) throws SQLException;

}
