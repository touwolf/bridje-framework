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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;

@XmlRootElement(name = "uisuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class UISuite
{
    private static final Logger LOG = Logger.getLogger(UISuite.class.getName());

    @XmlAttribute
    private String name;
    
    @XmlAttribute(name = "package")
    private String packageName;
    
    @XmlAttribute
    private String namespace;

    private String renderViewContainer;
    
    private String renderBody;
    
    private String renderHead;
    
    @XmlElementWrapper(name = "includes")
    @XmlElements(
    {
        @XmlElement(name = "include", type = String.class)
    })
    private List<String> includes;

    @XmlElementWrapper(name = "ftlIncludes")
    @XmlElements(
    {
        @XmlElement(name = "ftlInclude", type = String.class)
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

    public List<String> getIncludes()
    {
        return includes;
    }

    public void setIncludes(List<String> includes)
    {
        this.includes = includes;
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
    
    public void processIncludes(VFile currentDir)
    {
        if(includes != null)
        {
            for (String include : includes)
            {
                VFile includeFile = new VFile(currentDir.getPath().join(new Path(include)));
                if(includeFile.exists())
                {
                    try
                    {
                        PartialUISuite partial = PartialUISuite.load(includeFile);
                        if(partial != null)
                        {
                            if(partial.getControls() != null)
                            {
                                if(controls == null) controls = new ArrayList<>();
                                partial.getControls().stream().forEach(c -> c.setUiSuite(this));
                                controls.addAll(partial.getControls());
                            }
                            if(partial.getControlsTemplates() != null)
                            {
                                if(controlsTemplates == null) controlsTemplates = new ArrayList<>();
                                partial.getControlsTemplates().stream().forEach(c -> c.setUiSuite(this));
                                controlsTemplates.addAll(partial.getControlsTemplates());
                            }
                            if(partial.getResources() != null)
                            {
                                if(resources == null) resources = new ArrayList<>();
                                resources.addAll(partial.getResources());
                            }
                            if(partial.getFtlIncludes() != null)
                            {
                                if(ftlIncludes == null) ftlIncludes = new ArrayList<>();
                                ftlIncludes.addAll(partial.getFtlIncludes());
                            }
                        }
                    }
                    catch (IOException | JAXBException e)
                    {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            }
            includes.clear();
        }
    }

    /**
     * 
     * @param xmlFile 
     * @return 
     * @throws JAXBException 
     * @throws IOException 
     */
    public static UISuite load(VFile xmlFile) throws JAXBException, IOException
    {
        if(!xmlFile.exists()) return null;
        try(InputStream is = new VFileInputStream(xmlFile))
        {
            return load(is);
        }
    }

    /**
     * 
     * @param is 
     * @return 
     * @throws JAXBException 
     */
    public static UISuite load(InputStream is) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(UISuite.class);
        return (UISuite)ctx.createUnmarshaller().unmarshal(is);
    }

    /**
     * 
     * @param os 
     * @param config 
     * @throws JAXBException 
     */
    public static void save(OutputStream os, UISuite config) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(UISuite.class);
        ctx.createMarshaller().marshal(config, os);
    }
}
