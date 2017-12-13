/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.jfx.srcgen.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * Represents an object information mapping.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectInfMapping
{
    @XmlAttribute
    private String target;

    @XmlAttribute
    private String base;

    @XmlElements(
    {
        @XmlElement(name = "property", type = ObjectInfMappingProperty.class)
    })
    private List<ObjectInfMappingProperty> properties;
    
    /**
     * The target object for this mapping.
     * 
     * @return The target object for this mapping.
     */
    public String getTarget()
    {
        return target;
    }

    /**
     * The target object for this mapping.
     * 
     * @param target The target object for this mapping.
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    /**
     * The base object of the mapping object.
     * 
     * @return The base object of the mapping object.
     */
    public String getBase()
    {
        return base;
    }

    /**
     * The base object of the mapping object.
     * 
     * @param base The base object of the mapping object.
     */
    public void setBase(String base)
    {
        this.base = base;
    }
    
    /**
     * The properties to map.
     * 
     * @return The properties to map.
     */
    public List<ObjectInfMappingProperty> getProperties()
    {
        return properties;
    }

    /**
     * The properties to map.
     * 
     * @param properties The properties to map.
     */
    public void setProperties(List<ObjectInfMappingProperty> properties)
    {
        this.properties = properties;
    }
}
