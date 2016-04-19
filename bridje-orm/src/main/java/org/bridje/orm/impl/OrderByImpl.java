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
import org.bridje.orm.OrderBy;
import org.bridje.orm.OrderByType;

/**
 * Represents an order by statement that can be use in a query to order the
 * results.
 */
class OrderByImpl implements OrderBy
{
    private final OrderByType type;

    private final Column<?> column;

    /**
     * Creates a new order by statement for the given column.
     * 
     * @param type The ordering type.
     * @param column The column for this order by statement.
     */
    protected OrderByImpl(OrderByType type, Column<?> column)
    {
        this.type = type;
        this.column = column;
    }

    @Override
    public OrderByType getType()
    {
        return type;
    }

    @Override
    public Column<?> getColumn()
    {
        return column;
    }
}
