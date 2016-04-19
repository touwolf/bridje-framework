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

package org.bridje.orm.impl.sql;

/**
 *
 */
public class SelectBuilder
{
    private final StringBuilder sb;

    private boolean whereAdded;

    private boolean orderByAdded;

    public SelectBuilder()
    {
        sb = new StringBuilder();
        whereAdded = false;
    }

    public SelectBuilder select(String fields)
    {
        sb.append("SELECT ");
        sb.append(fields);
        return this;
    }
    
    public SelectBuilder from(String table)
    {
        sb.append(" FROM ");
        sb.append(table);
        return this;
    }
    
    public SelectBuilder innerJoin(String table, String condition)
    {
        sb.append(" INNER JOIN ");
        sb.append(table);
        sb.append(" ON ");
        sb.append(condition);
        return this;
    }

    public SelectBuilder where(String condition)
    {
        sb.append(!whereAdded ? " WHERE " : " AND ");
        sb.append(condition);
        whereAdded = true;
        return this;
    }
    
    public SelectBuilder orderBy(String orderBy)
    {
        sb.append(!orderByAdded ? " ORDER BY " : ", ");
        sb.append(orderBy);
        orderByAdded = true;
        return this;
    }

    public SelectBuilder limit(int index, int size)
    {
        if(index >= 0 && size >= 0)
        {
            sb.append(" LIMIT ");
            sb.append(index);
            sb.append(", ");
            sb.append(size);
        }
        return this;
    }
    
    @Override
    public String toString()
    {
        sb.append(';');
        return sb.toString();
    }
}
