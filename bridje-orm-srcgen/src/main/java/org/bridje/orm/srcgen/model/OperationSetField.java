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

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This object represents a set field value for an operation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OperationSetField
{
    @XmlAttribute(name = "field")
    private String fieldName;

    @XmlAttribute(name = "value")
    private String value;

    @XmlAttribute(name = "ifNull")
    private Boolean ifNull;
    
    @XmlTransient
    private FieldInfBase field;

    @XmlTransient
    private OperationInfBase operation;

    /**
     * The field to set.
     * 
     * @return The field to set.
     */
    public FieldInfBase getField()
    {
        if(field == null)
        {
            field = operation.getEntity().findField(fieldName);
        }
        return field;
    }

    /**
     * The name of the field.
     * 
     * @return The name of the field.
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * The name of the field.
     * 
     * @param fieldName The name of the field.
     */
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    /**
     * The operation for this object.
     * 
     * @return The operation for this object.
     */
    public OperationInfBase getOperation()
    {
        return operation;
    }

    /**
     * The value to set
     * 
     * @return The value to set
     */
    public String getValue()
    {
        return value;
    }

    /**
     * The value to set
     * 
     * @param value The value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * This value will be set only if the value of the field is null.
     * 
     * @return true the object will be set only if is null false otherwise.
     */
    public Boolean getIfNull()
    {
        if(ifNull == null) return false;
        return ifNull;
    }

    /**
     * This value will be set only if the value of the field is null.
     * 
     * @param ifNull true the object will be set only if is null false otherwise.
     */
    public void setIfNull(Boolean ifNull)
    {
        this.ifNull = ifNull;
    }
    
    /**
     * Called by JAXB after unmarshal.
     * 
     * @param u The unmarshaller.
     * @param parent The parent object.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        operation = (OperationInfBase)parent;
    }

    /**
     * Clones this object into a new OperationSetField.
     * 
     * @param operation The base operation for the new object.
     * @return The cloned object.
     */
    public OperationSetField clone(OperationInfBase operation)
    {
        OperationSetField res = new OperationSetField();
        res.operation = this.operation;
        res.fieldName = this.fieldName;
        res.value = this.value;
        
        return res;
    }
}
