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

/**
 * This class represents a property value for an enumerator constant.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EnumConstantProperty
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String value;

    /**
     * The name of the enumerator property.
     *
     * @return The name of the enumerator property.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the enumerator property.
     *
     * @param name The name of the enumerator property.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The value of the enumerator property for this constant.
     *
     * @return The value of the enumerator property for this constant.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * The value of the enumerator property for this constant.
     *
     * @param value The value of the enumerator property for this constant.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}
