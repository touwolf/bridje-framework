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

import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class WhereStmt extends ConditionStmt
{
    @XmlAttribute
    private boolean not;
    
    @XmlTransient
    private QueryInf query;

    @XmlTransient
    private Map<String, FieldInf> mapParams;

    @Override
    public boolean isNot()
    {
        return not;
    }

    public void setNot(boolean not)
    {
        this.not = not;
    }

    @Override
    public String getBooleanOperator()
    {
        return null;
    }

    @Override
    public QueryInf getQuery()
    {
        return query;
    }

    public Map<String, FieldInf> getParams()
    {
        if(mapParams == null)
        {
            mapParams = new LinkedHashMap<>();
            fillParams(mapParams);
        }
        return mapParams;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        query = (QueryInf)parent;
    }
}
