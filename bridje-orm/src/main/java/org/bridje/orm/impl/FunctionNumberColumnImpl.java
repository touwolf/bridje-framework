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

import org.bridje.orm.Column;
import org.bridje.orm.Condition;
import org.bridje.orm.NumberColumn;

/**
 *
 */
class FunctionNumberColumnImpl<T extends Number, B> extends FunctionColumnImpl<T, B> implements NumberColumn<T>
{
    public FunctionNumberColumnImpl(Column<B> column, Class<T> type, String function)
    {
        super(column, type, function);
    }

    @Override
    public NumberColumn<T> sum()
    {
        return new FunctionNumberColumnImpl<>(this, getType(), "SUM(%s)");
    }

    @Override
    public NumberColumn<T> puls(T value)
    {
        FunctionNumberColumnImpl result = new FunctionNumberColumnImpl<>(this, getType(), "%s + ?");
        result.addParameter(value);
        return result;
    }

    @Override
    public NumberColumn<T> minus(T value)
    {
        FunctionNumberColumnImpl result = new FunctionNumberColumnImpl<>(this,getType(), "%s + ?");
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
}
