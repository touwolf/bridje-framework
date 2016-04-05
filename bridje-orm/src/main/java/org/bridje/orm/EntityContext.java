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
 * the necesary functionality to manage entitys.
 */
public interface EntityContext
{
    /**
     * This method will create or repair the table for the given entity, so
     * future querys to the database involving this entity won fail. This method
     * won remove any fields if the table alrready exists.
     *
     * @param <T> The type of the entity.
     * @param entityClass The entity class to be The class of the entity.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    <T> void fixTable(Class<T> entityClass) throws SQLException;

    /**
     * This method will find an entity given his class and id.
     *
     * @param <T> The type of the entity.
     * @param entityClass The entity class to be find.
     * @param id The id of the entity to be find.
     * @return The finded entity, or null if no entity can be found by that id.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    <T> T find(Class<T> entityClass, Object id) throws SQLException;

    /**
     * Inserts the given entity in the database. If the entity have an auto
     * increment key field this method will fill the new generated value for
     * that field.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be inserted.
     * @return The same entity passed to this method but with the values updated
     * so it mach the inserted values in the database.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    <T> T insert(T entity) throws SQLException;

    /**
     * Updates the given entity in the database, This method will take the key
     * from the passed entity, so you must be carefull, this method should not
     * be used if the entity has change the value of the key seens it was
     * retrived from database. Other wiser it could update a diferent entity or
     * none if no entity exists with the given key.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be updated.
     * @return The same entity passed to this method.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
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
     * @param id The previous id value for the entity beign updated.
     * @return The same entity passed to this method.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
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
     * proccess.
     */
    <T> T refresh(T entity) throws SQLException;

    /**
     * Deletes the entity in the database
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be
     * @return The same entity passed to this method.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    <T> T delete(T entity) throws SQLException;

    /**
     * Creates a new query with the given entity as the base entity for the
     * query, the object returned by this method can be customized to build must
     * common querys you'l whant to execute on the database.
     *
     * @param <T> The type of the entity.
     * @param entityTable The entityClass to be query.
     * @return A new Query object.
     */
    <T> Query<T> query(Table<T> entityTable);

    /**
     * Clears the internal cache of the entity context, so new querys retrive
     * fresh data from the database, note that entitys returned from this
     * context will be cached, so if you what to reset the context and release
     * memory this method must be call.
     */
    void clearCache();
}
