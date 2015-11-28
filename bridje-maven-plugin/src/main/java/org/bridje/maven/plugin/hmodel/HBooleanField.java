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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * A boolean field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HBooleanField extends HBasicField
{
    @XmlAttribute(name = "default")
    private Boolean defaultValue;

    @Override
    public String getJavaType()
    {
        if(this.isRequired())
        {
            return "boolean";
        }
        return "Boolean";
    }

    public String getDefaultValueExp()
    {
        if(defaultValue == null)
        {
            return null;
        }
        return String.valueOf(defaultValue);
    }
    
    public Boolean getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getXmlType()
    {
        return "boolean";
    }
}
