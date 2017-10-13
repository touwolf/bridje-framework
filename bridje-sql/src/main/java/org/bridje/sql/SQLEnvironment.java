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
 * 
 */
public interface SQLEnvironment extends AutoCloseable
{
    /**
     * 
     * @return 
     */
    SQLDialect getDialect();

    /**
     * 
     * @param schema
     * @throws SQLException 
     */
    void fixSchema(Schema schema) throws SQLException;

    /**
     * 
     * @param query
     * @param parameters
     * @return
     * @throws SQLException 
     */
    int update(Query query, Object... parameters) throws SQLException;

    /**
     * 
     * @param <T>
     * @param query
     * @param parser
     * @param parameters
     * @return
     * @throws SQLException 
     */
    <T> List<T> fetchAll(Query query, SQLResultParser<T> parser, Object... parameters) throws SQLException;

    /**
     * 
     * @param <T>
     * @param query
     * @param parser
     * @param parameters
     * @return
     * @throws SQLException 
     */
    <T> T fetchOne(Query query, SQLResultParser<T> parser, Object... parameters) throws SQLException;

    /**
     * 
     * @param stmt
     * @return
     * @throws SQLException 
     */
    int update(SQLStatement stmt) throws SQLException;

    /**
     * 
     * @param <T>
     * @param stmt
     * @param parser
     * @return
     * @throws SQLException 
     */
    <T> List<T> fetchAll(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException;

    /**
     * 
     * @param <T>
     * @param stmt
     * @param parser
     * @return
     * @throws SQLException 
     */
    <T> T fetchOne(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException;

    /**
     * 
     * @throws SQLException 
     */
    void begin() throws SQLException;

    /**
     * 
     * @throws SQLException 
     */
    void commit() throws SQLException;

    /**
     * 
     * @throws SQLException 
     */
    void rollback() throws SQLException;
}
