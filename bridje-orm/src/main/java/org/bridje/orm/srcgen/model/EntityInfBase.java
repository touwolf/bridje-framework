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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityInfBase
{
    @XmlIDREF
    @XmlAttribute
    private EntityInfTemplate base;
    
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
    
    @XmlTransient
    private List<FieldInfBase> allFields;

    @XmlElementWrapper(name = "operations")
    @XmlElements(
    {
        @XmlElement(name = "create", type = CreateOperationInf.class),
        @XmlElement(name = "read", type = ReadOperationInf.class),
        @XmlElement(name = "update", type = UpdateOperationInf.class),
        @XmlElement(name = "delete", type = DeleteOperationInf.class),
        @XmlElement(name = "deleteEntity", type = DeleteOperationInf.class),
        @XmlElement(name = "save", type = SaveOperationInf.class)
    })
    private List<OperationInfBase> operations;
    
    @XmlTransient
    private List<OperationInfBase> allOperations;

    @XmlTransient
    private ModelInf model;
    
    @XmlTransient
    private FieldInfBase keyField;

    public EntityInfTemplate getBase()
    {
        return base;
    }

    public void setBase(EntityInfTemplate base)
    {
        this.base = base;
    }

    public ModelInf getModel()
    {
        return model;
    }
    
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
        if (allFields == null)
        {
            allFields = new ArrayList<>();
            if(base != null) allFields.addAll(cloneFields(base.getFields()));
            if(fields != null) allFields.addAll(fields);
        }
        return allFields;
    }

    public List<OperationInfBase> getOperations()
    {
        if (allOperations == null)
        {
            allOperations = new ArrayList<>();
            if(base != null) allOperations.addAll(base.getOperations());
            if(operations != null) allOperations.addAll(operations);
        }
        return allOperations;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        model = (ModelInf)parent;
    }
    
    public String getPackage()
    {
        return model.getPackage();
    }
    
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }
    
    public FieldInfBase getKeyField()
    {
        if(keyField == null) keyField = findKeyField();
        return keyField;
    }
    
    public FieldInfBase findKeyField()
    {
        for(FieldInfBase fieldInf : getFields())
        {
            if(fieldInf.isKey()) return fieldInf;
        }
        return null;
    }

    public FieldInfBase findField(String fieldName)
    {
        return getFields().stream()
                        .filter(f -> f.getName().equals(fieldName))
                        .findAny()
                        .orElse(null);
    }

    private List<FieldInfBase> cloneFields(List<FieldInfBase> fields)
    {
        List<FieldInfBase> result = new ArrayList<>();
        for (FieldInfBase field : fields)
        {
            result.add(field.clone(this));
        }
        return result;
    }
}
