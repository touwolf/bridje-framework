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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class FieldInf
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String column;

    @XmlAttribute
    private Boolean required;

    @XmlTransient
    private Object parent;

    public FieldInf()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColumn()
    {
        if(column == null)
        {
            column = Utils.toSQLName(name);
        }
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public abstract SQLTypeInf getType();

    public String getFullTypeName()
    {
        return getEntity().getModel().getName() + "Types." + getType().getName();
    }

    public Boolean getRequired()
    {
        return required;
    }

    public void setRequired(Boolean required)
    {
        this.required = required;
    }

    public EntityInf getEntity()
    {
        if(parent instanceof EntityInfKey)
        {
            return ((EntityInfKey)parent).getEntity();
        }
        return (EntityInf)parent;
    }

    public void setParent(Object parent)
    {
        this.parent = parent;
    }
    
    public abstract String getColumnClass();
}