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

import org.bridje.orm.SQLDialect;

/**
 * A helper class for building UPDATE statements more easy.
 */
public class UpdateBuilder
{
    private final StringBuilder sb;
    
    private boolean isFirst = true;
    
    private final SQLDialect dialect;

    /**
     * Default constructor
     * @param dialect
     */
    public UpdateBuilder(SQLDialect dialect)
    {
        sb = new StringBuilder();
        this.dialect = dialect;
    }

    /**
     * Begins this builder by adding the UPDATE statement.
     * 
     * @param table The table to update.
     * @return this builder.
     */
    public UpdateBuilder update(String table)
    {
        sb.append("UPDATE ");
        sb.append(table);
        return this;
    }
    
    /**
     * Sets the value of a field to update, this add the "field = ?" statement 
     * to the builder. It can be used multiple times to add several fields updates.
     * 
     * @param field The field to update.
     * @return this builder.
     */
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
    
    /**
     * Sets the value of a field to update, this add the "field = valueField" statement 
     * to the builder. It can be used multiple times to add several fields updates.
     * 
     * @param field The field to update.
     * @param valueField The value for the field.
     * @return this builder.
     */
    public UpdateBuilder set(String field, String valueField)
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
        sb.append(" = ");
        sb.append(valueField);
        isFirst = false;
        return this;
    }
    
    /**
     * Adds a where clause to this UPDATE statement.
     * 
     * @param condition The condition of the where clause.
     * @return this builder.
     */
    public UpdateBuilder where(String condition)
    {
        sb.append(" WHERE ");
        sb.append(condition);
        return this;
    }

    /**
     * Adds a new limit statement to this builder.
     * 
     * @param index The index for the limit.
     * @param size The size of the limit.
     * @return this builder.
     */
    public UpdateBuilder limit(int index, int size)
    {
        if(index >= 0 && size >= 0)
        {
            if(dialect == null)
            {
                sb.append(" LIMIT ");
                sb.append(index);
                sb.append(", ");
                sb.append(size);
            }
            else
            {
                sb.append(' ');
                sb.append(dialect.limit(index, size));
            }
        }
        return this;
    }
    
    @Override
    public String toString()
    {
        return sb.toString();
    }
}
