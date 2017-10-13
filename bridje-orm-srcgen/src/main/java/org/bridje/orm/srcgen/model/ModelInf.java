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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.ioc.Ioc;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.VFile;

@XmlRootElement(name = "ormModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelInf
{
    private static final Logger LOG = Logger.getLogger(ModelInf.class.getName());

    @XmlAttribute
    private String schema;

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "package")
    private String packageName;

    @XmlAttribute(name = "description")
    private String description;

    @XmlElementWrapper(name = "includes")
    @XmlElements(
    {
        @XmlElement(name = "include", type = String.class)
    })
    private List<String> includes;

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

    @XmlElementWrapper(name = "enums")
    @XmlElements(
            {
                @XmlElement(name = "enum", type = EnumInf.class),
                @XmlElement(name = "external", type = ExternalEnumInf.class)
            })
    private List<EnumBaseInf> enums;

    @XmlTransient
    private List<ModelInf> includeModels;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSchema()
    {
        if(schema == null)
        {
            schema = Utils.toSQLName(name);
        }
        return schema;
    }

    public void setSchema(String schema)
    {
        this.schema = schema;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<EnumBaseInf> getEnums()
    {
        return enums;
    }

    public void setEnums(List<EnumBaseInf> enums)
    {
        this.enums = enums;
    }

    public List<String> getIncludes()
    {
        if(includes == null)
        {
            includes = new ArrayList<>();
        }
        return includes;
    }

    public void setIncludes(List<String> includes)
    {
        this.includes = includes;
    }

    public SQLTypeInf findSQLType(String type)
    {
        SQLTypeInf result = types.stream()
                .filter(t -> t.getName().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);
        if(result == null)
        {
            for (ModelInf model : getIncludeModels())
            {
                result = model.findSQLType(type);
                if(result != null) return result;
            }
        }
        return result;
    }

    public List<ModelInf> getIncludeModels()
    {
        SrcGenService srcGen = Ioc.context().find(SrcGenService.class);
        if(includeModels == null)
        {
            includeModels = new ArrayList<>();
            for (String include : getIncludes())
            {
                try
                {
                    ModelInf model = srcGen.readFile(new VFile(SrcGenService.SUPL_PATH.join(include)), ModelInf.class);
                    includeModels.add(model);
                }
                catch (IOException e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return includeModels;
    }

    public EntityInf findEntity(String referencesName)
    {
        return entities.stream()
                .filter(t -> t.getName().equalsIgnoreCase(referencesName))
                .findFirst()
                .orElse(null);
    }
}
