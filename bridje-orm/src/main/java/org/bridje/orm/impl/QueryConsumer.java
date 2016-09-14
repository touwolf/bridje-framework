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

package org.bridje.orm.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional interface to parse the results of a ResultSet object into the
 * required type.
 *
 * @param <T> The type to convert the result set records to.
 */
@FunctionalInterface
interface QueryConsumer<T>
{
    /**
     * Parse the result set and gets the required type.
     *
     * @param rs The result set to parse.
     * @return The resulting type.
     * @throws SQLException If any SQLException occurs during the close
     * process.
     */
    T parse(ResultSet rs) throws SQLException;
}
