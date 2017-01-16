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

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Resource
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlElements(
    {
        @XmlElement(name = "script", type = Script.class),
        @XmlElement(name = "style", type = Style.class),
        @XmlElement(name = "link", type = Link.class)
    })
    private List<AssetBase> content;

    @XmlTransient
    private List<Script> scriptList;

    @XmlTransient
    private List<Style> styleList;

    @XmlTransient
    private List<Link> linkList;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<AssetBase> getContent()
    {
        return content;
    }

    public void setContent(List<AssetBase> content)
    {
        this.content = content;
    }

    public List<Script> getScripts()
    {
        if (scriptList == null)
        {
            scriptList = new LinkedList<>();
            content.forEach(field ->
            {
                if (Script.class.isAssignableFrom(field.getClass()))
                {
                    scriptList.add((Script) field);
                }
            });
        }
        return scriptList;
    }

    public List<Style> getStyles()
    {
        if (styleList == null)
        {
            styleList = new LinkedList<>();
            content.forEach(field ->
            {
                if (Style.class.isAssignableFrom(field.getClass()))
                {
                    styleList.add((Style) field);
                }
            });
        }
        return styleList;
    }

    public List<Link> getLinks()
    {
        if (linkList == null)
        {
            linkList = new LinkedList<>();
            content.forEach(field ->
            {
                if (Link.class.isAssignableFrom(field.getClass()))
                {
                    linkList.add((Link) field);
                }
            });
        }
        return linkList;
    }
}
