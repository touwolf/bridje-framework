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
 * Represent an SQL statement tha can be execute in the database.
 */
public interface SQLStatement
{
    /**
     * The resulting fields of the execution of this statement.
     * 
     * @return The resulting fields.
     */
    Expression<?, ?>[] getResultFields();

    /**
     * If this statement retrieves new generated keys.
     * 
     * @return true this statement retrieves new generated keys, false otherwise.
     */
    boolean isWithGeneratedKeys();

    /**
     * The actual SQL to execute in the database.
     * 
     * @return The actual SQL to execute in the database.
     */
    String getSQL();

    /**
     * The parameter to send to the database for this query.
     * 
     * @return The parameter to send to the database for this query.
     */
    Object[] getParameters();
}
