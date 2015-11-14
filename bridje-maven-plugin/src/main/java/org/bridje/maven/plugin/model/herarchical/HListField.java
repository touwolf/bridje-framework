/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.maven.plugin.model.herarchical;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 *
 * @author Gilberto
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HListField extends HField
{
    @XmlAttribute(name = "of")
    private String of;

    @XmlElements(
    {
        @XmlElement(name = "element", type = HListElement.class)
    })
    private List<HListElement> elements;

    public String getOf()
    {
        return of;
    }

    public void setOf(String of)
    {
        this.of = of;
    }

    public List<HListElement> getElements()
    {
        return elements;
    }

    public void setElements(List<HListElement> elements)
    {
        this.elements = elements;
    }

    @Override
    public String getJavaType()
    {
        return "java.util.List<" + of + ">";
    }

    @Override
    public boolean getIsList()
    {
        return true;
    }
}
