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

    public DecimalFieldType getType()
    {
        if(type == null) return DecimalFieldType.DOUBLE;
        return type;
    }

    public void setType(DecimalFieldType type)
    {
        this.type = type;
    }

    public DecimalFieldSQLType getSqlType()
    {
        return sqlType;
    }

    public void setSqlType(DecimalFieldSQLType sqlType)
    {
        this.sqlType = sqlType;
    }

    public Integer getLength()
    {
        return length;
    }

    public void setLength(Integer length)
    {
        this.length = length;
    }

    public Integer getPrecision()
    {
        return precision;
    }

    public void setPrecision(Integer precision)
    {
        this.precision = precision;
    }

    @Override
    public String getJavaType()
    {
        switch(getType())
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
