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

import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class OrderBaseStmt
{
    @XmlAttribute(name = "field")
    private String fieldStr;

    public FieldInf getField()
    {
        return getParent().getQuery()
                            .getEntity()
                            .findField(fieldStr);
    }

    public String getFieldStr()
    {
        return fieldStr;
    }

    public void setFieldStr(String fieldStr)
    {
        this.fieldStr = fieldStr;
    }

    public abstract OrderStmt getParent();
    
    public abstract String getSortFunction();

    public abstract OrderBaseStmt clone(OrderStmt parent);

    public void clone(OrderBaseStmt stmt, OrderStmt parent)
    {
        stmt.fieldStr = this.fieldStr;
    }

    public static List<OrderBaseStmt> clone(List<OrderBaseStmt> lst, OrderStmt parent)
    {
        return lst.stream()
                    .map(f -> f.clone(parent))
                    .collect(Collectors.toList());
    }
}
