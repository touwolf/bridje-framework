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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateQueryInf extends QueryInf
{
    private static final Logger LOG = Logger.getLogger(UpdateQueryInf.class.getName());

    private WhereStmt where;

    @XmlTransient
    private EntityInf entity;
    
    @XmlElements(
    {
        @XmlElement(name = "set", type = SetFieldStmt.class)
    })
    private List<SetFieldStmt> sets;
    
    @XmlTransient
    private Map<String, FieldInf> mapParams;

    @Override
    public String getQueryType()
    {
        return "update";
    }

    public WhereStmt getWhere()
    {
        return where;
    }

    public void setWhere(WhereStmt where)
    {
        this.where = where;
    }

    @Override
    public EntityInf getEntity()
    {
        return entity;
    }

    public List<SetFieldStmt> getSets()
    {
        return sets;
    }
    
    public Map<String, FieldInf> getParams()
    {
        if(mapParams == null)
        {
            mapParams = new LinkedHashMap<>();
            if(getWhere() != null)
            {
                mapParams.putAll(getWhere().getParams());
            }
            fillParams(mapParams);
        }
        return mapParams;
    }

    public void fillParams(Map<String, FieldInf> fillParams)
    {
        try
        {
            for (SetFieldStmt set : sets)
            {
                if(set.getParam() != null && set.getField() != null)
                {
                    fillParams.put(set.getParam(), set.getField());
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInf)parent;
    }
}
