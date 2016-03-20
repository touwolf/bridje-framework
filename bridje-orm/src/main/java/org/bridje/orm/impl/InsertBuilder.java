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

package org.bridje.orm.impl;

/**
 *
 */
public class InsertBuilder
{
    private final StringBuilder sb;

    public InsertBuilder()
    {
        sb = new StringBuilder();
    }

    public InsertBuilder insertInto(String table)
    {
        sb.append("INSERT INTO ");
        sb.append(table);
        return this;
    }
    
    public InsertBuilder fields(String fields)
    {
        sb.append(" (");
        sb.append(fields);
        sb.append(") ");
        return this;
    }
    
    public InsertBuilder values(String values)
    {
        sb.append(" VALUES (");
        sb.append(values);
        sb.append(")");
        return this;
    }
    
    public InsertBuilder valuesParams(int count)
    {
        sb.append(" VALUES (");
        for (int i = 0; i < count; i++)
        {
            sb.append("?");
        }
        sb.append(")");
        return this;
    }
    
    @Override
    public String toString()
    {
        sb.append(";");
        return sb.toString();
    }
}
