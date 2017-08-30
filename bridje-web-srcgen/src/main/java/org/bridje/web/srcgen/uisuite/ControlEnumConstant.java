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

/**
 * A constant for a control enumerator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ControlEnumConstant
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String value;
    
    @XmlAttribute
    private String description;

    /**
     * Name of this constant.
     * 
     * @return Name of this constant.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Name of this constant.
     * 
     * @param name Name of this constant.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Value of this constant.
     * 
     * @return Value of this constant.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Value of this constant.
     * 
     * @param value Value of this constant.
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Description for this constant.
     * 
     * @return Description for this constant.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Description for this constant.
     * 
     * @param description Description for this constant.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}
