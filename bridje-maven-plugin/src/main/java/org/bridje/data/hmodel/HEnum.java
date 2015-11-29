/**
 * 
 * Copyright 2015 Bridje Framework.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *     
 */

package org.bridje.data.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.Unmarshaller;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEnum
{
    @XmlAttribute
    private String name;
    
    private String description;
    
    @XmlElements(
    {
        @XmlElement(name = "value", type = HEnumValue.class)
    })
    private java.util.List<HEnumValue> values;
    
    /**
     * 
     * @return A String object representing the value of the name field.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * @param name The new value for the name field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * 
     * @return A String object representing the value of the description field.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * 
     * @param description The new value for the description field.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * 
     * @return A java.util.List&lt;HEnumValue&gt; object representing the value of the values field.
     */
    public java.util.List<HEnumValue> getValues()
    {
        return this.values;
    }

    /**
     * 
     * @param values The new value for the values field.
     */
    public void setValues(java.util.List<HEnumValue> values)
    {
        this.values = values;
    }

    @XmlTransient
    private HModel model;

    /**
     * The HModel object representing the parent of this object.
     */
    public HModel getModel()
    {
        return this.model;
    }

    /**
     * The HModel object representing the parent of this object.
     */
    void setModel(HModel model)
    {
        this.model = model;
    }

    /**
     * This method is call by the JAXB Unmarshaller after the object's creation.
     * @param unmarshaller The unmarshaller object being used.
     * @param parent The parent object for this object.
     */
    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        if(parent instanceof HModel)
        {
            setModel((HModel)parent);
        }
    }

}