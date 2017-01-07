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
import javax.xml.bind.annotation.XmlIDREF;

@XmlAccessorType(XmlAccessType.FIELD)
public class EnumFieldInf extends FieldInfBase
{
    @XmlIDREF
    @XmlAttribute(name = "type")
    private EnumInf type;

    @XmlAttribute
    private EnumFieldSQLType sqlType;

    @XmlAttribute
    private Integer length;

    public EnumInf getType()
    {
        return type;
    }

    public void setType(EnumInf type)
    {
        this.type = type;
    }

    public Integer getLength()
    {
        return length;
    }

    public void setLength(Integer length)
    {
        this.length = length;
    }

    public EnumFieldSQLType getSqlType()
    {
        return sqlType;
    }

    public void setSqlType(EnumFieldSQLType sqlType)
    {
        this.sqlType = sqlType;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
    }


    @Override
    public String getJavaType()
    {
        return getType().getName();
    }
}
