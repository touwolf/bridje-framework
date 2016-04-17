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
 * Represents an string column, string columns are the same as a regular columns
 * but the ofer some unique functions like length.
 *
 * @param <E> The type of the entity that the field this columns represents
 * belongs to.
 */
public interface StringColumn<E> extends Column<String>
{
    /**
     * Creates a new column that will return the the length in characters ot
     * this column.
     *
     * @return The new created column.
     */
    NumberColumn<Integer> length();

    /**
     * Creates a new like condition for this column.
     *
     * @param value The like expression.
     * @return The new created condition.
     */
    Condition like(String value);
}
