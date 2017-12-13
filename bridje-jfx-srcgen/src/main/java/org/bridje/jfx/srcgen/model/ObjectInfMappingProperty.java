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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * A property mapping information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectInfMappingProperty
{
    @XmlAttribute
    private String source;

    @XmlAttribute
    private String target;

    @XmlAttribute
    private String type;
    
    /**
     * The source property.
     * 
     * @return The source property.
     */
    public String getSource()
    {
        if(source == null && target != null)
        {
            return target;
        }
        return source;
    }

    /**
     * The source property.
     * 
     * @param source The source property.
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    /**
     * The target property.
     * 
     * @return The target property.
     */
    public String getTarget()
    {
        return target;
    }

    /**
     * The target property.
     * 
     * @param target The target property.
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    /**
     * The type of the property.
     * 
     * @return The type of the property.
     */
    public String getType()
    {
        return type;
    }

    /**
     * The type of the property.
     * 
     * @param type The type of the property.
     */
    public void setType(String type)
    {
        this.type = type;
    }
}
