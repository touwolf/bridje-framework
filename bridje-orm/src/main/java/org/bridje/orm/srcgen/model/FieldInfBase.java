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
public abstract class FieldInfBase
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String column;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private Boolean required;

    @XmlAttribute
    private Boolean indexed;

    @XmlAttribute
    private Boolean key;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColumn()
    {
        if(this.column == null) this.column = Utils.toSQLName(this.name);
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Boolean isRequired()
    {
        return required;
    }

    public void setRequired(Boolean required)
    {
        this.required = required;
    }

    public Boolean isIndexed()
    {
        return indexed;
    }

    public void setIndexed(Boolean indexed)
    {
        this.indexed = indexed;
    }

    public Boolean isKey()
    {
        if(key == null) return false;
        return key;
    }

    public void setKey(Boolean key)
    {
        this.key = key;
    }
    
    public abstract String getJavaType();
    
    public String getTableColumn()
    {
        return "TableComparableColumn";
    }
}
