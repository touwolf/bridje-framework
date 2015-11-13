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
public abstract class HField
{
    @XmlTransient
    private HEntity entity;
    
    @XmlAttribute(name = "name")
    private String name;
    
    @XmlAttribute(name = "type")
    private HFieldType type;

    public HEntity getEntity()
    {
        return entity;
    }

    public void setEntity(HEntity entity)
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

    public HFieldType getType()
    {
        return type;
    }

    public void setType(HFieldType type)
    {
        this.type = type;
    }

    public abstract String getJavaType();
    
    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        setEntity((HEntity)parent);
    }
}
