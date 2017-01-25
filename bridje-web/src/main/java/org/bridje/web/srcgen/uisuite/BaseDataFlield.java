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

package org.bridje.web.srcgen.uisuite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A base class for all the data fields that can be defined in the controls.
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseDataFlield implements FieldDef
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;

    @XmlAttribute(name = "def")
    private String defaultValue;

    /**
     * The name of the field.
     * 
     * @return The name of the field.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the field.
     * 
     * @param name The name of the field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The java type for this field.
     * 
     * @return The java type for this field.
     */
    public String getType()
    {
        return type;
    }

    /**
     * The java type for this field.
     * 
     * @param type The java type for this field.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * The default value to be use when the expression evaluates to null.
     * 
     * @return The default value to be use when the expression evaluates to null.
     */
    public String getDefaultValue()
    {
        if (defaultValue == null || defaultValue.trim().isEmpty())
        {
            return "null";
        }
        return defaultValue;
    }

    /**
     * The default value to be use when the expression evaluates to null.
     * 
     * @param defaultValue The default value to be use when the expression evaluates to null.
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean getIsChild()
    {
        return false;
    }

    @Override
    public boolean getIsEvent()
    {
        return false;
    }
}
