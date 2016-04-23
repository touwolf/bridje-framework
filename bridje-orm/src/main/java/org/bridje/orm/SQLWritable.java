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

import java.util.List;

/**
 * Represents an SQL writable object that can be write to a SQL query.
 */
public interface SQLWritable
{
    /**
     * This method allow to write the condition expresion that need to be used
     * when executing the query into the database.
     *
     * @param parameters The list where the parameters will be put when
     * serializing this condition.
     * @param ctx The current entity context.
     * @return An String object representing the condition expression for this
     * object.
     */
    public abstract String writeSQL(List<Object> parameters, EntityContext ctx);
}
