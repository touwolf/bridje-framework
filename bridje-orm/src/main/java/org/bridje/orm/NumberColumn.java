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
 * Represents a numeric column, numeric columns are the same as columns but they
 * got son especific funcions that can be used like sum, +, -, etc...
 *
 * @param <T> The type of the field this column represents.
 */
public interface NumberColumn<T extends Number> extends ComparableColumn<T>
{
    /**
     * Creates a new column that totalize all the values of the current column.
     * Ex: if this column is some_col the resulting column will be
     * sum(some_col).
     *
     * @return The new sum column.
     */
    NumberColumn<T> sum();

    /**
     * Creates a new column that will return the sum of the current column plus
     * the value especified.
     *
     * @param value The value to add to the column.
     * @return The new created column.
     */
    NumberColumn<T> puls(T value);

    /**
     * Creates a new column that will return the the current column values minus
     * the value especified.
     *
     * @param value The value to substract to the column.
     * @return The new created column.
     */
    NumberColumn<T> minus(T value);
}
