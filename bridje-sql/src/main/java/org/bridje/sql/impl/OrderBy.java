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

import org.bridje.sql.Expression;
import org.bridje.sql.OrderExpr;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SortType;

class OrderBy implements OrderExpr
{
    private final SortType type;

    private final Expression<?> column;

    public OrderBy(SortType type, Expression<?> column)
    {
        this.type = type;
        this.column = column;
    }

    @Override
    public SortType getType()
    {
        return type;
    }

    @Override
    public Expression<?> getColumn()
    {
        return column;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        column.writeSQL(builder);
        if(type != null)
        {
            builder.append(' ');
            builder.append(type.name());
        }
    }
}
