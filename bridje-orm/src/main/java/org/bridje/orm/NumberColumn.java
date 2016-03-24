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
 *
 * @param <E>
 * @param <T>
 */
public class NumberColumn<E, T extends Number> extends Column<E, T>
{
    public NumberColumn(Table<E> table, String field, Class<T> type)
    {
        super(table, field, type);
    }

    public NumberColumn(Table<E> table, String field, Class<T> type, String function, List<Object> parameters)
    {
        super(table, field, type, function, parameters);
    }

    public NumberColumn<E, T> sum()
    {
        String functionExp;
        if(getFunction() == null)
        {
            functionExp = "SUM(%s)";
        }
        else
        {
            functionExp = "SUM(" + getFunction() + ")";
        }
        return new NumberColumn<>(getTable(), getField(), getType(), functionExp, getParameters());
    }
    
    public NumberColumn<E, T> puls(T value)
    {
        String functionExp;
        if(getFunction() == null)
        {
            functionExp = "%s + ?";
        }
        else
        {
            functionExp = getFunction() + " + ?";
        }
        addParameter(value);
        return new NumberColumn<>(getTable(), getField(), getType(), functionExp, getParameters());
    }

    public NumberColumn<E, T> minus(T value)
    {
        String functionExp;
        if(getFunction() == null)
        {
            functionExp = "%s - ?";
        }
        else
        {
            functionExp = getFunction() + " - ?";
        }
        addParameter(value);
        return new NumberColumn<>(getTable(), getField(), getType(), functionExp, getParameters());
    }    
}
