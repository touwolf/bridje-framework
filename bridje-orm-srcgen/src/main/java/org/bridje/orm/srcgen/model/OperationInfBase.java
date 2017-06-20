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

import java.util.Objects;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class represents the operation base class for all entitios operations.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class OperationInfBase
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private OperationModifier modifier;

    @XmlTransient
    private EntityInfBase entity;
    
    /**
     * The name of the operation.
     * 
     * @return The name of the operation.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the operation.
     * 
     * @param name The name of the operation.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The description of the operation.
     * 
     * @return The description of the operation.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * The description of the operation.
     * 
     * @param description The description of the operation.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The parent entity.
     * 
     * @return The parent entity.
     */
    public EntityInfBase getEntity()
    {
        return entity;
    }

    /**
     * Called by JAXB after unmarshal.
     * 
     * @param u The unmarshaller.
     * @param parent The parent object.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInfBase)parent;
    }

    /**
     * The kind of operation this object represents.
     * 
     * @return The kind of operation this object represents.
     */
    public abstract OperationType getOperationType();

    /**
     * Clone this object to the given entity.
     * 
     * @param entity The parent entity for the clone.
     * @return The new cloned object.
     */
    public abstract OperationInfBase clone(EntityInfBase entity);

    /**
     * 
     * @return 
     */
    public OperationModifier getModifier()
    {
        if(modifier == null) return OperationModifier.PUBLIC;
        return modifier;
    }

    /**
     * 
     * @param modifier 
     */
    public void setModifier(OperationModifier modifier)
    {
        this.modifier = modifier;
    }
    
    /**
     * Convinient clone method for the classes that extends from this one.
     * 
     * @param result The resulting object.
     * @param entity The parent entity.
     */
    protected void clone(OperationInfBase result, EntityInfBase entity)
    {
        result.entity = entity;
        result.name = name;
        result.description = description;
        result.modifier = modifier;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public abstract String getSignature();
}
