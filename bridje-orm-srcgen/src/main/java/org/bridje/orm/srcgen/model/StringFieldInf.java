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
 * This class represents a string field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StringFieldInf extends FieldInfBase
{
    @XmlAttribute
    private StringFieldSQLType sqlType;

    @XmlAttribute
    private Integer length;

    @XmlAttribute
    private Boolean emptyToNull;

    @XmlAttribute
    private Boolean blankToNull;

    /**
     * The length of the column.
     * 
     * @return The length of the column.
     */
    public Integer getLength()
    {
        return length;
    }

    /**
     * The length of the column.
     * 
     * @param length The length of the column.
     */
    public void setLength(Integer length)
    {
        this.length = length;
    }

    /**
     * The SQL type for this string field.
     * 
     * @return The SQL type for this string field.
     */
    public StringFieldSQLType getSqlType()
    {
        return sqlType;
    }

    /**
     * The SQL type for this string field.
     * 
     * @param sqlType The SQL type for this string field.
     */
    public void setSqlType(StringFieldSQLType sqlType)
    {
        this.sqlType = sqlType;
    }

    /**
     * If the field is empty it must be converted to null.
     * 
     * @return If the field is empty it must be converted to null.
     */
    public Boolean getEmptyToNull()
    {
        return emptyToNull;
    }

    /**
     * If the field is empty it must be converted to null.
     * 
     * @param emptyToNull If the field is empty it must be converted to null.
     */
    public void setEmptyToNull(Boolean emptyToNull)
    {
        this.emptyToNull = emptyToNull;
    }

    /**
     * If the field is blank it must be converted to null.
     * 
     * @return If the field is blank it must be converted to null.
     */
    public Boolean getBlankToNull()
    {
        return blankToNull;
    }

    /**
     * If the field is blank it must be converted to null.
     * 
     * @param blankToNull If the field is blank it must be converted to null.
     */
    public void setBlankToNull(Boolean blankToNull)
    {
        this.blankToNull = blankToNull;
    }

    @Override
    public String getJavaType()
    {
        return "String";
    }

    @Override
    public String getTableColumn()
    {
        return "TableStringColumn";
    }

    @Override
    public FieldInfBase clone(EntityInfBase entity)
    {
        StringFieldInf result = new StringFieldInf();
        clone(result, entity);
        result.sqlType = sqlType;
        result.length = length;
        result.emptyToNull = emptyToNull;
        result.blankToNull = blankToNull;
        return result;
    }
}
