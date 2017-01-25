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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChildrenFlield implements FieldDef
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private Boolean allowPlaceHolder;
    
    @XmlAttribute
    private String wrapper;

    @XmlAttribute
    private Boolean single;
                
    @XmlElements(
    {
        @XmlElement(name = "child", type = ChildField.class)
    })
    private List<ChildField> content;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean getAllowPlaceHolder()
    {
        if(this.allowPlaceHolder == null) return false;
        return allowPlaceHolder;
    }

    public void setAllowPlaceHolder(boolean allowPlaceHolder)
    {
        this.allowPlaceHolder = allowPlaceHolder;
    }

    public String getWrapper()
    {
        return wrapper;
    }

    public void setWrapper(String wrapper)
    {
        this.wrapper = wrapper;
    }

    public boolean getIsSingle()
    {
        if(single == null) return false;
        return single;
    }

    public void setSingle(boolean single)
    {
        this.single = single;
    }

    public List<ChildField> getContent()
    {
        return content;
    }

    public void setContent(List<ChildField> content)
    {
        this.content = content;
    }

    @Override
    public String getJavaType()
    {
        if(getIsSingle()) return "Control";
        return "List<Control>";
    }

    @Override
    public String getFieldType()
    {
        return "children";
    }

    public String getDefaultValue()
    {
        if(getIsSingle()) return null;
        return "new ArrayList<>()";
    }

    @Override
    public boolean getIsChild()
    {
        return true;
    }

    @Override
    public boolean getIsEvent()
    {
        return false;
    }

    @Override
    public boolean getIsInput()
    {
        return false;
    }
}
