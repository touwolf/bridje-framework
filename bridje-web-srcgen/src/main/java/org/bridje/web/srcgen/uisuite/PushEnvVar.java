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
 * Sets the given var to the given value in the el environment.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PushEnvVar implements ReadInputAction
{
    @XmlAttribute
    private String var;

    @XmlAttribute
    private String value;

    /**
     * The name of var to set.
     * 
     * @return The name of var to set.
     */
    public String getVar()
    {
        return var;
    }

    /**
     * The name of var to set.
     * 
     * @param var The name of var to set.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /**
     * The value of the var.
     * 
     * @return The value of the var.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * The value of the var.
     * 
     * @param value The value of the var.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}
