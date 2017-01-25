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

/**
 * This class represents a field hows type is an integer type field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerFieldInf extends FieldInfBase
{
    @XmlAttribute
    private IntegerFieldType type;

    @XmlAttribute
    private IntegerFieldSQLType sqlType;

    @XmlAttribute
    private Integer length;

    private Boolean autoIncrement;

    /**
     * The actual integer java type for this field.
     * 
     * @return The actual integer java type for this field.
     */
    public IntegerFieldType getType()
    {
        if (type == null)
        {
            return IntegerFieldType.INTEGER;
        }
        return type;
    }

    /**
     * The actual integer java type for this field.
     * 
     * @param type The actual integer java type for this field.
     */
    public void setType(IntegerFieldType type)
    {
        this.type = type;
    }

    /**
     * The SQL type for this field.
     * 
     * @return The SQL type for this field.
     */
    public IntegerFieldSQLType getSqlType()
    {
        return sqlType;
    }

    /**
     * The SQL type for this field.
     * 
     * @param sqlType The SQL type for this field.
     */
    public void setSqlType(IntegerFieldSQLType sqlType)
    {
        this.sqlType = sqlType;
    }

    /**
     * The columns length for this field.
     * 
     * @return The columns length for this field.
     */
    public Integer getLength()
    {
        return length;
    }

    /**
     * The columns length for this field.
     * 
     * @param length The columns length for this field.
     */
    public void setLength(Integer length)
    {
        this.length = length;
    }

    /**
     * If this field column´s must be mark as auto increment.
     * 
     * @return If this field column´s must be mark as auto increment.
     */
    public boolean getAutoIncrement()
    {
        if (autoIncrement == null)
        {
            return false;
        }
        return autoIncrement;
    }

    /**
     * If this field column´s must be mark as auto increment.
     * 
     * @param autoIncrement If this field column´s must be mark as auto increment.
     */
    public void setAutoIncrement(Boolean autoIncrement)
    {
        this.autoIncrement = autoIncrement;
    }

    @Override
    public String getJavaType()
    {
        switch (getType())
        {
            case BIGINTEGER:
                return "BigInteger";
            case BYTE:
                return "Byte";
            case CHARACTER:
                return "Character";
            case INTEGER:
                return "Integer";
            case LONG:
                return "Long";
            case SHORT:
                return "Short";
        }
        return "Integer";
    }

    @Override
    public String getTableColumn()
    {
        return "TableNumberColumn";
    }

    @Override
    public FieldInfBase clone(EntityInfBase entity)
    {
        IntegerFieldInf result = new IntegerFieldInf();
        clone(result, entity);
        result.type = type;
        result.sqlType = sqlType;
        result.length = length;
        result.autoIncrement = autoIncrement;
        return result;
    }

}
