/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.dql.impl;

import org.bridje.dql.DQLCollection;
import org.bridje.dql.DQLField;
import org.bridje.dql.DQLFieldExpr;
import org.bridje.dql.DQLFilter;

public class DQLFactory
{
    private static DQLFactory INST;

    public static DQLFactory getInstance()
    {
        if(INST == null)
        {
            INST = new DQLFactory();
        }
        return INST;
    }

    private DQLFactory()
    {
    }

    public DQLCollection collection(String name)
    {
        return new DQLCollectionImpl(name);
    }

    public DQLField field(String name)
    {
        return new DQLFieldImpl(name);
    }

    public DQLFilter exists(DQLFieldExpr field)
    {
        return new DQLFieldUnaFilter(DQLOperators.EXISTS, field);
    }

    public DQLFilter in(DQLFieldExpr field, Object... values)
    {
        return new DQLFieldArrFilter(DQLOperators.IN, field, values);
    }

    public DQLFilter eq(DQLFieldExpr field, Object value)
    {
        return new DQLFieldBinFilter(DQLOperators.EQ, field, value);
    }

    public DQLFilter gt(DQLFieldExpr field, Object value)
    {
        return new DQLFieldBinFilter(DQLOperators.GT, field, value);
    }

    public DQLFilter ge(DQLFieldExpr field, Object value)
    {
        return new DQLFieldBinFilter(DQLOperators.GE, field, value);
    }

    public DQLFilter lt(DQLFieldExpr field, Object value)
    {
        return new DQLFieldBinFilter(DQLOperators.LT, field, value);
    }

    public DQLFilter le(DQLFieldExpr field, Object value)
    {
        return new DQLFieldBinFilter(DQLOperators.LE, field, value);
    }

    public DQLFilter ne(DQLFieldExpr field, Object value)
    {
        return new DQLFieldBinFilter(DQLOperators.NE, field, value);
    }

    public DQLFilter and(DQLFilter... elements)
    {
        return null;
    }

    public DQLFilter or(DQLFilter... elements)
    {
        return null;
    }

    public DQLFilter not(DQLFilter elements)
    {
        return null;
    }
}
