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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class EnumInf
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private Boolean descriptionAsProperty;

    @XmlElements(
    {
        @XmlElement(name = "constant", type = EnumConstantInf.class)
    })
    private List<EnumConstantInf> constants;

    @XmlElementWrapper(name = "properties")
    @XmlElements({
        @XmlElement(name = "property", type = EnumPropertyInf.class)
    })
    private List<EnumPropertyInf> properties;

    @XmlTransient
    private ModelInf model;

    public ModelInf getModel()
    {
        return model;
    }

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

    public Boolean getDescriptionAsProperty()
    {
        if (descriptionAsProperty == null)
        {
            descriptionAsProperty = false;
        }
        return descriptionAsProperty;
    }

    public void setDescriptionAsProperty(Boolean descriptionAsProperty)
    {
        this.descriptionAsProperty = descriptionAsProperty;
    }

    public List<EnumConstantInf> getConstants()
    {
        if(constants == null)
        {
            constants = new ArrayList<>();
        }
        return constants;
    }

    public List<EnumPropertyInf> getProperties()
    {
        if (properties == null)
        {
            properties = new ArrayList<>();
        }
        return properties;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        model = (ModelInf)parent;
    }
    
    public String getPackage()
    {
        return model.getPackage();
    }
    
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }
}
