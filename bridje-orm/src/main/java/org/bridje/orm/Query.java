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
 * Represents a query that can be execute in an entity context to retrieve
 * entities from the database.
 *
 * @param <T> The type of the base entity for the query.
 */
public interface Query<T>
{
    /**
     * The page that must be fetch from the database.
     *
     * @param page The page number where 1 is the first page, 0 or less means no
     *             page, so all the entities will be returned.
     * @param size The size in records of a page.
     *
     * @return this
     */
    Query<T> paging(int page, int size);

    /**
     * Executes the query and fetch all entities.
     *
     * @return The list of entities returned from the database.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    List<T> fetchAll() throws SQLException;

    /**
     * Executes the query and fetch the given column.
     *
     * @param <C>    The type of the field the given column represents.
     * @param column The column to be fetch.
     *
     * @return A list of objects of type c representing the values of the
     *         column.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    <C> List<C> fetchAll(Column<C> column) throws SQLException;

    /**
     * Executes the query and fetch the first value of the given table returned
     * by the database.
     *
     * @param <R>   The entity of the given table.
     * @param table The table to be fetch.
     *
     * @return The first entity returned by the query.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    <R> R fetchOne(Table<R> table) throws SQLException;

    /**
     * Executes the query and fetch the given table.
     *
     * @param <R>   The entity of the given table represents.
     * @param table The table to be fetch.
     *
     * @return A list of objects of type R representing the values of the result
     *         entity.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    <R> List<R> fetchAll(Table<R> table) throws SQLException;

    /**
     * Executes the query and fetch the first record that the database return.
     *
     * @return The first entity returned by the query.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    T fetchOne() throws SQLException;

    /**
     * Executes the query and fetch the first value of the given column returned
     * by the database.
     *
     * @param <C>    The type of the field the given column represents.
     * @param column The column to be fetch.
     *
     * @return The first entity returned by the query.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    <C> C fetchOne(Column<C> column) throws SQLException;

    /**
     * Executes a delete query in the database with the current where condition.
     *
     * @return The number of records that were deleted in the database.
     *
     * @throws java.sql.SQLException If the query execution fails.
     */
    int delete() throws SQLException;

    /**
     * Executes a update query in the database with the current where condition.
     *
     * @return The number of records that were updated in the database.
     *
     * @throws java.sql.SQLException If the query execution fails.
     */
    int update() throws SQLException;

    /**
     * Executes an insert query in the database with the current where condition.
     *
     * @return The number of records that were inserted in the database.
     *
     * @throws java.sql.SQLException If the query execution fails.
     */
    int insert() throws SQLException;

    /**
     * Executes a select count in the database and gets the number of records
     * this query will return from the database.
     *
     * @return The number of records this query can return, 0 means that this
     *         query will not return any records.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    long count() throws SQLException;

    /**
     * Gets when ever this query will return any records at all.
     *
     * @return true the query will return 1 or more record, false the query will
     *         not return any records.
     *
     * @throws SQLException If any SQLException occurs during the close process.
     */
    boolean exists() throws SQLException;

    /**
     * Creates a new join query out of the given relation.
     *
     * @param <R>      The related entity type.
     * @param relation The relation column object.
     *
     * @return The new create join query.
     */
    <R> Query<R> join(TableRelationColumn<T, R> relation);

    /**
     * Creates a new left join query out of the given relation.
     *
     * @param <R>      The related entity type.
     * @param relation The relation column object.
     *
     * @return The new create join query.
     */
    <R> Query<R> leftJoin(TableRelationColumn<T, R> relation);

    /**
     * Creates a new left join query out of the given relation.
     *
     * @param <R>      The related entity type.
     * @param relation The relation column object.
     *
     * @return The new create join query.
     */
    <R> Query<R> rightJoin(TableRelationColumn<T, R> relation);

    /**
     * Creates a new join query out of the given relation.
     *
     * @param <R>     The related entity type.
     * @param related The related table.
     * @param on      The on condition for thid join.
     *
     * @return The new create join query.
     */
    <R> Query<R> join(Table<R> related, Condition on);

    /**
     * Creates a new left join query out of the given relation.
     *
     * @param <R>     The related entity type.
     * @param related The related table.
     * @param on      The on condition for thid join.
     *
     * @return The new create join query.
     */
    <R> Query<R> leftJoin(Table<R> related, Condition on);

    /**
     * Creates a new left join query out of the given relation.
     *
     * @param <R>     The related entity type.
     * @param related The related table.
     * @param on      The on condition for thid join.
     *
     * @return The new create join query.
     */
    <R> Query<R> rightJoin(Table<R> related, Condition on);

    /**
     * Specifies the condition to be use on this query.
     *
     * @param condition A condition object representing the where statement of
     *                  the query.
     *
     * @return this object.
     */
    Query<T> where(Condition condition);

    /**
     * Specifies the order by statement to be use in this query.
     *
     * @param statements The OrderBy objects that the query must use to order
     *                   the records.
     *
     * @return this object.
     */
    Query<T> orderBy(OrderBy... statements);

    /**
     * Sets the value of a column so it can be use in either an INSERT or UPDATE
     * statement.
     *
     * @param <D>    The type of the column to be set.
     * @param column The column to be set.
     * @param value  The literal value for the field.
     *
     * @return this object.
     */
    <D> Query<T> set(TableColumn<T, D> column, D value);

    /**
     * Sets the value of a column so it can be use in either an INSERT or UPDATE
     * statement.
     *
     * @param <D>         The type of the column to be set.
     * @param column      The column to be set.
     * @param valueColumn The column that will be use to read the value to be
     *                    set.
     *
     * @return this object.
     */
    <D> Query<T> set(TableColumn<T, D> column, Column<D> valueColumn);

}
