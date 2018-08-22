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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class SelectOneQueryInf extends QueryInf
{
    private WhereStmt where;

    private OrderStmt orderBy;

    @XmlTransient
    private EntityInf entity;

    @XmlAttribute
    private String fetch;

    @Override
    public String getQueryType()
    {
        return "selectOne";
    }

    public WhereStmt getWhere()
    {
        return where;
    }

    public void setWhere(WhereStmt where)
    {
        this.where = where;
    }

    public OrderStmt getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(OrderStmt orderBy)
    {
        this.orderBy = orderBy;
    }

    @Override
    public EntityInf getEntity()
    {
        return entity;
    }

    public FieldInf getFetchField()
    {
        if(fetch == null) return null;
        return getEntity().findField(fetch);
    }

    public String getFetch()
    {
        return fetch;
    }

    public void setFetch(String fetch)
    {
        this.fetch = fetch;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInf)parent;
    }

    @Override
    public QueryInf clone(EntityInf entity)
    {
        SelectOneQueryInf result = new SelectOneQueryInf();
        super.clone(result, entity);
        result.entity = entity;
        if(this.where != null) result.where = (WhereStmt)this.where.clone(result, null);
        if(this.orderBy != null) result.orderBy = this.orderBy.clone(result);
        result.fetch = this.fetch;
        return result;
    }
}
