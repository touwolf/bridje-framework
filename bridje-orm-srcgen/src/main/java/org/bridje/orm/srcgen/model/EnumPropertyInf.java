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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * This class defines the information for a property of an enumerator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EnumPropertyInf
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private EnumPropertyType type;

    /**
     * The name of the property.
     * 
     * @return The name of the property.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the property.
     * 
     * @param name The name of the property.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The type of the property.
     * 
     * @return The type of the property.
     */
    public EnumPropertyType getType()
    {
        return type;
    }

    /**
     * The type of the property.
     * 
     * @param type The type of the property.
     */
    public void setType(EnumPropertyType type)
    {
        this.type = type;
    }

    /**
     * The java type for this property.
     * 
     * @return The java type for this property.
     */
    public String getJavaType()
    {
        switch(type)
        {
            case DOUBLE:
                return "Double";
            case INTEGER:
                return "Integer";
            case LONG:
                return "Long";
            default:
                return "String";
        }
    }
}
