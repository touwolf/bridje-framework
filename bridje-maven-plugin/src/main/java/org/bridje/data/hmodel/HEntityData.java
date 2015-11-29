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
<<<<<<< HEAD:bridje-maven-plugin/src/main/java/org/bridje/data/hmodel/HEntityData.java
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
=======
>>>>>>> 25da036fbb102bdcae228f4b9efceba42a9d2fd0:bridje-maven-plugin/src/main/java/org/bridje/maven/plugin/hmodel/HEntityData.java
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEntityData
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private String extendsFrom;
    
    @XmlAttribute
    private Boolean customizable;
    
    @XmlElements(
    {
        @XmlElement(name = "string", type = HStringField.class), 
        @XmlElement(name = "list", type = HListField.class), 
        @XmlElement(name = "enum", type = HEnumField.class), 
<<<<<<< HEAD:bridje-maven-plugin/src/main/java/org/bridje/data/hmodel/HEntityData.java
        @XmlElement(name = "boolean", type = HBooleanField.class), 
=======
        @XmlElement(name = "boolean", type = HBooleanField.class),
>>>>>>> 25da036fbb102bdcae228f4b9efceba42a9d2fd0:bridje-maven-plugin/src/main/java/org/bridje/maven/plugin/hmodel/HEntityData.java
        @XmlElement(name = "element", type = HElementField.class)
    })
    private java.util.List<HField> fields;
    
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getExtendsFrom()
    {
        return this.extendsFrom;
    }

    public void setExtendsFrom(String extendsFrom)
    {
        this.extendsFrom = extendsFrom;
    }

    public Boolean getCustomizable()
    {
        if(this.customizable == null)
        {
            this.customizable = false;
        }
        return this.customizable;
    }

    public void setCustomizable(Boolean customizable)
    {
        this.customizable = customizable;
    }

    public java.util.List<HField> getFields()
    {
        return this.fields;
    }

    public void setFields(java.util.List<HField> fields)
    {
        this.fields = fields;
    }

}