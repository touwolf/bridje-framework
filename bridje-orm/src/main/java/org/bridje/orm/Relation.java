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

/**
 * This annotation can be use on a field of an entity class to especify that the
 * field should be mapped to a especific column in the target table of the
 * entity, and that the field is a foraign key on the database with the table of
 * the entity of the type of this field. This annotation must be use on a field
 * whos type is another entity of the data model, otherwise the framework will
 * throw an exception indicating that the given field is not a relation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Relation
{
    /**
     * The name of the column to be use as a foraign key. the related field will
     * always be the primary key of the related table.
     *
     * @return The column name.
     */
    String column() default "";
}
