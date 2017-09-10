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

/**
 * This class represents a relationship field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExternalRelationFieldInf extends FieldInfBase
{
    @XmlAttribute
    private String type;

    @XmlAttribute
    private String entityPackage;

    public String getPackage()
    {
        return entityPackage;
    }
    
    /**
     * The entity for the relation.
     * 
     * @return The entity for the relation.
     */
    public String getType()
    {
        return type;
    }

    /**
     * The entity for the relation.
     * 
     * @param type The entity for the relation.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String getJavaType()
    {
        if(type == null) return "";
        if(entityPackage == null) return type;
        return entityPackage + "." + type;
    }

    @Override
    public String getTableColumn()
    {
        return "TableRelationColumn";
    }

    @Override
    public FieldInfBase clone(EntityInfBase entity)
    {
        ExternalRelationFieldInf result = new ExternalRelationFieldInf();
        clone(result, entity);
        result.type = type;
        result.entityPackage = entityPackage;
        return result;
    }
}
