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

package org.bridje.orm;

/**
 * Context where entities are keep.
 */
public interface EntityContext
{
    /**
     * Determines if this context contains the given entity.
     *
     * @param <T>    The type of the entity.
     * @param entity The class of the entity to look for.
     * @param id     The id of the entity to look for.
     *
     * @return true if this context contains the entity, false otherwise.
     */
    <T> boolean contains(Class<T> entity, Object id);

    /**
     * Gets the given entity by its id.
     *
     * @param <T>    The type of the entity.
     * @param entity The class of the entity to look for.
     * @param id     The id of the entity to look for.
     *
     * @return The entity found, or null if it does not exists.
     */
    <T> T get(Class<T> entity, Object id);

    /**
     * Adds the given entity to the context.
     *
     * @param <T>    The type of the entity.
     * @param entity The class of the entity to look for.
     * @param id     The id of the entity to look for.
     *
     */
    <T> void put(Object id, T entity);

    /**
     * Remove the given entity.
     *
     * @param <T>    The type of the entity.
     * @param entity The class of the entity to look for.
     * @param id     The id of the entity to look for.
     *
     */
    <T> void remove(Class<T> entity, Object id);

    /**
     * Remove all stored entities of the given class.
     *
     * @param <T>    The type of the entity.
     * @param entity The class of the entity to look for.
     *
     */
    <T> void clear(Class<T> entity);

    /**
     * Remove all stored entitys.
     */
    void clear();

}
