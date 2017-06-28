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
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class ControlEnum
{
    @XmlAttribute
    private String name;
    
    @XmlTransient
    private UISuite uiSuite;

    @XmlElements(
    {
        @XmlElement(name = "constant", type = ControlEnumConstant.class)
    })
    private List<ControlEnumConstant> constants;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public List<ControlEnumConstant> getConstants()
    {
        return constants;
    }

    public void setConstants(List<ControlEnumConstant> constants)
    {
        this.constants = constants;
    }

    /**
     * The java class package for this control. This is taken from the UISuite.
     * 
     * @return The java class package for this control.
     */
    public String getPackage()
    {
        return uiSuite.getPackage();
    }

    /**
     * The full name of the java class for this control.
     * 
     * @return The full name of the java class for this control.
     */
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }


    /**
     * This method is called by JAXB after the unmarshal has happend.
     * 
     * @param u The unmarshaller.
     * @param parent The parent.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if(parent instanceof UISuite)
        {
            uiSuite = (UISuite) parent;
        }
    }

    /**
     * Gets the parent UISuite object.
     * 
     * @return The parent UISuite object.
     */
    public UISuite getUISuite()
    {
        return uiSuite;
    }

    /**
     * Sets the parent UISuite object.
     * 
     * @param uiSuite The parent UISuite object.
     */
    void setUiSuite(UISuite uiSuite)
    {
        this.uiSuite = uiSuite;
    }
}
