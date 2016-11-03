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

import java.util.ArrayList;
import java.util.List;
import org.bridje.orm.Column;
import org.bridje.orm.Condition;
import org.bridje.orm.EntityContext;
import org.bridje.orm.NumberColumn;
import org.bridje.orm.TableColumn;

class FunctionColumnImpl<T, B> extends AbstractColumn<T> implements NumberColumn<T>
{
    private final Column<B> column;

    private final Class<T> type;

    private final String function;

    private List<Object> parameters;

    /**
     * This constructor is used to create a column with a function or operator.
     *
     * @param column The Table object this columns belong to.
     * @param type The type for this column.
     * @param function The function expression to be use when selialize this
     * object to a query.
     * @param parameters The parameters list on the current query, that this
     * column must have for correct serialization.
     */
    public FunctionColumnImpl(Column<B> column, Class<T> type, String function)
    {
        this.column = column;
        this.function = function;
        this.type = type;
    }

    @Override
    public Class<T> getType()
    {
        return type;
    }

    /**
     * Gets the function expression for this column, if any. if this functions
     * return null or empty it means that this object represents a plain column
     * with no functions.
     *
     * @return The function expression or null if none exists.
     */
    public String getFunction()
    {
        return function;
    }

    /**
     * Gets the parameters list for the function expression for this column, if
     * any. if this functions return null or empty it means that this object
     * represents that no parameter has being put into this column.
     *
     * @return The parameters list or null if none exists.
     */
    public List<Object> getParameters()
    {
        return parameters;
    }

    /**
     * Gets the base column for this function column.
     *
     * @return A Column object representing the base column for this function.
     */
    public Column<B> getColumn()
    {
        return column;
    }

    /**
     * Adds a new parameter to this column, this method should only be use in
     * the functions columns, that needs parameters when they are serialize to
     * create and execute the query into the database.
     *
     * @param param The param to be added.
     */
    protected void addParameter(Object param)
    {
        if (parameters == null)
        {
            parameters = new ArrayList<>();
        }
        parameters.add(param);
    }

    @Override
    public String writeSQL(List<Object> parameters, EntityContext ctx)
    {
        String result;
        if(column instanceof TableColumn)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(column.writeSQL(parameters, ctx));
            result = String.format(getFunction(), sb.toString());
        }
        else
        {
            result = String.format(getFunction(), column.writeSQL(parameters, ctx));
        }
        if(getParameters() != null)
        {
            for (Object parameter : getParameters())
            {
                parameters.add(parameter);
            }
        }
        return result;
    }

    @Override
    public NumberColumn<T> sum()
    {
        return new FunctionColumnImpl<>(this, getType(), "SUM(%s)");
    }

    @Override
    public NumberColumn<T> puls(T value)
    {
        FunctionColumnImpl result = new FunctionColumnImpl<>(this, getType(), "%s + ?");
        result.addParameter(value);
        return result;
    }

    @Override
    public NumberColumn<T> minus(T value)
    {
        FunctionColumnImpl result = new FunctionColumnImpl<>(this,getType(), "%s + ?");
        result.addParameter(value);
        return result;
    }

    @Override
    public Condition gt(T value)
    {
        return new BinaryCondition(this, Operator.GT, value);
    }

    @Override
    public Condition ge(T value)
    {
        return new BinaryCondition(this, Operator.GE, value);
    }

    @Override
    public Condition lt(T value)
    {
        return new BinaryCondition(this, Operator.LT, value);
    }

    @Override
    public Condition le(T value)
    {
        return new BinaryCondition(this, Operator.LE, value);
    }

    @Override
    public Condition in(T... values)
    {
        return new FunctionCondition(this, "IN", values);
    }

    @Override
    public Condition notIn(T... values)
    {
        return new FunctionCondition(this, "NOT IN", values);
    }
}
