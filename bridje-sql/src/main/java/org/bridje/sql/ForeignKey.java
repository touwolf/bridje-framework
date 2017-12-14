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
 * Represents a foreign key.
 */
public interface ForeignKey
{
    /**
     * The name of the foreign key.
     * 
     * @return The name of the foreign key.
     */
    String getName();

    /**
     * The table of the foreign key.
     * 
     * @return The table of the foreign key.
     */
    Table getTable();

    /**
     * The columns for the foreign key.
     * 
     * @return The columns for the foreign key.
     */
    Column<?, ?>[] getColumns();

    /**
     * The references for the foreign key.
     * 
     * @return The references for the foreign key.
     */
    Table getReferences();

    /**
     * The strategy to do on update.
     * 
     * @return The strategy to do on update.
     */
    ForeignKeyStrategy getOnUpdate();

    /**
     * The strategy to do on delete.
     * 
     * @return The strategy to do on delete.
     */
    ForeignKeyStrategy getOnDelete();
}
