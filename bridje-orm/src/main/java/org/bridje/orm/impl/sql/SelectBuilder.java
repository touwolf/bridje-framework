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
 * A helper class for building SELECT statements more easy.
 */
public class SelectBuilder
{
    private final StringBuilder sb;

    private boolean whereAdded;

    private boolean orderByAdded;

    /**
     * Default constructor.
     */
    public SelectBuilder()
    {
        sb = new StringBuilder();
        whereAdded = false;
    }

    /**
     * Begins this builder whit the select clause.
     * 
     * @param fields The list of fields to retrieve.
     * @return this builder.
     */
    public SelectBuilder select(String fields)
    {
        sb.append("SELECT ");
        sb.append(fields);
        return this;
    }
    
    /**
     * Adds the FROM clause to this builder.
     * 
     * @param table The name of the table.
     * @return this builder.
     */
    public SelectBuilder from(String table)
    {
        sb.append(" FROM ");
        sb.append(table);
        return this;
    }
    
    /**
     * Adds a new JOIN clause to after the FROM statement.
     * 
     * @param joinType INNER, LEFT, RIGHT...
     * @param table The name of the table to join.
     * @param condition The on condition for the join.
     * @return this builder.
     */
    public SelectBuilder join(String joinType, String table, String condition)
    {
        sb.append(" ");
        sb.append(joinType);
        sb.append(" JOIN ");
        sb.append(table);
        sb.append(" ON ");
        sb.append(condition);
        return this;
    }

    /**
     * Adds a new WHERE clause to this SELECT statement. It can be used 
     * multiple times after the first time it will add as an AND.
     * 
     * @param condition The condition to filter this select.
     * @return this builder.
     */
    public SelectBuilder where(String condition)
    {
        sb.append(!whereAdded ? " WHERE " : " AND ");
        sb.append(condition);
        whereAdded = true;
        return this;
    }
    
    /**
     * Adds a new ORDER BY clause to this statement. It can be used multiple 
     * times.
     * 
     * @param orderBy The ORDER BY statement to add.
     * @return this builder.
     */
    public SelectBuilder orderBy(String orderBy)
    {
        sb.append(!orderByAdded ? " ORDER BY " : ", ");
        sb.append(orderBy);
        orderByAdded = true;
        return this;
    }

    /**
     * Adds a new limit statement to this builder.
     * 
     * @param index The index for the limit.
     * @param size The size of the limit.
     * @return this builder.
     */
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
        return sb.toString();
    }
}
