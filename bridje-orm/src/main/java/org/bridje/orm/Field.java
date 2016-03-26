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
 * This annotation can be use on a field of an entity class to especify that the
 * field should be mapped to a especific column in the target table of the
 * entity.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field
{
    /**
     * The column name that this field should be mapped to.
     *
     * @return The name of the column in the target table of the entity.
     */
    String column() default "";

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
     * Defines if this field have a index or not. If true th ORM API will create
     * an index on this field upon fix the Entity table.
     *
     * @return true the column will be indexed, false no index will be
     * automatically created on the column of this field.
     */
    boolean index() default false;
}
