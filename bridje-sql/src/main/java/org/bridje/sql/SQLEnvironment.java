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
import java.util.List;

/**
 * The work environment for the SQL API.
 */
public interface SQLEnvironment extends AutoCloseable
{
    /**
     * The dialect of this environment.
     *
     * @return The dialect of this environment.
     */
    SQLDialect getDialect();

    /**
     * Fix a schema (creates all missing objects) in the underlying SQL
     * database.
     *
     * @param schema The schema to fix.
     *
     * @throws SQLException If any SQL error occurs.
     */
    void fixSchema(Schema schema) throws SQLException;

    /**
     * Performs an update query in the database.
     *
     * @param query      The query to execute.
     * @param parameters The parameter for the query.
     *
     * @return The uypdated records count.
     *
     * @throws SQLException If any SQL error occurs.
     */
    int update(Query query, Object... parameters) throws SQLException;

    /**
     * Performs a select many query in the database.
     *
     * @param <T>        The type of the result.
     * @param query      The query to execute.
     * @param parser     The records parser.
     * @param parameters The parameters for the query.
     *
     * @return The list of records.
     *
     * @throws SQLException If any SQL error occurs.
     */
    <T> List<T> fetchAll(Query query, SQLResultParser<T> parser, Object... parameters) throws SQLException;

    /**
     * Performs a select one query in the database.
     *
     * @param <T>        The type of the result.
     * @param query      The query to execute.
     * @param parser     The records parser.
     * @param parameters The parameters for the query.
     *
     * @return The record found.
     *
     * @throws SQLException If any SQL error occurs.
     */
    <T> T fetchOne(Query query, SQLResultParser<T> parser, Object... parameters) throws SQLException;

    /**
     * Performs an update query in the database.
     *
     * @param stmt The SQL statement to execute.
     *
     * @return The updated records count.
     *
     * @throws SQLException If any SQL error occurs.
     */
    int update(SQLStatement stmt) throws SQLException;

    /**
     * Performs a select many query in the database.
     *
     * @param <T>    The type of the result.
     * @param stmt   The SQL statement to execute.
     * @param parser The records parser.
     *
     * @return The list of records.
     *
     * @throws SQLException If any SQL error occurs.
     */
    <T> List<T> fetchAll(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException;

    /**
     * Performs a select one query in the database.
     *
     * @param <T>    The type of the result.
     * @param stmt   The SQL statement to execute.
     * @param parser The records parser.
     *
     * @return The record found.
     *
     * @throws SQLException If any SQL error occurs.
     */
    <T> T fetchOne(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException;

    /**
     * Begins a new SQL transaction.
     *
     * @throws SQLException If any SQL error occurs.
     */
    void begin() throws SQLException;

    /**
     * Commits the current SQL transaction.
     *
     * @throws SQLException If any SQL error occurs.
     */
    void commit() throws SQLException;

    /**
     * Rollbacks the current SQL transaction.
     * 
     * @throws SQLException If any SQL error occurs.
     */
    void rollback() throws SQLException;

}
