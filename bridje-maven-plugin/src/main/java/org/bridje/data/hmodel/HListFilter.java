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

/**
 * 
 */
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class HListFilter
{
    @javax.xml.bind.annotation.XmlAttribute
    private String name;
    
    @javax.xml.bind.annotation.XmlAttribute
    private Boolean unique;
    
    @javax.xml.bind.annotation.XmlElements(
    {
        @javax.xml.bind.annotation.XmlElement(name = "by", type = HListFilterField.class)
    })
    private java.util.List<HListFilterField> fields;
    
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
     * @return A Boolean object representing the value of the unique field.
     */
    public Boolean getUnique()
    {
        if(this.unique == null)
        {
            this.unique = false;
        }
        return this.unique;
    }

    /**
     * 
     * @param unique The new value for the unique field.
     */
    public void setUnique(Boolean unique)
    {
        this.unique = unique;
    }

    /**
     * 
     * @return A java.util.List&lt;HListFilterField&gt; object representing the value of the fields field.
     */
    public java.util.List<HListFilterField> getFields()
    {
        if(this.fields == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.fields);
    }

    /**
     * Adds a HListFilterField object to the fields list.
     * @param value The object to be added
     */
    public void addBy(HListFilterField value)
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
     * Removes a HListFilterField object from the fields list.
     * @param value The object to be removed.
     */
    public void removeBy(HListFilterField value)
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