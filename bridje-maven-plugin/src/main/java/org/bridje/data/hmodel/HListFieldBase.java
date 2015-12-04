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
 * Represents a field that holds a collection of objects.
 */
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public abstract class HListFieldBase extends HField
{
    @javax.xml.bind.annotation.XmlAttribute
    private String of;
    
    @javax.xml.bind.annotation.XmlAttribute
    private Boolean wrapper;
    
    @javax.xml.bind.annotation.XmlElementWrapper(name = "elements")
    @javax.xml.bind.annotation.XmlElements(
    {
        @javax.xml.bind.annotation.XmlElement(name = "element", type = HListElement.class)
    })
    private java.util.List<HListElement> elements;
    
    @javax.xml.bind.annotation.XmlElementWrapper(name = "filters")
    @javax.xml.bind.annotation.XmlElements(
    {
        @javax.xml.bind.annotation.XmlElement(name = "filter", type = HListFilter.class)
    })
    private java.util.List<HListFilter> filters;
    
    /**
     * The type of the list represented by this field.
     * @return A String object representing the value of the of field.
     */
    public String getOf()
    {
        return this.of;
    }

    /**
     * The type of the list represented by this field.
     * @param of The new value for the of field.
     */
    public void setOf(String of)
    {
        this.of = of;
    }

    /**
     * If this fields needs to be wrapped.
     * @return A Boolean object representing the value of the wrapper field.
     */
    public Boolean getWrapper()
    {
        if(this.wrapper == null)
        {
            this.wrapper = false;
        }
        return this.wrapper;
    }

    /**
     * If this fields needs to be wrapped.
     * @param wrapper The new value for the wrapper field.
     */
    public void setWrapper(Boolean wrapper)
    {
        this.wrapper = wrapper;
    }

    /**
     * Elements of the list
     * @return A java.util.List&lt;HListElement&gt; object representing the value of the elements field.
     */
    public java.util.List<HListElement> getElements()
    {
        if(this.elements == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.elements);
    }

    /**
     * Filters for this list
     * @return A java.util.List&lt;HListFilter&gt; object representing the value of the filters field.
     */
    public java.util.List<HListFilter> getFilters()
    {
        if(this.filters == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.filters);
    }

    /**
     * Adds a HListElement object to the elements list.
     * @param value The object to be added
     */
    public void addElement(HListElement value)
    {
        if(value == null)
        {
            return;
        }
        if(this.elements == null)
        {
            this.elements = new java.util.ArrayList<>();
        }
        this.elements.add(value);
    }

    /**
     * Removes a HListElement object from the elements list.
     * @param value The object to be removed.
     */
    public void removeElement(HListElement value)
    {
        if(value == null)
        {
            return;
        }
        if(this.elements == null)
        {
            return;
        }
        this.elements.remove(value);
    }

    /**
     * Adds a HListFilter object to the filters list.
     * @param value The object to be added
     */
    public void addFilter(HListFilter value)
    {
        if(value == null)
        {
            return;
        }
        if(this.filters == null)
        {
            this.filters = new java.util.ArrayList<>();
        }
        this.filters.add(value);
    }

    /**
     * Removes a HListFilter object from the filters list.
     * @param value The object to be removed.
     */
    public void removeFilter(HListFilter value)
    {
        if(value == null)
        {
            return;
        }
        if(this.filters == null)
        {
            return;
        }
        this.filters.remove(value);
    }

    @javax.xml.bind.annotation.XmlTransient
    private HEntityData entity;

    /**
     * The HEntityData parent of this object.
     * @return The HEntityData object representing the parent of this object.
     */
    public HEntityData getEntity()
    {
        return this.entity;
    }

    /**
     * The HEntityData object representing the parent of this object.
     */
    void setEntity(HEntityData entity)
    {
        this.entity = entity;
    }

    /**
     * This method is call by the JAXB Unmarshaller after the object's creation.
     * @param unmarshaller The unmarshaller object being used.
     * @param parent The parent object for this object.
     */
    public void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, Object parent)
    {
        if(parent instanceof HEntityData)
        {
            setEntity((HEntityData)parent);
        }
    }

}