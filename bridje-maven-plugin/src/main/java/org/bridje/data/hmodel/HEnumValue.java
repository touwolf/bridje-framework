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
 * An enumerator value, this class defines the constans of the object representing by the HEnum object.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEnumValue
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private String description;
    
    /**
     * The name of the value.
     * @return A String object representing the value of the name field.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * The name of the value.
     * @param name The new value for the name field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The description of the value.
     * @return A String object representing the value of the description field.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * The description of the value.
     * @param description The new value for the description field.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

}