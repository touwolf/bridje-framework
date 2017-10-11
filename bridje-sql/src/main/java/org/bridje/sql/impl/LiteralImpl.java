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

package org.bridje.sql.impl;

import org.bridje.sql.ArithmeticExpr;
import org.bridje.sql.BooleanExpr;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLType;
import org.bridje.sql.StringExpr;
import org.bridje.sql.Literal;

class LiteralImpl<T, E> extends ExpressionBase<T, E> implements BooleanExpr<T, E>, StringExpr<T, E>, ArithmeticExpr<T, E>, Literal<T, E>
{
    private final T value;

    public LiteralImpl(T value)
    {
        super(null);
        this.value = value;
    }

    public LiteralImpl(T value, SQLType<T, E> sqlType)
    {
        super(sqlType);
        this.value = value;
    }

    @Override
    public T getValue()
    {
        return value;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.getParameters().add(value);
        builder.append('?');
    }
}
