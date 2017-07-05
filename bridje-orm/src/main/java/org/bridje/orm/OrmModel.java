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

import java.sql.SQLException;

public interface OrmModel
{
    /**
     * Obtains the entity context.
     *
     * @return The entity context object.
     */
    EntityContext getContext();

    /**
     * Retrieve all the tables for the entities that are handled by this model.
     * @return all the tables for the entities that are handled by this model.
     */
    Table<?>[] tables();

    /**
     * Fix all the tables of the model in the database.
     * @throws java.sql.SQLException If any SQL exception occurs.
     */
    void fixAllTables() throws SQLException;

    /**
     * Determines when ever this model has the given entity.
     *
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return true the given class is an entity of this model, false otherwise.
     */
    <T> boolean haveEntity(Class<T> entity);
}
