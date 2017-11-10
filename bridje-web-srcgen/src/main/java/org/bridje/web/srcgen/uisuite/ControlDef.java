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
 * A UI suite control definition.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ControlDef extends BaseControlDef
{
    @XmlAttribute
    private boolean isTransient;

    private String render;

    @XmlAttribute
    private String renderFile;

    public boolean getIsTransient()
    {
        return isTransient;
    }

    public void setIsTransient(boolean isTransient)
    {
        this.isTransient = isTransient;
    }

    /**
     * Defines the path (relative to XML suite) to the file with the control render content.
     * Only used when the render value is not defined.
     *
     * @return the path (relative to XML suite) to the file with the control render content.
     */
    public String getRenderFile()
    {
        return renderFile;
    }

    /**
     * Defines the render freemarker script to be use by this control.
     * 
     * @return The text of the render script.
     */
    public String getRender()
    {
        return render;
    }

    /**
     * 
     * @param render the control content.
     */
    public void setRender(String render)
    {
        this.render = render;
    }

    /**
     * Changes the names of the macros called in this ftl segment so they fit 
     * the real names print in the Theme template.
     * 
     * @param render The freemarker segmente to replace the macros in.
     * @return The same freemarker code by with the macros name replaced.
     */
    public String replaceMacros(String render)
    {
        if(render == null) return "";
        String replaced = render;
        for(ControlFtlMacro m : getMacros())
        {
            replaced = replaced.replaceAll("<@" + m.getName(), "<@" + m.findRealName(this));
            replaced = replaced.replaceAll("</@" + m.getName(), "</@" + m.findRealName(this));
        }
        return replaced;
    }
}
