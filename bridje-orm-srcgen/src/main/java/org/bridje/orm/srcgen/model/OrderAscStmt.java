/*
 * Copyright 2018 Bridje Framework.
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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderAscStmt extends OrderBaseStmt
{
    private OrderStmt parent;

    @Override
    public String getSortFunction()
    {
        return "asc";
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        this.parent = (OrderStmt)parent;
    }

    @Override
    public OrderStmt getParent()
    {
        return parent;
    }

    @Override
    public OrderBaseStmt clone(OrderStmt parent)
    {
        OrderAscStmt result = new OrderAscStmt();
        super.clone(result, parent);
        result.parent = parent;
        return result;
    }
}
