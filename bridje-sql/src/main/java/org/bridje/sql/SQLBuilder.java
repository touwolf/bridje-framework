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

import java.util.List;

/**
 * A builder for SQL queries.
 */
public interface SQLBuilder
{
    /**
     * If the builder needs to print the columns as simple names rather than in
     * the table.column form.
     *
     * @return true if the builder will print the columns as simple names, false
     *         otherwise.
     */
    boolean isSimpleColumnNames();

    /**
     * If the builder needs to print the columns as simple names rather than in
     * the table.column form.
     *
     * @param simpleColumnNames true if the builder will print the columns as
     *                          simple names, false otherwise.
     */
    void setSimpleColumnNames(boolean simpleColumnNames);

    /**
     * The SQL dialect used for this builder.
     *
     * @return The SQL dialect used for this builder.
     */
    SQLDialect getDialect();

    /**
     * The current list of parameters added to this builder.
     *
     * @return The current list of parameters added to this builder.
     */
    List<Object> getParameters();

    /**
     * Appends a new string to the SQL query.
     *
     * @param str The string to append.
     *
     * @return this object.
     */
    SQLBuilder append(String str);

    /**
     * Appends a new string to the SQL query.
     *
     * @param s The char sequence to append.
     *
     * @return this object.
     */
    SQLBuilder append(CharSequence s);

    /**
     * Appends a new char to the SQL query.
     *
     * @param c The char to append.
     *
     * @return this object.
     */
    SQLBuilder append(char c);

    /**
     * Appends a new integer to the SQL query.
     *
     * @param i The integer to append.
     *
     * @return this object.
     */
    SQLBuilder append(int i);

    /**
     * Appends a new long to the SQL query.
     *
     * @param lng The long to append.
     *
     * @return this object.
     */
    SQLBuilder append(long lng);

    /**
     * Appends a new float to the SQL query.
     *
     * @param f The float to append.
     *
     * @return this object.
     */
    SQLBuilder append(float f);

    /**
     * Appends a new double to the SQL query.
     *
     * @param d The double to append.
     *
     * @return this object.
     */
    SQLBuilder append(double d);

    /**
     * Appends a new object name to the SQL query.
     *
     * @param name The name to append.
     *
     * @return this object.
     */
    SQLBuilder appendObjectName(String name);

    /**
     * Appends a new limit statement to the SQL query.
     *
     * @param offset The offset to append.
     * @param count  The count to append.
     *
     * @return this object.
     */
    SQLBuilder appendLimit(int offset, int count);

    /**
     * Appends a new limit statement to the SQL query.
     *
     * @param count  The count to append.
     *
     * @return this object.
     */
    SQLBuilder appendLimit(int count);

    /**
     * Appends a new SQL expression to the SQL query.
     *
     * @param expression The expression to append.
     *
     * @return this object.
     */
    SQLBuilder append(SQLWritable expression);

    /**
     * Appends the array of SQL expressions to the SQL query.
     *
     * @param expressions The expressions to append.
     * @param sep         The separator to use.
     */
    void appendAll(SQLWritable[] expressions, String sep);

}
