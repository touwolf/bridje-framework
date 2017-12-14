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

package org.bridje.sql;

/**
 * The sets step.
 */
public interface SetsStep extends UpdateWhereStep
{
    /**
     * Adds the value to set for a column in this query.
     *
     * @param <T>    The final java type of the expression.
     * @param <E>    The result set read java type of the expression.
     * @param column The column to set.
     * @param value  The value for the column.
     *
     * @return The next step.
     */
    <T, E> SetsStep set(Column<T, E> column, T value);

    /**
     * Adds the value to set for a column in this query.
     *
     * @param <T>    The final java type of the expression.
     * @param <E>    The result set read java type of the expression.
     * @param column The column to set.
     * @param value  The value for the column.
     *
     * @return The next step.
     */
    <T, E> SetsStep set(Column<T, E> column, Expression<T, E> value);

    /**
     * Adds a WHERE statement to the query.
     *
     * @param condition The condition for the where statement.
     *
     * @return The next step.
     */
    UpdateWhereStep where(BooleanExpr<?, ?> condition);

}
