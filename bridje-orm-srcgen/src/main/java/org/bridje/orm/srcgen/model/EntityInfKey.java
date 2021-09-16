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
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class EntityInfKey
{
    @XmlElements(
    {
        @XmlElement(name = "ai", type = AiField.class),
        @XmlElement(name = "boolean", type = BooleanField.class),
        @XmlElement(name = "number", type = NumberField.class),
        @XmlElement(name = "string", type = StringField.class)
    })
    private FieldInf field;

    @XmlTransient
    private EntityInf entity;

    public FieldInf getField()
    {
        return field;
    }

    public void setField(FieldInf field)
    {
        this.field = field;
    }

    public EntityInf getEntity()
    {
        return entity;
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        entity = (EntityInf)parent;
    }
}
