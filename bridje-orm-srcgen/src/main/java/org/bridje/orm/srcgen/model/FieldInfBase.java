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
import javax.xml.bind.annotation.XmlTransient;

/**
 * The base class for all the fields that an entity may have.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class FieldInfBase
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String column;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private Boolean required;

    @XmlAttribute
    private Boolean indexed;

    @XmlAttribute
    private Boolean key;

    @XmlTransient
    private EntityInfBase entity;

    /**
     * The name of the field.
     *
     * @return The name of the field.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the field.
     *
     * @param name The name of the field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The name of the column for this field.
     *
     * @return The name of the column for this field.
     */
    public String getColumn()
    {
        if (this.column == null)
        {
            this.column = Utils.toSQLName(this.name);
        }
        return column;
    }

    /**
     * The name of the column for this field.
     *
     * @param column The name of the column for this field.
     */
    public void setColumn(String column)
    {
        this.column = column;
    }

    /**
     * The description of the field.
     *
     * @return The description of the field.
     */
    public String getDescription()
    {
        if(description == null || description.trim().isEmpty())
        {
            return entity.getModel().getFieldDescription();
        }
        return description;
    }

    /**
     * The description of the field.
     *
     * @param description The description of the field.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * If the field must have a value.
     *
     * @return If the field must have a value.
     */
    public boolean getRequired()
    {
        if(required == null) return false;
        return required;
    }

    /**
     * If the field must have a value.
     *
     * @param required If the field must have a value.
     */
    public void setRequired(Boolean required)
    {
        this.required = required;
    }

    /**
     * If the column for this field must be indexed or not.
     *
     * @return If the column for this field must be indexed or not.
     */
    public boolean getIndexed()
    {
        if(indexed == null) return false;
        return indexed;
    }

    /**
     * If the column for this field must be indexed or not.
     *
     * @param indexed If the column for this field must be indexed or not.
     */
    public void setIndexed(Boolean indexed)
    {
        this.indexed = indexed;
    }

    /**
     * If the field is the key field for the entity.
     *
     * @return If the field is the key field for the entity.
     */
    public Boolean getKey()
    {
        if (key == null)
        {
            return false;
        }
        return key;
    }

    /**
     * If the field is the key field for the entity.
     *
     * @param key If the field is the key field for the entity.
     */
    public void setKey(Boolean key)
    {
        this.key = key;
    }

    /**
     * The entity that this field belongs to.
     *
     * @return The entity that this field belongs to.
     */
    public EntityInfBase getEntity()
    {
        return entity;
    }

    /**
     * The java type for this field.
     *
     * @return The java type for this field.
     */
    public abstract String getJavaType();

    /**
     * The table columnb for this field.
     *
     * @return The table columnb for this field.
     */
    public String getTableColumn()
    {
        return "TableComparableColumn";
    }

    /**
     * This method will be called by JABX after unmarshall.
     *
     * @param u      The unmarshaller.
     * @param parent The parent object.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInfBase) parent;
    }

    /**
     * this methods clone the current object and sets his entity to the given
     * entity.
     *
     * @param entity The parent entity for the new cloned object.
     *
     * @return The cloned object.
     */
    public abstract FieldInfBase clone(EntityInfBase entity);

    /**
     * Used by child classes to clone thenself.
     *
     * @param result The cloned object to fill.
     * @param entity The parent entity.
     */
    protected void clone(FieldInfBase result, EntityInfBase entity)
    {
        result.entity = entity;
        result.column = column;
        result.description = description;
        result.indexed = indexed;
        result.key = key;
        result.name = name;
        result.required = required;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
