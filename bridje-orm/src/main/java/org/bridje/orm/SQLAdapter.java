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
 * Represents an adapter that can be use in a field to serialize, unserialize
 * the value to/from the database.
 *
 * @param <T> The tpye of the field as declared in java.
 * @param <R> The type of the java object that will be storage in the database.
 */
public interface SQLAdapter<T, R>
{
    /**
     * Converts the value from the java type to the SQL equivalent type.
     *
     * @param value  The value to serialize.
     * @param column The column for this value.
     *
     * @return The SQL type to write to the database.
     */
    R serialize(T value, Column column);

    /**
     * Converts the value from the the SQL type to the java type.
     *
     * @param value  The value readed from the database.
     * @param column The column for this value.
     *
     * @return The java type to write to the java object field.
     */
    T unserialize(R value, Column column);

}
