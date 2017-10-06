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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ormModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelInf
{
    @XmlAttribute
    private String name;

    @XmlAttribute(name = "package")
    private String packageName;
    
    @XmlElementWrapper(name = "types")
    @XmlElements(
    {
        @XmlElement(name = "type", type = SQLTypeInf.class)
    })
    private List<SQLTypeInf> types;

    @XmlElementWrapper(name = "entities")
    @XmlElements(
    {
        @XmlElement(name = "entity", type = EntityInf.class)
    })
    private List<EntityInf> entities;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPackage()
    {
        return packageName;
    }

    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    public String getFullName()
    {
        return this.packageName + "." + this.name;
    }

    public List<SQLTypeInf> getTypes()
    {
        return types;
    }

    public void setTypes(List<SQLTypeInf> types)
    {
        this.types = types;
    }

    public List<EntityInf> getEntities()
    {
        return entities;
    }

    public void setEntities(List<EntityInf> entities)
    {
        this.entities = entities;
    }

    public SQLTypeInf findSQLType(String type)
    {
        return types.stream()
                .filter(t -> t.getName().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);
    }
}
