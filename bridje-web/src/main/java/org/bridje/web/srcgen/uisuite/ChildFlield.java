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

/**
 * This class defines a child control mapping for the control in witch is
 * deifned.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildFlield implements FieldDef
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;

    /**
     * The name of the field and/or mapping for the child control.
     * 
     * @return The name of the field and/or mapping for the child control.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the field and/or mapping for the child control.
     * 
     * @param name The name of the field and/or mapping for the child control.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The name of the control for this field/mapping.
     * 
     * @return The name of the control for this field/mapping.
     */
    public String getType()
    {
        return type;
    }

    /**
     * The name of the control for this field/mapping.
     * 
     * @param type The name of the control for this field/mapping.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String getJavaType()
    {
        return getType();
    }

    @Override
    public String getFieldType()
    {
        return "child";
    }
}
