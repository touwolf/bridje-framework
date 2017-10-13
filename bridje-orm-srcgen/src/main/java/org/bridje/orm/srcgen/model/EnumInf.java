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
 * This class defines an enumerator, the source code generator will generate an
 * enumerator class base on the information provided by each instance of this
 * object.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EnumInf extends EnumBaseInf
{
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
    @XmlElements(
            {
                @XmlElement(name = "property", type = EnumPropertyInf.class)
            })
    private List<EnumPropertyInf> properties;

    /**
     * The description of the enumerator.
     *
     * @return The description of the enumerator.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * The description of the enumerator.
     *
     * @param description The description of the enumerator.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Determines if the description must be a property of the enumerator.
     *
     * @return true the description must be a property of the enumerator, false
     *         otherwise.
     */
    public Boolean getDescriptionAsProperty()
    {
        if (descriptionAsProperty == null)
        {
            descriptionAsProperty = false;
        }
        return descriptionAsProperty;
    }

    /**
     * Determines if the description must be a property of the enumerator.
     *
     * @param descriptionAsProperty true the description must be a property of
     *                              the enumerator, false otherwise.
     */
    public void setDescriptionAsProperty(Boolean descriptionAsProperty)
    {
        this.descriptionAsProperty = descriptionAsProperty;
    }

    /**
     * Gets the list of constants that this enumerator have.
     *
     * @return The list of constants that this enumerator has.
     */
    public List<EnumConstantInf> getConstants()
    {
        if (constants == null)
        {
            constants = new ArrayList<>();
        }
        return constants;
    }

    /**
     * Gets the list of properties that the constants of this enumerator have.
     *
     * @return The list of properties that the constants of this enumerator
     *         have.
     */
    public List<EnumPropertyInf> getProperties()
    {
        if (properties == null)
        {
            properties = new ArrayList<>();
        }
        return properties;
    }

    /**
     * The name of the package for this enumerator.
     * 
     * @return The name of the package for this enumerator.
     */
    @Override
    public String getPackage()
    {
        return getModel().getPackage();
    }
}
