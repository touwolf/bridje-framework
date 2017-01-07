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

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelInf
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlAttribute(name = "package")
    private String packageName;

    @XmlAttribute
    private String tablePrefix;

    @XmlAttribute
    private String fieldDescription;

    @XmlAttribute
    private String entityDescription;

    @XmlElementWrapper(name = "entities")
    @XmlElements(
    {
        @XmlElement(name = "entity", type = EntityInf.class)
    })
    private List<EntityInf> entities;

    @XmlElementWrapper(name = "templates")
    @XmlElements(
    {
        @XmlElement(name = "template", type = EntityInfTemplate.class)
    })
    private List<EntityInfTemplate> templates;

    @XmlElementWrapper(name = "enums")
    @XmlElements(
    {
        @XmlElement(name = "enum", type = EnumInf.class)
    })
    private List<EnumInf> enums;

    public String getFullName()
    {
        return packageName + "." + name;
    }

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

    public String getTablePrefix()
    {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix)
    {
        this.tablePrefix = tablePrefix;
    }

    public String getFieldDescription()
    {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription)
    {
        this.fieldDescription = fieldDescription;
    }

    public String getEntityDescription()
    {
        return entityDescription;
    }

    public void setEntityDescription(String entityDescription)
    {
        this.entityDescription = entityDescription;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<EntityInf> getEntities()
    {
        if(entities == null)
        {
            this.entities = new LinkedList<>();
        }
        return entities;
    }

    public List<EntityInfTemplate> getTemplates()
    {
        if(templates == null)
        {
            this.templates = new LinkedList<>();
        }
        return templates;
    }

    public List<EnumInf> getEnums()
    {
        if(enums == null)
        {
            this.enums = new LinkedList<>();
        }
        return enums;
    }
}
