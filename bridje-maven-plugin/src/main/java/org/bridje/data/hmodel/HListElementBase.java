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
 * An element of a list field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HListElementBase
{
    @XmlAttribute
    private String type;
    
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private String description;
    
    /**
     * The entity or class this element maps to.
     * @return A String object representing the value of the type field.
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * The entity or class this element maps to.
     * @param type The new value for the type field.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * The name of the element.
     * @return A String object representing the value of the name field.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * The name of the element.
     * @param name The new value for the name field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * If this field is required.
     * @return A String object representing the value of the description field.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * If this field is required.
     * @param description The new value for the description field.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    @XmlTransient
    private HListField listField;

    /**
     * The HListField object representing the parent of this object.
     */
    public HListField getListField()
    {
        return this.listField;
    }

    /**
     * The HListField object representing the parent of this object.
     */
    void setListField(HListField listField)
    {
        this.listField = listField;
    }

    /**
     * This method is call by the JAXB Unmarshaller after the object's creation.
     * @param unmarshaller The unmarshaller object being used.
     * @param parent The parent object for this object.
     */
    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        if(parent instanceof HListField)
        {
            setListField((HListField)parent);
        }
    }

}