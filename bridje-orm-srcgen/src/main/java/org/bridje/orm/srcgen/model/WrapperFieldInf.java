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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * The information for a field of an entity in the model.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WrapperFieldInf extends FieldInfBase
{
    @XmlAttribute
    private String type;

    @XmlElements(
    {
        @XmlElement(name = "boolean", type = BooleanField.class),
        @XmlElement(name = "number", type = NumberField.class),
        @XmlElement(name = "string", type = StringField.class),
        @XmlElement(name = "date", type = DateField.class)
    })
    private List<FieldInf> fields;

    public List<FieldInf> getFields()
    {
        return fields;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String getJavaType()
    {
        return getType();
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        setParent(parent);
    }

    @Override
    public FieldInfBase clone(Object parent)
    {
        WrapperFieldInf result = new WrapperFieldInf();
        super.clone(result, parent);
        result.type = this.type;
        result.fields = FieldInf.clonesFields(this.fields, result);
        return result;
    }
}
