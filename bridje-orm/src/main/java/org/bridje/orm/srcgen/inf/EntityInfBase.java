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

package org.bridje.orm.srcgen.inf;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityInfBase
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlElementWrapper(name = "fields")
    @XmlElements(
    {
        @XmlElement(name = "string", type = StringFieldInf.class),
        @XmlElement(name = "integer", type = IntegerFieldInf.class),
        @XmlElement(name = "decimal", type = DecimalFieldInf.class),
        @XmlElement(name = "enum", type = EnumFieldInf.class),
        @XmlElement(name = "relation", type = RelationFieldInf.class),
        @XmlElement(name = "datetime", type = DateTimeFieldInf.class),
        @XmlElement(name = "custom", type = CustomFieldInf.class)
    })
    private List<FieldInfBase> fields;

    @XmlElementWrapper(name = "operations")
    @XmlElements(
    {
        @XmlElement(name = "create", type = CreateOperationInf.class),
        @XmlElement(name = "read", type = ReadOperationInf.class),
        @XmlElement(name = "update", type = UpdateOperationInf.class),
        @XmlElement(name = "delete", type = DeleteOperationInf.class),
        @XmlElement(name = "save", type = SaveOperationInf.class)
    })
    private List<OperationInfBase> operations;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<FieldInfBase> getFields()
    {
        if(fields == null)
        {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public List<OperationInfBase> getOperations()
    {
        if(operations == null)
        {
            operations = new ArrayList<>();
        }
        return operations;
    }
}
