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
 * An utility class for creating UPDATE SQL querys.
 */
public class UpdateBuilder
{
    private final StringBuilder sb;
    
    private boolean isFirst = true;

    public UpdateBuilder()
    {
        sb = new StringBuilder();
    }

    public UpdateBuilder update(String table)
    {
        sb.append("UPDATE ");
        sb.append(table);
        return this;
    }
    
    public UpdateBuilder set(String field)
    {
        if(isFirst)
        {
            sb.append(" SET ");
        }
        else
        {
            sb.append(", ");
        }
        sb.append(field);
        sb.append(" = ?");
        isFirst = false;
        return this;
    }
    
    public UpdateBuilder where(String condition)
    {
        sb.append(" WHERE ");
        sb.append(condition);
        return this;
    }
    
    @Override
    public String toString()
    {
        return sb.toString();
    }
}
