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
public class BooleanField extends FieldInf
{
    @XmlAttribute
    private String type;

    @XmlTransient
    private SQLTypeInf typeInf;

    @Override
    public SQLTypeInf getType()
    {
        if(typeInf == null)
        {
            typeInf = getEntity().getModel().findSQLType(type);
        }
        return typeInf;
    }

    @Override
    public String getColumnClass()
    {
        return "BooleanColumn";
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        setParent(parent);
    }
}
