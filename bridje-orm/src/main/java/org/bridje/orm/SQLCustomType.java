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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.JDBCType;

/**
 * Defines an object as a custom SQL type that can be use in the orm as a field
 * of an entity. Such entity field will have the properties of this custom type
 * unless it defines its own properties, like the name, sqlType, adapter, etc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SQLCustomType
{
    /**
     * The name of the custom SQL datatype.
     *
     * @return The name of the custom SQL datatype.
     */
    String name();

    /**
     * The JDBCType for the column.
     *
     * @return The JDBCType for the column.
     */
    JDBCType type() default JDBCType.NULL;

    /**
     * Defines the length of the data type for this column for the types that
     * needs then (ex: VARCHAR, CHAR, INTEGER, BIGINT, DECIMAL, ....)
     *
     * @return The length of the field.
     */
    int length() default 0;

    /**
     * Defines the precision of the floating point types.
     *
     * @return The precision of the field.
     */
    int precision() default 0;

    /**
     * Defines the adapter to be use by this field to serialize/deserialize from
     * the database value. To create a new SQLAdapter
     *
     * @return The class of the SQLAdapter to be use.
     */
    Class<? extends SQLAdapter> adapter() default SQLAdapter.class;

}
