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

/**
 *
 * @param <E>
 * @param <T>
 */
public class EntityColumn<E, T>
{
    private final EntityTable<E> table;

    private final String field;

    private final Class<T> type;

    public EntityColumn(EntityTable<E> table, String field, Class<T> type)
    {
        this.table = table;
        this.field = field;
        this.type = type;
    }

    public EntityTable<E> getTable()
    {
        return table;
    }

    public String getField()
    {
        return field;
    }

    public Class<T> getType()
    {
        return type;
    }

    public Condition eq(T value)
    {
        return new BinaryCondition(this, Operator.EQ, value);
    }
    
    public Condition ne(T value)
    {
        return new BinaryCondition(this, Operator.NE, value);
    }

    public Condition gt(T value)
    {
        return new BinaryCondition(this, Operator.GT, value);
    }

    public Condition ge(T value)
    {
        return new BinaryCondition(this, Operator.GE, value);
    }

    public Condition lt(T value)
    {
        return new BinaryCondition(this, Operator.LT, value);
    }

    public Condition le(T value)
    {
        return new BinaryCondition(this, Operator.LE, value);
    }
}
