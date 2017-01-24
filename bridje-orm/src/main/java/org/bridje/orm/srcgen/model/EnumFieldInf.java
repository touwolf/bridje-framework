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
import javax.xml.bind.annotation.XmlIDREF;

/**
 * A field hows type is an enumerator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EnumFieldInf extends FieldInfBase
{
    @XmlIDREF
    @XmlAttribute(name = "type")
    private EnumBaseInf type;

    @XmlAttribute
    private EnumFieldSQLType sqlType;

    @XmlAttribute
    private Integer length;

    /**
     * The enumerator for this field.
     * 
     * @return The enumerator for this field.
     */
    public EnumBaseInf getType()
    {
        return type;
    }

    /**
     * The enumerator for this field.
     * 
     * @param type The enumerator for this field.
     */
    public void setType(EnumBaseInf type)
    {
        this.type = type;
    }

    /**
     * The length of this SQL column for this field.
     * 
     * @return The length of this SQL column for this field.
     */
    public Integer getLength()
    {
        return length;
    }

    /**
     * The length of this SQL column for this field.
     * 
     * @param length The length of this SQL column for this field.
     */
    public void setLength(Integer length)
    {
        this.length = length;
    }

    /**
     * The SQL type for this field.
     * 
     * @return The SQL type for this field.
     */
    public EnumFieldSQLType getSqlType()
    {
        if(sqlType == null) return EnumFieldSQLType.BYTE;
        return sqlType;
    }

    /**
     * The SQL type for this field.
     * 
     * @param sqlType The SQL type for this field.
     */
    public void setSqlType(EnumFieldSQLType sqlType)
    {
        this.sqlType = sqlType;
    }

    @Override
    public String getJavaType()
    {
        if(!getType().getPackage().equalsIgnoreCase(getEntity().getPackage()))
        {
            return getType().getFullName();
        }
        return getType().getName();
    }

    @Override
    public FieldInfBase clone(EntityInfBase entity)
    {
        EnumFieldInf result = new EnumFieldInf();
        clone(result, entity);
        result.type = type;
        result.sqlType = sqlType;
        result.length = length;
        return result;
    }
}
