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

import java.lang.reflect.Field;
import org.bridje.orm.Condition;
import org.bridje.orm.NumberColumn;
import org.bridje.orm.TableStringColumn;

class TableStringColumnImpl<E> extends TableColumnImpl<E, String> implements TableStringColumn<E>
{
    public TableStringColumnImpl(TableImpl<E> table, Field field)
    {
        super(table, field, String.class);
    }

    @Override
    public NumberColumn<Integer> length()
    {
        return new FunctionNumberColumnImpl<>(this, Integer.class, "LENGTH(%s)");
    }

    @Override
    public Condition like(String value)
    {
        return new BinaryCondition(this, Operator.LIKE, serialize(value));
    }
}
