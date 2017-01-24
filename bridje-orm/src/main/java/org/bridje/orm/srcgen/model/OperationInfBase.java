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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class OperationInfBase
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlTransient
    private EntityInfBase entity;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public EntityInfBase getEntity()
    {
        return entity;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInfBase)parent;
    }

    public abstract OperationType getOperationType();
    
    public abstract OperationInfBase clone(EntityInfBase entity);
    
    protected void clone(OperationInfBase result, EntityInfBase entity)
    {
        result.entity = entity;
        result.name = name;
        result.description = description;
    }
}
