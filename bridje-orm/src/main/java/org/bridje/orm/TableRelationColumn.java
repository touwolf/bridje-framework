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
 * Represents a column from a table that is a relation to another table in the
 * model.
 *
 * @param <E> The type for the base entity.
 * @param <R> The type for the related entity.
 */
public interface TableRelationColumn<E, R> extends TableColumn<E, R>
{
    /**
     * Gets the related table object.
     *
     * @return The table object that this table is related to.
     */
    Table<R> getRelated();
}
