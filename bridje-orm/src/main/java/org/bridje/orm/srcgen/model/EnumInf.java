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

/**
 * This class defines an enumerator, the source code generator will generate an
 * enumerator class base on the information provided by each instance of this
 * object.
 */
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
    @XmlElements(
            {
                @XmlElement(name = "property", type = EnumPropertyInf.class)
            })
    private List<EnumPropertyInf> properties;

    @XmlTransient
    private ModelInf model;

    /**
     * The model that this enumerator belongs to.
     *
     * @return The model that this enumerator belongs to.
     */
    public ModelInf getModel()
    {
        return model;
    }

    /**
     * The name of the enumerator.
     *
     * @return The name of the enumerator.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the enumerator.
     *
     * @param name The name of the enumerator.
     */
    public void setName(String name)
    {
        this.name = name;
    }

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
     * Called by JAXB after unmarshal.
     *
     * @param u      The unmarshaller.
     * @param parent The parent object.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        model = (ModelInf) parent;
    }

    /**
     * The name of the package for this enumerator.
     * 
     * @return The name of the package for this enumerator.
     */
    public String getPackage()
    {
        return model.getPackage();
    }

    /**
     * The full java class name for this enumerator.
     * 
     * @return The full java class name for this enumerator.
     */
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }
}
