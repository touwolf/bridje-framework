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
 * A functional interface to retrive the column name that must be use to
 * serialize the given column into a query.
 */
@FunctionalInterface
public interface ColumnNameFinder
{
    /**
     * Finds the real column name in the database for the given column.
     *
     * @param column The column whos name needs to be find.
     * @return The real column name in the database for the given column.
     */
    String findColumnName(Column column);
}
