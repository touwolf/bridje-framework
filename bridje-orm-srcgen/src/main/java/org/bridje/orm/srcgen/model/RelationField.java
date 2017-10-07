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
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class RelationField extends FieldInf
{
    @XmlTransient
    private SQLTypeInf typeInf;

    @XmlAttribute(name = "with")
    private String referencesName;

    @XmlAttribute
    private RelationStrategy onUpdate;

    @XmlAttribute
    private RelationStrategy onDelete;
    
    public String getReferencesName()
    {
        return referencesName;
    }

    public void setReferencesName(String referencesName)
    {
        this.referencesName = referencesName;
    }

    public EntityInf getWith()
    {
        return getEntity().getModel().findEntity(referencesName);
    }

    public RelationStrategy getOnUpdate()
    {
        if(onUpdate == null)
        {
            onUpdate = RelationStrategy.NO_ACTION;
        }
        return onUpdate;
    }

    public void setOnUpdate(RelationStrategy onUpdate)
    {
        this.onUpdate = onUpdate;
    }

    public RelationStrategy getOnDelete()
    {
        if(onDelete == null)
        {
            onDelete = RelationStrategy.NO_ACTION;
        }
        return onDelete;
    }

    public void setOnDelete(RelationStrategy onDelete)
    {
        this.onDelete = onDelete;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        setParent(parent);
    }

    @Override
    public SQLTypeInf getType()
    {
        if(typeInf == null)
        {
            SQLTypeInf sqlType = getWith().getKey().getType();
            typeInf = new SQLTypeInf();
            typeInf.setName("RELATION_TYPE");
            typeInf.setJavaType(getWith().getName());
            typeInf.setReadType(sqlType.getReadType());
            typeInf.setJdbcType(sqlType.getJdbcType());
            typeInf.setLength(sqlType.getLength());
            typeInf.setPrecision(sqlType.getPrecision());
        }
        return typeInf;
    }
    
    @Override
    public String getColumnClass()
    {
        return "Column";
    }

    @Override
    public String getFullTypeName()
    {
        return getWith().getName() + "." + getType().getName();
    }
}
