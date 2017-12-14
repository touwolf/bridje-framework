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
 * The strategies to use for the rows on update and on delete events.
 */
public enum ForeignKeyStrategy
{
    /**
     * Does not allow the modification.
     */
    NO_ACTION,
    /**
     * Modifies the records in cascade.
     */
    CASCADE,
    /**
     * Set the foreign columns to null.
     */
    SET_NULL,
    /**
     * Set the foreign columns to the default value they have.
     */
    SET_DEFAULT;
}
