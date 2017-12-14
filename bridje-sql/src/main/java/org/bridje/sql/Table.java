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
 * Represents an SQL table.
 */
public interface Table extends TableExpr
{
    /**
     * The name of the table.
     *
     * @return The name of the table.
     */
    String getName();

    /**
     * The schema that this table belongs to.
     *
     * @return The schema that this table belongs to.
     */
    Schema getSchema();

    /**
     * The primary key of the table.
     *
     * @return The primary key of the table.
     */
    Column<?, ?>[] getPrimaryKey();

    /**
     * The autoincrement columns for this table.
     *
     * @return The autoincrement columns for this table.
     */
    Column<?, ?>[] getAutoIncrement();

    /**
     * The columns for this table.
     *
     * @return The columns for this table.
     */
    Column<?, ?>[] getColumns();

    /**
     * Gets the given column.
     *
     * @param name The column name.
     *
     * @return The column that was found, or null if it does not exists.
     */
    Column<?, ?> getColumn(String name);

    /**
     * The indexs for this table.
     * 
     * @return The indexs for this table.
     */
    Index[] getIndexes();

    /**
     * The foreign keys for this table.
     * 
     * @return The foreign keys for this table.
     */
    ForeignKey[] getForeignKeys();

}
