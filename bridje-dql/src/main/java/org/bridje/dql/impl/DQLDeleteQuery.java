/*
 * Copyright 2018 Bridje Framework.
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

package org.bridje.dql.impl;

import org.bridje.dql.DQLDialect;
import org.bridje.dql.DQLFilter;
import org.bridje.dql.DQLQuery;

class DQLDeleteQuery implements DQLQuery
{
    private final DQLCollectionImpl collection;

    private final DQLFilter filter;

    public DQLDeleteQuery(DQLCollectionImpl collection, DQLFilter filter)
    {
        this.collection = collection;
        this.filter = filter;
    }

    @Override
    public String toStatement(DQLDialect dialect)
    {
        StringBuilder sb = new StringBuilder();
        filter.writeFilter(sb, dialect);
        return sb.toString();
    }
}
