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
 * This class represents a decimal field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DecimalFieldInf extends FieldInfBase
{
    @XmlAttribute
    private DecimalFieldType type;

    @XmlAttribute
    private DecimalFieldSQLType sqlType;

    @XmlAttribute
    private Integer length;

    @XmlAttribute
    private Integer precision;

    /**
     * The java type of the decimal field, it must be Float, Double, BigDecimal.
     * 
     * @return The java type of the decimal field, it must be Float, Double, BigDecimal.
     */
    public DecimalFieldType getType()
    {
        if (type == null)
        {
            return DecimalFieldType.DOUBLE;
        }
        return type;
    }

    /**
     * The java type of the decimal field, it must be Float, Double, BigDecimal.
     * 
     * @param type The java type of the decimal field, it must be Float, Double, BigDecimal.
     */
    public void setType(DecimalFieldType type)
    {
        this.type = type;
    }

    /**
     * The SQL (JDBCType) of the decimal field.
     * 
     * @return The SQL (JDBCType) of the decimal field.
     */
    public DecimalFieldSQLType getSqlType()
    {
        return sqlType;
    }

    /**
     * The SQL (JDBCType) of the decimal field.
     * 
     * @param sqlType The SQL (JDBCType) of the decimal field.
     */
    public void setSqlType(DecimalFieldSQLType sqlType)
    {
        this.sqlType = sqlType;
    }

    /**
     * The length of the field, for the column generation in the RDBMS.
     * 
     * @return The length of the field, for the column generation in the RDBMS.
     */
    public Integer getLength()
    {
        return length;
    }

    /**
     * The length of the field, for the column generation in the RDBMS.
     * 
     * @param length The length of the field, for the column generation in the RDBMS.
     */
    public void setLength(Integer length)
    {
        this.length = length;
    }

    /**
     * The precision of the field, for the column generation in the RDBMS.
     * 
     * @return The precision of the field, for the column generation in the RDBMS.
     */
    public Integer getPrecision()
    {
        return precision;
    }

    /**
     * The precision of the field, for the column generation in the RDBMS.
     * 
     * @param precision The precision of the field, for the column generation in the RDBMS.
     */
    public void setPrecision(Integer precision)
    {
        this.precision = precision;
    }

    @Override
    public String getJavaType()
    {
        switch (getType())
        {
            case BIGDECIMAL:
                return "BigDecimal";
            case DOUBLE:
                return "Double";
            case FLOAT:
                return "Float";
        }
        return "Double";
    }

    @Override
    public String getTableColumn()
    {
        return "TableNumberColumn";
    }

    @Override
    public FieldInfBase clone(EntityInfBase entity)
    {
        DecimalFieldInf result = new DecimalFieldInf();
        clone(result, entity);
        result.type = type;
        result.sqlType = sqlType;
        return result;
    }

}
