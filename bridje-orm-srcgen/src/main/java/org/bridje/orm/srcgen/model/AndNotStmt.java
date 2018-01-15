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
import javax.xml.bind.annotation.XmlTransient;

/**
 * Information about a negated condition with an "and" operator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AndNotStmt extends ConditionStmt
{
    @XmlTransient
    private ConditionStmt parent;
    
    @Override
    public boolean isNot()
    {
        return true;
    }

    @Override
    public String getBooleanOperator()
    {
        return "and";
    }

    /**
     * The previous condition of this condition.
     * 
     * @return The previous condition of this condition.
     */
    public ConditionStmt getParent()
    {
        return parent;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        this.parent = (ConditionStmt)parent;
    }

    @Override
    public QueryInf getQuery()
    {
        return getParent().getQuery();
    }
}
