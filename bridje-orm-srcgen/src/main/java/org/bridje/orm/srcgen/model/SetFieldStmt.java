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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class SetFieldStmt
{
    @XmlAttribute
    private String field;
    
    @XmlAttribute
    private String value;

    @XmlTransient
    private QueryInf query;

    public String getFieldName()
    {
        return field;
    }
    
    public FieldInf getField()
    {
        return getQuery().getEntity().findField(field);
    }

    public String getValue()
    {
        if(value.startsWith("$")) return value.substring(1);
        return null;
    }

    public QueryInf getQuery()
    {
        return query;
    }
    
    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        query = (QueryInf)parent;
    }

    public String getParam()
    {
        if(value.startsWith("$")) return value.substring(1);
        return null;
    }
}
