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
import javax.xml.bind.annotation.XmlTransient;

/**
 * A reference to a resource.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceRef
{
    @XmlAttribute(name = "name")
    private String name;
    
    @XmlTransient
    private Resource resource;

    @XmlTransient
    private UISuite uiSuite;

    /**
     * Gets the name of the referenced resource.
     * 
     * @return The name of the referenced resource.
     */
    public String getName()
    {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the list of assets for the referenced resource.
     * 
     * @return The list of assets for the referenced resource.
     */
    public List<AssetBase> getContent()
    {
        if(getResource() == null) return null;
        return resource.getContent();
    }

    /**
     * Gets the actual referenced resource.
     * 
     * @return The actual referenced resource.
     */
    private Resource getResource()
    {
        if(resource == null)
        {
            resource = uiSuite.getResources().stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
        }
        return resource;
    }

    /**
     * The parent UISuite for this resource.
     * 
     * @return The parent UISuite for this resource.
     */
    public UISuite getUiSuite()
    {
        return uiSuite;
    }

    /**
     * The parent UISuite for this resource.
     * 
     * @param uiSuite The parent UISuite for this resource.
     */
    void setUiSuite(UISuite uiSuite)
    {
        this.uiSuite = uiSuite;
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
}
