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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The information for a field of an entity in the model.
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class FieldInf extends FieldInfBase
{
    @XmlAttribute
    private String column;

    @XmlAttribute
    private Boolean required;

    public FieldInf()
    {
    }

    /**
     * The name of the SQL column of the field.
     * 
     * @return The name of the SQL column of the field.
     */
    public String getColumn()
    {
        if(column == null)
        {
            column = Utils.toSQLName(getName());
        }
        return column;
    }

    /**
     * The name of the SQL column of the field.
     * 
     * @param column The name of the SQL column of the field.
     */
    public void setColumn(String column)
    {
        this.column = column;
    }

    /**
     * The information for the SQL type of the field.
     * 
     * @return The information for the SQL type of the field.
     */
    public abstract SQLTypeInf getType();

    /**
     * If the field is autoincrement.
     * 
     * @return If the field is autoincrement.
     */
    public abstract boolean isAutoIncrement();

    /**
     * The full name of the type of the field.
     * 
     * @return The full name of the type of the field.
     */
    public String getFullTypeName()
    {
        return getEntity().getModel().getName() + "Types." + getType().getName();
    }

    /**
     * If the field is required.
     * 
     * @return If the field is required.
     */
    public Boolean getRequired()
    {
        if(required == null)
        {
            required = false;
        }
        return required;
    }

    /**
     * If the field is required.
     * 
     * @param required If the field is required.
     */
    public void setRequired(Boolean required)
    {
        this.required = required;
    }

    @Override
    public String getJavaType()
    {
        return getType().getJavaType();
    }
    
    /**
     * Gets the name of the class for the column of this field.
     * 
     * @return The name of the class for the column of this field.
     */
    public abstract String getColumnClass();

    /**
     * If this fields needs a custom getter.
     * 
     * @return true this fields needs a custom getter, false otherwise.
     */    
    public boolean getNeedCustomGetter()
    {
        return getParent() instanceof WrapperFieldInf;
    }
}
