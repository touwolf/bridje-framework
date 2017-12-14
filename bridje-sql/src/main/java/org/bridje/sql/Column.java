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

package org.bridje.sql;

/**
 * Represents a column for a table for an SQL database.
 *
 * @param <T> The final java type of the expression.
 * @param <E> The result set read java type of the expression.
 */
public interface Column<T, E> extends Expression<T, E>
{
    /**
     * The table that this column belongs to.
     *
     * @return The table that this column belongs to.
     */
    Table getTable();

    /**
     * The name of the column.
     *
     * @return The name of the column.
     */
    String getName();

    /**
     * If this column is part of the primary key.
     *
     * @return true this column is part of the primary key, false otherwise.
     */
    boolean isKey();

    /**
     * If this column allows null values.
     *
     * @return true this column allows null values, false otherwise.
     */
    boolean isAllowNull();

    /**
     * If this column is an autoincrement column.
     *
     * @return true this column is an autoincrement column, false otherwise.
     */
    boolean isAutoIncrement();

    /**
     * The default value for this column.
     *
     * @return The default value for this column.
     */
    T getDefValue();

    /**
     * Gets an SQL expression that represents a param (?) for this column.
     *
     * @return The SQL expression that represents a param (?) for this column.
     */
    Expression<T, E> asParam();

}
