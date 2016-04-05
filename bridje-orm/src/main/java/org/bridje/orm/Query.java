/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.orm;

import java.sql.SQLException;
import java.util.List;

/**
 * Represents a query that can be execute in an entity context to retrive
 * entitys from the database.
 *
 * @param <T> The type of the base entity for the query.
 */
public interface Query<T>
{
    /**
     * The page that must be fetch from the database.
     *
     * @param page The page number where 1 is the first page, 0 or less means no
     * page, so all the entitys will be returned.
     * @param size The size in records of a page.
     */
    void paging(int page, int size);

    /**
     * Executes the query and fetch all entitys.
     *
     * @return The list of entitys returned from the database.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    List<T> fetchAll() throws SQLException;

    /**
     * Executes the query and fetch the given column.
     *
     * @param <C> The type of the field the given column represents.
     * @param column The column to be fetch.
     * @return A list of objects of type c representing the values of the
     * column.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    <C> List<C> fetchAll(Column<T, C> column) throws SQLException;

    /**
     * Executes the query and fetch the first record that the database return.
     *
     * @return The first entity returned by the query.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    T fetchOne() throws SQLException;

    /**
     * Executes the query and fetch the first value of the given column returned
     * by the database.
     *
     * @param <C> The type of the field the given column represents.
     * @param column The column to be fetch.
     * @return The first entity returned by the query.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    <C> C fetchOne(Column<T, C> column) throws SQLException;

    /**
     * Executes a select count in the database and gets the number of records
     * this query will return from the database.
     *
     * @return The number of records this query can return, 0 means that this
     * query will not return any records.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    long count() throws SQLException;

    /**
     * Gets when ever this query will return any records at all.
     *
     * @return true the query will return 1 or more record, false the query will
     * not return any records.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    boolean exists() throws SQLException;

    /**
     * Specifies the condition to be use on this query.
     *
     * @param condition A condition object representing the where statement of
     * the query.
     * 
     * @return this object.
     */
    Query<T> where(Condition condition);

    /**
     * Specifies the order by statement to be use in this query.
     * 
     * @param statements The OrderBy objects that the query must use to order the records.
     * @return this object.
     */
    Query<T> orderBy(OrderBy... statements);
}
