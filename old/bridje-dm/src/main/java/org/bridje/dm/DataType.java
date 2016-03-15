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

package org.bridje.dm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataType extends DataTypeBase
{
    @XmlAttribute
    private String javaType;

    @XmlAttribute
    private String sqlType;

    @XmlAttribute(name = "extends")
    @XmlIDREF
    private DataType extendsFrom;

    @XmlAttribute
    private boolean indexed;

    public DataType getExtends()
    {
        return extendsFrom;
    }

    public void setExtends(DataType extendsFrom)
    {
        this.extendsFrom = extendsFrom;
    }

    public String getJavaType()
    {
        return javaType;
    }

    public void setJavaType(String javaType)
    {
        this.javaType = javaType;
    }

    public String getSqlType()
    {
        return sqlType;
    }

    public void setSqlType(String sqlType)
    {
        this.sqlType = sqlType;
    }

    public boolean getIndexed()
    {
        return indexed;
    }

    public void setIndexed(boolean indexed)
    {
        this.indexed = indexed;
    }
}
