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
 * Data common to all entity like classes.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HEntityDataBase
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
     * The name for this object, This value will be used to identify the entity withing the model, it will be the class and xml type name also.
     * @return A String object representing the value of the name field.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * The name for this object, This value will be used to identify the entity withing the model, it will be the class and xml type name also.
     * @param name The new value for the name field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Entity that this object will extends from.
     * @return A String object representing the value of the extendsFrom field.
     */
    public String getExtendsFrom()
    {
        return this.extendsFrom;
    }

    /**
     * Entity that this object will extends from.
     * @param extendsFrom The new value for the extendsFrom field.
     */
    public void setExtendsFrom(String extendsFrom)
    {
        this.extendsFrom = extendsFrom;
    }

    /**
     * The description of this entity.
     * @return A String object representing the value of the description field.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * The description of this entity.
     * @param description The new value for the description field.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * If this entity is customizable or not. If true a base class will be generated for this entity.
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
     * If this entity is customizable or not. If true a base class will be generated for this entity.
     * @param customizable The new value for the customizable field.
     */
    public void setCustomizable(Boolean customizable)
    {
        this.customizable = customizable;
    }

    /**
     * A list of fields for this entity.
     * @return A java.util.List&lt;HField&gt; object representing the value of the fields field.
     */
    public java.util.List<HField> getFields()
    {
        if(this.fields == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.fields);
    }

    /**
     * Adds a HStringField object from the fields list.
     * @param value The object to be added
     */
    public void addString(HStringField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            this.fields = new java.util.ArrayList<>();
        }
        this.fields.add(value);
    }

    /**
     * Removes a HStringField object from the fields list.
     * @param value The object to be removed.
     */
    public void removeString(HStringField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            return;
        }
        this.fields.remove(value);
    }

    /**
     * Adds a HListField object from the fields list.
     * @param value The object to be added
     */
    public void addList(HListField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            this.fields = new java.util.ArrayList<>();
        }
        this.fields.add(value);
    }

    /**
     * Removes a HListField object from the fields list.
     * @param value The object to be removed.
     */
    public void removeList(HListField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            return;
        }
        this.fields.remove(value);
    }

    /**
     * Adds a HEnumField object from the fields list.
     * @param value The object to be added
     */
    public void addEnum(HEnumField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            this.fields = new java.util.ArrayList<>();
        }
        this.fields.add(value);
    }

    /**
     * Removes a HEnumField object from the fields list.
     * @param value The object to be removed.
     */
    public void removeEnum(HEnumField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            return;
        }
        this.fields.remove(value);
    }

    /**
     * Adds a HBooleanField object from the fields list.
     * @param value The object to be added
     */
    public void addBoolean(HBooleanField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            this.fields = new java.util.ArrayList<>();
        }
        this.fields.add(value);
    }

    /**
     * Removes a HBooleanField object from the fields list.
     * @param value The object to be removed.
     */
    public void removeBoolean(HBooleanField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            return;
        }
        this.fields.remove(value);
    }

    /**
     * Adds a HElementField object from the fields list.
     * @param value The object to be added
     */
    public void addElement(HElementField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            this.fields = new java.util.ArrayList<>();
        }
        this.fields.add(value);
    }

    /**
     * Removes a HElementField object from the fields list.
     * @param value The object to be removed.
     */
    public void removeElement(HElementField value)
    {
        if(value == null)
        {
            return;
        }
        if(this.fields == null)
        {
            return;
        }
        this.fields.remove(value);
    }

}