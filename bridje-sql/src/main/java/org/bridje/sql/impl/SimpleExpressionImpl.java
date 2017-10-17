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

import java.util.Objects;
import org.bridje.sql.ArithmeticExpr;
import org.bridje.sql.BooleanExpr;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLType;
import org.bridje.sql.StringExpr;

class SimpleExpressionImpl<T, E> extends ExpressionBase<T, E> implements BooleanExpr<T, E>, StringExpr<T, E>, ArithmeticExpr<T, E>
{
    private final String expr;

    public SimpleExpressionImpl(String expr, SQLType<T, E> type)
    {
        super(type);
        this.expr = expr;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.append(expr);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.expr);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SimpleExpressionImpl<?, ?> other = (SimpleExpressionImpl<?, ?>) obj;
        return Objects.equals(this.expr, other.expr);
    }
}
