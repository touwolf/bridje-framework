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
package org.bridje.sql.impl;

import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SelectExpr;
import org.bridje.sql.TableExpr;

class QueryAsTable implements SelectExpr
{
    private String alias;
    
    private final SelectBuilder query;

    public QueryAsTable(SelectBuilder query, String alias)
    {
        this.query = query;
        this.alias = alias;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.append("( ");
        builder.append(query);
        builder.append(" )");
        builder.append(alias);
    }

    @Override
    public TableExpr as(String alias)
    {
        this.alias = alias;
        return this;
    }

    @Override
    public String getAlias()
    {
        return this.alias;
    }
    
}
