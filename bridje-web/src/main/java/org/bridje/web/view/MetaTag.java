/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.web.view;

import java.util.Objects;
import javax.xml.bind.annotation.*;

/**
 * Defines a meta information for the current view.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaTag
{
    @XmlAttribute(name = "name")
    private String name;

    @XmlValue
    private String content;

    /**
     * The name of the meta tag.
     * 
     * @return The name of the meta tag.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the meta tag.
     * 
     * @param name The name of the meta tag.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The content of the meta tag.
     * 
     * @return The content of the meta tag.
     */
    public String getContent()
    {
        return content;
    }

    /**
     * The name of the meta tag.
     * 
     * @param content The content of the meta tag.
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        final MetaTag other = (MetaTag) obj;
        return Objects.equals(this.name, other.name);
    }
}
