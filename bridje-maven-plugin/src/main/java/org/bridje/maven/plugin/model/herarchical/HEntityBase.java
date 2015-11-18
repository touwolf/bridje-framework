/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.maven.plugin.model.herarchical;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 *
 * @author Gilberto
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEntityBase
{
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "extends")
    private String extendsFrom;
    
    @XmlElements(
    {
        @XmlElement(name = "string", type = HStringField.class),
        @XmlElement(name = "list", type = HListField.class),
        @XmlElement(name = "enum", type = HEnumField.class),
        @XmlElement(name = "boolean", type = HBooleanField.class)
    })
    private List<HFieldAccess> fields;

    @XmlElements(
    {
        @XmlElement(name = "calc", type = HCalcField.class)
    })
    private List<HCalcField> calcFields;

    
    public List<HFieldAccess> getFields()
    {
        return fields;
    }

    public void setFields(List<HFieldAccess> fields)
    {
        this.fields = fields;
    }
        
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getExtends()
    {
        return extendsFrom;
    }

    public void setExtends(String extendsFrom)
    {
        this.extendsFrom = extendsFrom;
    }

    public List<HCalcField> getCalcFields()
    {
        return calcFields;
    }

    public void setCalcFields(List<HCalcField> calcFields)
    {
        this.calcFields = calcFields;
    }
}
