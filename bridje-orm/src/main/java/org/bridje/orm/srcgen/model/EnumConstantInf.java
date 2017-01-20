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
import javax.xml.bind.annotation.*;

/**
 * THis class represents an enumerator constant.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EnumConstantInf
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlElements({
        @XmlElement(name = "property", type = EnumConstantProperty.class)
    })
    private List<EnumConstantProperty> properties;

    /**
     * The name of the constant.
     * 
     * @return The name of the constant.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the constant.
     * 
     * @param name The name of the constant.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The description of the constant.
     * 
     * @return The description of the constant.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * The description of the constant.
     * 
     * @param description The description of the constant.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The properties values of the enumerator for this constant.
     * 
     * @return The properties values of the enumerator for this constant.
     */
    public List<EnumConstantProperty> getProperties()
    {
        if (properties == null)
        {
            properties = new ArrayList<>();
        }
        return properties;
    }

    /**
     * Gets the value of the given property for this constant.
     * 
     * @param propertyName The name of the property to get.
     * @return The value of the given property for this constant.
     */
    public String propertyValue(String propertyName)
    {
        if (propertyName != null)
        {
            for (EnumConstantProperty property : getProperties())
            {
                if (propertyName.equals(property.getName()))
                {
                    return property.getValue();
                }
            }
        }
        return "";
    }
}
