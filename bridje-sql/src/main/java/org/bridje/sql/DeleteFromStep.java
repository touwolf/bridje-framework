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
 * Delete from step.
 */
public interface DeleteFromStep extends DeleteWhereStep
{
    /**
     *
     * @param table
     * @param on
     *
     * @return The next step.
     */
    DeleteFromStep innerJoin(TableExpr table, BooleanExpr<?, ?> on);

    /**
     *
     * @param table
     * @param on
     *
     * @return The next step.
     */
    DeleteFromStep leftJoin(TableExpr table, BooleanExpr<?, ?> on);

    /**
     *
     * @param table
     * @param on
     *
     * @return The next step.
     */
    DeleteFromStep rightJoin(TableExpr table, BooleanExpr<?, ?> on);

    /**
     *
     * @param condition
     *
     * @return The next step.
     */
    DeleteWhereStep where(BooleanExpr<?, ?> condition);

}
