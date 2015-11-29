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
public class HEntityData
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private String extendsFrom;
    
    private String description;
    
    @XmlAttribute
    private Boolean customizable;
    
    @XmlElements(
    {
        @XmlElement(name = "string", type = HStringField.class), 
        @XmlElement(name = "list", type = HListField.class), 
        @XmlElement(name = "enum", type = HEnumField.class), 
        @XmlElement(name = "boolean", type = HBooleanField.class), 
        @XmlElement(name = "element", type = HElementField.class)
    })
    private java.util.List<HField> fields;
    
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
     * @return A String object representing the value of the extendsFrom field.
     */
    public String getExtendsFrom()
    {
        return this.extendsFrom;
    }

    /**
     * 
     * @param extendsFrom The new value for the extendsFrom field.
     */
    public void setExtendsFrom(String extendsFrom)
    {
        this.extendsFrom = extendsFrom;
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
     * @return A Boolean object representing the value of the customizable field.
     */
    public Boolean getCustomizable()
    {
        if(this.customizable == null)
        {
            this.customizable = false;
        }
        return this.customizable;
    }

    /**
     * 
     * @param customizable The new value for the customizable field.
     */
    public void setCustomizable(Boolean customizable)
    {
        this.customizable = customizable;
    }

    /**
     * 
     * @return A java.util.List&lt;HField&gt; object representing the value of the fields field.
     */
    public java.util.List<HField> getFields()
    {
        return this.fields;
    }

    /**
     * 
     * @param fields The new value for the fields field.
     */
    public void setFields(java.util.List<HField> fields)
    {
        this.fields = fields;
    }

}