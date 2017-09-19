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
 * 
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
     * 
     * @return 
     */
    public String getTarget()
    {
        return target;
    }

    /**
     * 
     * @param target 
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    /**
     * 
     * @return 
     */
    public String getBase()
    {
        return base;
    }

    /**
     * 
     * @param base 
     */
    public void setBase(String base)
    {
        this.base = base;
    }
    
    /**
     * 
     * @return 
     */
    public List<ObjectInfMappingProperty> getProperties()
    {
        return properties;
    }

    /**
     * 
     * @param properties 
     */
    public void setProperties(List<ObjectInfMappingProperty> properties)
    {
        this.properties = properties;
    }
}
