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

package org.bridje.orm.dialects;

import java.util.List;

/**
 * Represents the data necesary to create a table into a database.
 */
public interface TableData
{
    /**
     * Gets the data for the primary key column of the table.
     *
     * @return A ColumnData object representing the primary key column data.
     */
    ColumnData getKeyColumn();

    /**
     * The table name.
     *
     * @return The table name.
     */
    String getTableName();

    /**
     * Get a list of all columns for the table.
     *
     * @return A list of ColumnData objects with all the data for the columns of
     * this table.
     */
    List<ColumnData> getColumns();
}
