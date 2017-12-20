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

/**
 * Information for a delete query.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteQueryInf extends QueryInf
{
    private WhereStmt where;

    @XmlTransient
    private EntityInf entity;

    @Override
    public String getQueryType()
    {
        return "delete";
    }

    /**
     * Information for the where statement of the query.
     * 
     * @return Information for the where statement of the query.
     */
    public WhereStmt getWhere()
    {
        return where;
    }

    /**
     * Information for the where statement of the query.
     * 
     * @param where Information for the where statement of the query.
     */
    public void setWhere(WhereStmt where)
    {
        this.where = where;
    }

    @Override
    public EntityInf getEntity()
    {
        return entity;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInf)parent;
    }
}
