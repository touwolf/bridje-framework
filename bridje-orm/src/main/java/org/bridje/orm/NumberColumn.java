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
 * Represents a numeric column, numeric columns are the same as columns but they
 * got son especific funcions that can be used like sum, +, -, etc...
 *
 * @param <E> The type of the entity this columns belongs to.
 * @param <T> The type of the field this column represents.
 */
public class NumberColumn<E, T extends Number> extends Column<E, T>
{
    /**
     * This constructor is used to create a column without any funcions, a
     * column constructed by this constructor along will represent the plain
     * database column with no functions, columns must be created with the table
     * object they belong to, the field name as declared in the entity class,
     * and the Type of the field.
     *
     * @param table The Table object this columns belong to.
     * @param field The field name for the declared java field in the base
     * entity class.
     * @param type The java type for this column.
     */
    public NumberColumn(Table<E> table, String field, Class<T> type)
    {
        super(table, field, type);
    }

    /**
     * This constructor is used to create a column with a function or operator.
     *
     * @param table The Table object this columns belong to.
     * @param field The field name for the declared java field in the base
     * entity class.
     * @param type The java type for this column.
     * @param function The function expresion to be use when selialize this
     * object to a query.
     * @param parameters The parameters list on the current query, that this
     * column must have for correct serialization.
     */
    public NumberColumn(Table<E> table, String field, Class<T> type, String function, List<Object> parameters)
    {
        super(table, field, type, function, parameters);
    }

    /**
     * Creates a new column that totalize all the values of the current column.
     * Ex: if this column is some_col the resulting column will be
     * sum(some_col).
     *
     * @return The new sum column.
     */
    public NumberColumn<E, T> sum()
    {
        String functionExp;
        if (getFunction() == null)
        {
            functionExp = "SUM(%s)";
        }
        else
        {
            functionExp = "SUM(" + getFunction() + ")";
        }
        return new NumberColumn<>(getTable(), getField(), getType(), functionExp, getParameters());
    }

    /**
     * Creates a new column that will return the sum of the current column plus
     * the value especified.
     *
     * @param value The value to add to the column.
     * @return The new created column.
     */
    public NumberColumn<E, T> puls(T value)
    {
        String functionExp;
        if (getFunction() == null)
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

    /**
     * Creates a new column that will return the the current column values minus
     * the value especified.
     *
     * @param value The value to substract to the column.
     * @return The new created column.
     */
    public NumberColumn<E, T> minus(T value)
    {
        String functionExp;
        if (getFunction() == null)
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
