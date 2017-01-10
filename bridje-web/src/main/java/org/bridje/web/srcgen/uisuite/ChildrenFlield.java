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
public class ChildrenFlield
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
        @XmlElement(name = "child", type = ChildFlield.class)
    })
    private List<ChildFlield> content;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getAllowPlaceHolder()
    {
        return allowPlaceHolder;
    }

    public void setAllowPlaceHolder(Boolean allowPlaceHolder)
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

    public Boolean getSingle()
    {
        return single;
    }

    public void setSingle(Boolean single)
    {
        this.single = single;
    }

    public List<ChildFlield> getContent()
    {
        return content;
    }

    public void setContent(List<ChildFlield> content)
    {
        this.content = content;
    }

    public String getJavaType()
    {
        return "List<Control>";
    }

    public String getFieldType()
    {
        return "children";
    }
}
