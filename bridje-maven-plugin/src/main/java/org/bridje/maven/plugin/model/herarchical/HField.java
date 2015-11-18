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

package org.bridje.maven.plugin.model.herarchical;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gilberto
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class HField
{
    @XmlTransient
    private HEntityBase entity;
    
    @XmlAttribute(name = "name")
    private String name;
    
    @XmlAttribute
    private boolean required;
    
    @XmlAttribute
    private boolean readonly;

    @XmlAttribute(name = "transient")
    private boolean isTransient;
    
    public HEntityBase getEntity()
    {
        return entity;
    }

    public void setEntity(HEntityBase entity)
    {
        this.entity = entity;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public abstract String getJavaType();
    
    public abstract boolean getIsList();
    
    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        setEntity((HEntityBase)parent);
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public boolean isReadonly()
    {
        return readonly;
    }

    public void setReadonly(boolean readonly)
    {
        this.readonly = readonly;
    }

    public boolean getTransient()
    {
        return isTransient;
    }

    public void setTransient(boolean isTransient)
    {
        this.isTransient = isTransient;
    }
    
    public boolean getIsNullable()
    {
        switch(getJavaType())
        {
            case "boolean":
            case "char":
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
                return false;
        }
        return true;
    }
}
