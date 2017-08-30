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
package org.bridje.web.srcgen.uisuite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * A macro that belongs to a control.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ControlFtlMacro
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    public String params;
    
    @XmlValue
    private String content;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getParams()
    {
        return params;
    }

    public void setParams(String params)
    {
        this.params = params;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String findRealName(ControlDef control)
    {
        return "render" + control.getName() + capFirst(getName());
    }

    private String capFirst(String name)
    {
        String s1 = name.substring(0, 1).toUpperCase();
        String nameCapitalized = s1 + name.substring(1);
        return nameCapitalized;
    }
}
