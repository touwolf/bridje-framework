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

package org.bridje.jfx.srcgen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Icon definition.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IconDef
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String file;

    /**
     * Gets the name for the icon.
     * 
     * @return The name for the icon.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name for the icon.
     * 
     * @param name The name for the icon.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the file for the icon.
     * 
     * @return The file for the icon.
     */
    public String getFile()
    {
        return file;
    }

    /**
     * Sets the file for the icon.
     * 
     * @param file The file for the icon.
     */
    public void setFile(String file)
    {
        this.file = file;
    }
}
