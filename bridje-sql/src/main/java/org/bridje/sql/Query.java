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
 * Represents an SQL query.
 */
public interface Query
{
    /**
     * The resulting fields for this query.
     *
     * @return The resulting fields for this query.
     */
    Expression<?, ?>[] getResultFields();

    /**
     * If this query needs to retrieve generated keys.
     *
     * @return If this query needs to retrieve generated keys.
     */
    boolean isWithGeneratedKeys();

    /**
     * Converts this query to a new SQL statement.
     *
     * @param dialect    The dialect to use for the convertion.
     * @param parameters The parameters to fill in the query.
     *
     * @return The resulting SQL statement.
     */
    SQLStatement toStatement(SQLDialect dialect, Object... parameters);

}
