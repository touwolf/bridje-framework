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

import java.util.List;

/**
 * Represents a database table attached to an entity of the model. This object
 * can be user to query the especific table and return the entitys atached to
 * it.
 *
 * @param <T> The type of the entity that the table is mapped to.
 */
public interface Table<T>
{
    /**
     * Gets the entity class mapped to this table.
     *
     * @return A class of type T representing the class of the entity.
     */
    Class<T> getEntity();

    /**
     * The table name as it is created in the database.
     *
     * @return The name for this table.
     */
    String getName();

    /**
     * All the columns of the table.
     *
     * @return The list of all the columns for this table.
     */
    List<TableColumn<T, ?>> getColumns();

    /**
     * The key column for this table.
     *
     * @return A table column wich is the primary key column for this table.
     */
    TableColumn<T, ?> getKey();

    /**
     * Finds the column for the given field name.
     *
     * @param fieldName The name of the field attached to the column.
     * @return The column object for the given field.
     */
    TableColumn<T, ?> findColumn(String fieldName);
}
