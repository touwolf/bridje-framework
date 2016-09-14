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

/**
 * This interface can be obtained as a service from the Ioc, and it´s provides
 * the necesary functionality to manage entities.
 */
public interface EntityContext
{
    /**
     * This method will create or repair the table for the given entity, so
     * future queries to the database involving this entity won fail. This method
     * won remove any fields if the table already exists.
     *
     * @param <T> The type of the entity.
     * @param tables The tables to be created.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    <T> void fixTable(Table<T>... tables) throws SQLException;

    /**
     * This method will find an entity given his class and id.
     *
     * @param <T> The type of the entity.
     * @param table The entity table to be find.
     * @param id The id of the entity to be find.
     * @return The finded entity, or null if no entity can be found by that id.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    <T> T find(Table<T> table, Object id) throws SQLException;

    /**
     * Inserts the given entity in the database. If the entity have an auto
     * increment key field this method will fill the new generated value for
     * that field.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be inserted.
     * @return The same entity passed to this method but with the values updated
     * so it match the inserted values in the database.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    <T> T insert(T entity) throws SQLException;

    /**
     * Updates the given entity in the database, This method will take the key
     * from the passed entity, so you must be carefull, this method should not
     * be used if the entity has change the value of the key seens it was
     * retrieved from database. Other wiser it could update a different entity or
     * none if no entity exists with the given key.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be updated.
     * @return The same entity passed to this method.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    <T> T update(T entity) throws SQLException;

    /**
     * Updates the given entity in the database by the given key. This method
     * takes the previous value of the key field that this entity have in
     * database so using this method you can update the correct entity in the
     * database.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be updated.
     * @param id The previous id value for the entity begin updated.
     * @return The same entity passed to this method.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    <T> T update(T entity, Object id) throws SQLException;

    /**
     * This method will update all the fields of the entity from the actual
     * values in the database.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be refreshed.
     * @return The same entity passed to this method but with the fields
     * refreshed.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    <T> T refresh(T entity) throws SQLException;

    /**
     * Deletes the entity in the database
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be
     * @return The same entity passed to this method.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    <T> T delete(T entity) throws SQLException;

    /**
     * Creates a new query with the given entity as the base entity for the
     * query, the object returned by this method can be customized to build must
     * common queries you'l whant to execute on the database.
     *
     * @param <T> The type of the entity.
     * @param table The entity table to be query.
     * @return A new Query object.
     */
    <T> Query<T> query(Table<T> table);

    /**
     * Clears the internal cache of the entity context, so new queries retrieve
     * fresh data from the database, note that entities returned from this
     * context will be cached, so if you what to reset the context and release
     * memory this method must be call.
     */
    void clearCache();

    /**
     * Gets the current SQL dialect for the entities context.
     *
     * @return The current SQL dialect.
     */
    SQLDialect getDialect();


    /**
     * Finds the table for the given entity.
     *
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return The table object for the given entity.
     */
    <T> Table<T> findTable(Class<T> entity);
}
