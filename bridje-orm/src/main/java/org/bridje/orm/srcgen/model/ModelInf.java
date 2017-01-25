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

/**
 * This class represents a ORM model, the information in this object will be use
 * by the source code generation tool to generate the entitys and enumeration
 * classes defined here.
 */
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
                ,
                @XmlElement(name = "external", type = ExternalEnumInf.class)
            })
    private List<EnumBaseInf> enums;

    /**
     * Gets the full name "package.name" for this model.
     *
     * @return
     */
    public String getFullName()
    {
        return packageName + "." + name;
    }

    /**
     * The name of the model.
     *
     * @return The name of the model.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the model.
     *
     * @param name The name of the model.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The package of the model.
     *
     * @return The package of the model.
     */
    public String getPackage()
    {
        return packageName;
    }

    /**
     * The package of the model.
     *
     * @param packageName The package of the model.
     */
    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    /**
     * The prefix used in the model for the names of the tables.
     *
     * @return The prefix used in the model for the names of the tables.
     */
    public String getTablePrefix()
    {
        return tablePrefix;
    }

    /**
     * The prefix used in the model for the names of the tables.
     *
     * @param tablePrefix The prefix used in the model for the names of the
     *                    tables.
     */
    public void setTablePrefix(String tablePrefix)
    {
        this.tablePrefix = tablePrefix;
    }

    /**
     * The default description for the fields of this model.
     * 
     * @return The default description for the fields of this model.
     */
    public String getFieldDescription()
    {
        return fieldDescription;
    }

    /**
     * The default description for the fields of this model.
     * 
     * @param fieldDescription The default description for the fields of this model.
     */
    public void setFieldDescription(String fieldDescription)
    {
        this.fieldDescription = fieldDescription;
    }

    /**
     * The default description for the entities of this model.
     * 
     * @return The default description for the entities of this model.
     */
    public String getEntityDescription()
    {
        return entityDescription;
    }

    /**
     * The default description for the entities of this model.
     *
     * @param entityDescription The default description for the entities of this model.
     */
    public void setEntityDescription(String entityDescription)
    {
        this.entityDescription = entityDescription;
    }

    /**
     * The description of the model.
     *
     * @return The description of the model.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * The description of the model.
     * 
     * @param description The description of the model.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The list of entities for this model.
     *  
     * @return The list of entities for this model.
     */
    public List<EntityInf> getEntities()
    {
        if (entities == null)
        {
            this.entities = new LinkedList<>();
        }
        return entities;
    }

    /**
     * The list of entities templates for this model.
     * 
     * @return The list of entities templates for this model.
     */
    public List<EntityInfTemplate> getTemplates()
    {
        if (templates == null)
        {
            this.templates = new LinkedList<>();
        }
        return templates;
    }

    /**
     * The list of enums for this model.
     * 
     * @return The list of enums for this model.
     */
    public List<EnumBaseInf> getEnums()
    {
        if (enums == null)
        {
            this.enums = new LinkedList<>();
        }
        return enums;
    }

}
