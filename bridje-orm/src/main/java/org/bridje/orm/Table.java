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

/**
 * Represents a database table attached to an entity of the model. This object
 * can be user to query the especific table and return the entitys atached to
 * it.
 *
 * @param <T> The type of the entity that the table is mapped to.
 */
public class Table<T>
{
    private final Class<T> entityClass;

    /**
     * Constructor that receives the entity class.
     *
     * @param entityClass The entityClass that maps to the table.
     */
    public Table(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    /**
     * Gets the entity class mapped to this table.
     *
     * @return A class of type T representing the class of the entity.
     */
    public Class<T> getEntityClass()
    {
        return entityClass;
    }
}
