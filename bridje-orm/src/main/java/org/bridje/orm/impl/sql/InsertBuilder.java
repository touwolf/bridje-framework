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
 * A helper class for building INSERT statements more easy.
 */
public class InsertBuilder
{
    private final StringBuilder sb;

    /**
     * Default constructor.
     */
    public InsertBuilder()
    {
        sb = new StringBuilder();
    }

    /**
     * Begins the INSERT INTO statement.
     * 
     * @param table The name of the table to insert into.
     * @return this builder.
     */
    public InsertBuilder insertInto(String table)
    {
        sb.append("INSERT INTO ");
        sb.append(table);
        return this;
    }
    
    /**
     * Adds the list of fields to insert into.
     * 
     * @param fields The list of fields to insert into.
     * @return this builder.
     */
    public InsertBuilder fields(String fields)
    {
        sb.append(" (");
        sb.append(fields);
        sb.append(") ");
        return this;
    }

    /**
     * Adds the values to be inserted.
     * 
     * @param values The values to insert.
     * @return this builder.
     */
    public InsertBuilder values(String values)
    {
        sb.append(" VALUES (");
        sb.append(values);
        sb.append(')');
        return this;
    }

    /**
     * Adds the parameters values "?" to the insert statement.
     * 
     * @param count The number of parameters to insert.
     * @return this builder.
     */
    public InsertBuilder valuesParams(int count)
    {
        sb.append(" VALUES (");
        for (int i = 0; i < count; i++)
        {
            if(i > 0)
            {
                sb.append(", ");
            }
            sb.append('?');
        }
        sb.append(')');
        return this;
    }

    @Override
    public String toString()
    {
        return sb.toString();
    }
}
