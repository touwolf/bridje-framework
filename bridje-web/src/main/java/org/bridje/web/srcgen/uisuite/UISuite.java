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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "uisuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class UISuite
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute(name = "package")
    private String packageName;
    
    @XmlAttribute
    private String namespace;

    private String renderViewContainer;
    
    private String renderBody;
    
    private String renderHead;
    
    @XmlElementWrapper(name = "ftlIncludes")
    @XmlElements(
    {
        @XmlElement(name = "ftlIncludes", type = String.class)
    })
    private List<String> ftlIncludes;

    @XmlElementWrapper(name = "resources")
    @XmlElements(
    {
        @XmlElement(name = "resource", type = Resource.class)
    })
    private List<Resource> resources;

    private Resource defaultResources;

    @XmlElementWrapper(name = "controls")
    @XmlElements(
    {
        @XmlElement(name = "control", type = ControlDef.class)
    })
    private List<ControlDef> controls;

    @XmlElementWrapper(name = "controlsTemplates")
    @XmlElements(
    {
        @XmlElement(name = "controlTemplate", type = ControlDef.class)
    })
    private List<ControlDef> controlsTemplates;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPackage()
    {
        return packageName;
    }

    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public String getRenderViewContainer()
    {
        return renderViewContainer;
    }

    public void setRenderViewContainer(String renderViewContainer)
    {
        this.renderViewContainer = renderViewContainer;
    }

    public String getRenderBody()
    {
        return renderBody;
    }

    public void setRenderBody(String renderBody)
    {
        this.renderBody = renderBody;
    }

    public String getRenderHead()
    {
        return renderHead;
    }

    public void setRenderHead(String renderHead)
    {
        this.renderHead = renderHead;
    }

    public List<Resource> getResources()
    {
        return resources;
    }

    public void setResources(List<Resource> resources)
    {
        this.resources = resources;
    }

    public Resource getDefaultResources()
    {
        return defaultResources;
    }

    public void setDefaultResources(Resource defaultResources)
    {
        this.defaultResources = defaultResources;
    }

    public List<ControlDef> getControls()
    {
        return controls;
    }

    public void setControls(List<ControlDef> controls)
    {
        this.controls = controls;
    }

    public List<ControlDef> getControlsTemplates()
    {
        return controlsTemplates;
    }

    public void setControlsTemplates(List<ControlDef> controlsTemplates)
    {
        this.controlsTemplates = controlsTemplates;
    }

    public List<String> getFtlIncludes()
    {
        return ftlIncludes;
    }

    public void setFtlIncludes(List<String> ftlIncludes)
    {
        this.ftlIncludes = ftlIncludes;
    }
}
