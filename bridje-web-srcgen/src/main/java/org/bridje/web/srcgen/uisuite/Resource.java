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

/**
 * A resource declaration for an UI suite.
 */
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
    
    /**
     * The name of the resource.
     * 
     * @return The name of the resource.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the resource.
     * 
     * @param name The name of the resource.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The list of assets for this resource.
     * 
     * @return The list of assets for this resource.
     */
    public List<AssetBase> getContent()
    {
        return content;
    }

    /**
     * The list of assets for this resource.
     * 
     * @param content The list of assets for this resource.
     */
    public void setContent(List<AssetBase> content)
    {
        this.content = content;
    }

    /**
     * Gets all the scripts declared for this resource.
     * 
     * @return The list of scripts declared for this resource.
     */
    public List<Script> getScripts()
    {
        if (scriptList == null)
        {
            scriptList = new LinkedList<>();
            if(content != null)
            {
                content.forEach(field ->
                {
                    if (Script.class.isAssignableFrom(field.getClass()))
                    {
                        scriptList.add((Script) field);
                    }
                });
            }
        }
        return scriptList;
    }

    /**
     * Gets all the styles declared for this resource.
     * 
     * @return The list of styles declared for this resource.
     */
    public List<Style> getStyles()
    {
        if (styleList == null)
        {
            styleList = new LinkedList<>();
            if(content != null)
            {
                content.forEach(field ->
                {
                    if (Style.class.isAssignableFrom(field.getClass()))
                    {
                        styleList.add((Style) field);
                    }
                });
            }
        }
        return styleList;
    }

    /**
     * Gets all the links declared for this resource.
     * 
     * @return The list of links declared for this resource.
     */
    public List<Link> getLinks()
    {
        if (linkList == null)
        {
            linkList = new LinkedList<>();
            if(content != null)
            {
                content.forEach(field ->
                {
                    if (Link.class.isAssignableFrom(field.getClass()))
                    {
                        linkList.add((Link) field);
                    }
                });
            }
        }
        return linkList;
    }
}
