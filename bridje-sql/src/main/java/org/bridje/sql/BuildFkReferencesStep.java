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
 * Build foreign key references step.
 */
public interface BuildFkReferencesStep
{
    /**
     * Adds the ON DELTE, ON UPDATE strategy for the foreign key.
     *
     * @param onUpdate The ON DELETE strategy.
     * @param onDelete The ON UPDATE strategy.
     *
     * @return The next step.
     */
    BuildFkFinalStep strategy(ForeignKeyStrategy onUpdate, ForeignKeyStrategy onDelete);

    /**
     * Adds the ON DELTE, ON UPDATE strategy for the foreign key.
     *
     * @param stategy The ON DELETE, ON UPDATE strategy.
     *
     * @return The next step.
     */
    BuildFkFinalStep strategy(ForeignKeyStrategy stategy);

}
