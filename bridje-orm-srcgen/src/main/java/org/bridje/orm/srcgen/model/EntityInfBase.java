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
import java.util.Collection;
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

/**
 * Base class for entitys and templates. This class holds the commons fields of
 * the entities.
 */
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
        @XmlElement(name = "custom", type = CustomFieldInf.class),
        @XmlElement(name = "boolean", type = BooleanFieldInf.class)
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
                @XmlElement(name = "deleteEntity", type = DeleteEntityOperationInf.class),
                @XmlElement(name = "save", type = SaveOperationInf.class)
            })
    private List<OperationInfBase> operations;

    @XmlTransient
    private List<OperationInfBase> allOperations;

    @XmlTransient
    private ModelInf model;

    @XmlTransient
    private FieldInfBase keyField;

    /**
     * The base template for this entity.
     *
     * @return The base template for this entity.
     */
    public EntityInfTemplate getBase()
    {
        return base;
    }

    /**
     * The base template for this entity.
     *
     * @param base The base template for this entity.
     */
    public void setBase(EntityInfTemplate base)
    {
        this.base = base;
    }

    /**
     * The ORM model that this entity belongs to.
     *
     * @return The ORM model that this entity belongs to.
     */
    public ModelInf getModel()
    {
        return model;
    }

    /**
     * The name of this entity.
     *
     * @return The name of this entity.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of this entity.
     *
     * @param name The name of this entity.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * A description for this entity.
     *
     * @return A description for this entity.
     */
    public String getDescription()
    {
        if(description == null || description.trim().isEmpty())
        {
            return model.getEntityDescription();
        }
        return description;
    }

    /**
     * A description for this entity.
     *
     * @param description A description for this entity.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The fields that must be generated for this entity.
     *
     * @return The fields that must be generated for this entity.
     */
    public List<FieldInfBase> getFields()
    {
        if (allFields == null)
        {
            allFields = new ArrayList<>();
            if (base != null)
            {
                allFields.addAll(cloneFields(base.getFields()));
            }
            if (fields != null)
            {
                allFields.addAll(fields);
            }
        }
        return allFields;
    }

    /**
     * The operations that must be generated for this entity.
     *
     * @return The operations that must be generated for this entity.
     */
    public List<OperationInfBase> getOperations()
    {
        if (allOperations == null)
        {
            allOperations = new ArrayList<>();
            if (base != null)
            {
                allOperations.addAll(cloneOperations(base.getOperations()));
            }
            if (operations != null)
            {
                allOperations.addAll(operations);
            }
        }
        return allOperations;
    }

    /**
     * Called by JDBC after the unmarshalling has hapend for this object.
     *
     * @param u The unmarshaller.
     * @param parent The parent of this object.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        model = (ModelInf) parent;
    }

    /**
     * Gets the java package that this entity belongs to.
     * 
     * @return The java package that this entity belongs to.
     */
    public String getPackage()
    {
        return model.getPackage();
    }

    /**
     * The full java name of the entity´s class.
     * 
     * @return The full java name of the entity´s class.
     */
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    /**
     * The key field for this entity.
     * 
     * @return The key field for this entity.
     */
    public FieldInfBase getKeyField()
    {
        if (keyField == null)
        {
            keyField = findKeyField();
        }
        return keyField;
    }

    private FieldInfBase findKeyField()
    {
        for (FieldInfBase fieldInf : getFields())
        {
            if (fieldInf.getKey())
            {
                return fieldInf;
            }
        }
        return null;
    }

    /**
     * Finds a field of this entity that match the given name.
     * 
     * @param fieldName The name of the field to find.
     * @return The field finded or null if none could be found.
     */
    public FieldInfBase findField(String fieldName)
    {
        return getFields().stream()
                .filter(f -> f.getName().equals(fieldName))
                .findAny()
                .orElse(null);
    }

    /**
     * Gets only the declared fields in this entity.
     * 
     * @return Gets only the declared fields in this entity.
     */
    public List<FieldInfBase> getDeclaredFields()
    {
        return fields;
    }

    /**
     * Gets only the declared operations in this entity.
     * 
     * @return Gets only the declared operations in this entity.
     */
    public List<OperationInfBase> getDeclaredOperations()
    {
        return operations;
    }

    private List<FieldInfBase> cloneFields(List<FieldInfBase> fields)
    {
        List<FieldInfBase> result = new ArrayList<>();
        fields.forEach(f -> result.add(f.clone(this)));
        return result;
    }

    private Collection<? extends OperationInfBase> cloneOperations(List<OperationInfBase> operations)
    {
        List<OperationInfBase> result = new ArrayList<>();
        operations.forEach(op -> result.add(op.clone(this)));
        return result;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
