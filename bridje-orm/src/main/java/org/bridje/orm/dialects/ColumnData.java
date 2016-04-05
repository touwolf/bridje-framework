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

import java.sql.JDBCType;

/**
 * Interface with the data of a column that can be consume by an SQL dialect to
 * create or fix the column in the database.
 */
public interface ColumnData
{
    /**
     * The data for the table that this column belongs to.
     *
     * @return A TableData object whit the table data that this column belongs
     * to.
     */
    TableData getTableData();

    /**
     * The name of this column in the database.
     *
     * @return An String representing the name of the column.
     */
    String getColumnName();

    /**
     * The SQL type to be use when creating this column in the database.
     *
     * @return A JDBCType representing the generic type for this column.
     */
    JDBCType getSqlType();

    /**
     * Defines if this column is the primary key for the table.
     *
     * @return true this column is the primary key for the table, false
     * otherwise.
     */
    boolean isKey();

    /**
     * The length of this SQL type for this column if it is applicable for
     * example to VARCHAR and numeric types.
     *
     * @return An integer with the length for the column.
     */
    int getLength();

    /**
     * The precision of the column if is applicable to his SQL type for example
     * the float-point types.
     *
     * @return The precision of the column.
     */
    int getPrecision();

    /**
     * Represents if this column must be indexed.
     *
     * @return true an index should be created for this column in the database,
     * false no index should be created for the column.
     */
    boolean isIndexed();

    /**
     * Gets the default value for the column.
     * 
     * @return The String representation of the default value.
     */
    String getDefaultValue();

    /**
     * If this column is an integer base type and is the primary key for the table 
     * it can be autoincrement.
     * 
     * @return true this column is autoincrement column, false otherwise.
     */
    boolean isAutoIncrement();
}
