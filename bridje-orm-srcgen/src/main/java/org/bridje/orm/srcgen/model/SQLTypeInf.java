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

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class SQLTypeInf
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String javaType;

    @XmlAttribute
    private String readType;

    @XmlAttribute
    private String jdbcType;

    @XmlAttribute
    private Integer length;

    @XmlAttribute
    private Integer precision;

    @XmlAttribute
    private String parser;

    @XmlAttribute
    private String writer;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getJavaType()
    {
        return javaType;
    }

    public void setJavaType(String javaType)
    {
        this.javaType = javaType;
    }

    public String getReadType()
    {
        return readType;
    }

    public void setReadType(String readType)
    {
        this.readType = readType;
    }

    public String getJdbcType()
    {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType)
    {
        this.jdbcType = jdbcType;
    }

    public Integer getLength()
    {
        if(length == null)
        {
            return 0;
        }
        return length;
    }

    public void setLength(Integer length)
    {
        this.length = length;
    }

    public Integer getPrecision()
    {
        if(precision == null)
        {
            return 0;
        }
        return precision;
    }

    public void setPrecision(Integer precision)
    {
        this.precision = precision;
    }

    public String getParser()
    {
        return parser;
    }

    public void setParser(String parser)
    {
        this.parser = parser;
    }

    public String getWriter()
    {
        return writer;
    }

    public void setWriter(String writer)
    {
        this.writer = writer;
    }

    public String writerCode(String value)
    {
        if(writer == null) return value;
        return String.format(writer, value);
    }

    public String parserCode(String value)
    {
        if(parser == null) return value;
        return String.format(parser, value);
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        
    }
}
