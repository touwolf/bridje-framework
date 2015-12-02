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

package org.bridje.data.rmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.Unmarshaller;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class REntity
{
    @XmlElements(
    {
        @XmlElement(name = "string", type = RStringField.class)
    })
    private java.util.List<RStringField> fields;
    
    /**
     * 
     * @return A java.util.List&lt;RStringField&gt; object representing the value of the fields field.
     */
    public java.util.List<RStringField> getFields()
    {
        if(this.fields == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.fields);
    }

    /**
     * Adds a RStringField object to the fields list.
     * @param value The object to be added
     */
    public void addString(RStringField value)
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
     * Removes a RStringField object from the fields list.
     * @param value The object to be removed.
     */
    public void removeString(RStringField value)
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