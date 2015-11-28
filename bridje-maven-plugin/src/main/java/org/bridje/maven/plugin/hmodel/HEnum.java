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

package org.bridje.maven.plugin.hmodel;

import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class defines an enumerator to be use in the hierarchy.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEnum
{
    @XmlTransient
    private HModel model;

    @XmlAttribute
    private String name;
    
    @XmlElements(
    {
        @XmlElement(name = "value", type = HEnumValue.class)
    })
    private List<HEnumValue> values;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public HModel getModel()
    {
        return model;
    }

    public void setModel(HModel model)
    {
        this.model = model;
    }

    public List<HEnumValue> getValues()
    {
        return values;
    }

    public void setValues(List<HEnumValue> values)
    {
        this.values = values;
    }
    
    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        setModel((HModel)parent);
    }
}
