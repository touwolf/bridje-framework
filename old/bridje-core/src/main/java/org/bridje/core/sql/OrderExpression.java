
package org.bridje.core.sql;

import java.io.StringWriter;

/*
 * Copyright 2015 Bridje Framework.
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

public class OrderExpression implements SQLExpression
{
    private final Column column;
    
    private final String orderType;

    public OrderExpression(Column column, String orderType)
    {
        this.column = column;
        this.orderType = orderType;
    }
    
    @Override
    public void writeSQL(StringWriter sw)
    {
        column.writeSQL(sw);
        sw.write(" ");
        sw.write(orderType);
    }

    public Column getColumn()
    {
        return column;
    }

    public String getOrderType()
    {
        return orderType;
    }
}
