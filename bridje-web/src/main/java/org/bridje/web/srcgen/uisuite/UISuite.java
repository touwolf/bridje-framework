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

/**
 * An UI suite definition that can be use to generate the controls and templates 
 * to be use by the web API to render the view of an application.
 */
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

    /**
     * The name of the suite.
     * 
     * @return The name of the suite.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the suite.
     * 
     * @param name The name of the suite.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The java package for the controls.
     * 
     * @return The java package for the controls.
     */
    public String getPackage()
    {
        return packageName;
    }

    /**
     * The java package for the controls.
     * 
     * @param packageName The java package for the controls.
     */
    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    /**
     * The xsd namespace.
     * 
     * @return The xsd namespace.
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * The xsd namespace.
     * 
     * @param namespace The xsd namespace.
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * The HTML to render for the view container.
     * 
     * @return The HTML to render for the view container.
     */
    public String getRenderViewContainer()
    {
        return renderViewContainer;
    }

    /**
     * The HTML to render for the view container.
     * 
     * @param renderViewContainer The HTML to render for the view container.
     */
    public void setRenderViewContainer(String renderViewContainer)
    {
        this.renderViewContainer = renderViewContainer;
    }

    /**
     * The HTML to render for the body.
     * 
     * @return The HTML to render for the body.
     */
    public String getRenderBody()
    {
        return renderBody;
    }

    /**
     * The HTML to render for the body.
     * 
     * @param renderBody The HTML to render for the body.
     */
    public void setRenderBody(String renderBody)
    {
        this.renderBody = renderBody;
    }
    
    /**
     * The HTML to render for the head.
     * 
     * @return The HTML to render for the head.
     */
    public String getRenderHead()
    {
        return renderHead;
    }

    /**
     * The HTML to render for the head.
     * 
     * @param renderHead The HTML to render for the head.
     */
    public void setRenderHead(String renderHead)
    {
        this.renderHead = renderHead;
    }

    /**
     * The resources for this suite.
     * 
     * @return The resources for this suite.
     */
    public List<Resource> getResources()
    {
        return resources;
    }

    /**
     * The resources for this suite.
     * 
     * @param resources The resources for this suite.
     */
    public void setResources(List<Resource> resources)
    {
        this.resources = resources;
    }

    /**
     * The default resources to render for each view of this suite.
     * 
     * @return The default resources to render for each view of this suite.
     */
    public Resource getDefaultResources()
    {
        return defaultResources;
    }

    /**
     * The default resources to render for each view of this suite.
     * 
     * @param defaultResources The default resources to render for each view of this suite.
     */
    public void setDefaultResources(Resource defaultResources)
    {
        this.defaultResources = defaultResources;
    }

    /**
     * The list of controls to generate for this suite.
     * 
     * @return The list of controls to generate for this suite.
     */
    public List<ControlDef> getControls()
    {
        return controls;
    }

    /**
     * The list of controls to generate for this suite.
     * 
     * @param controls The list of controls to generate for this suite.
     */
    public void setControls(List<ControlDef> controls)
    {
        this.controls = controls;
    }

    /**
     * The list of controls templates for this suite.
     * 
     * @return The list of controls templates for this suite.
     */
    public List<ControlDef> getControlsTemplates()
    {
        return controlsTemplates;
    }

    /**
     * The list of controls templates for this suite.
     * 
     * @param controlsTemplates The list of controls templates for this suite.
     */
    public void setControlsTemplates(List<ControlDef> controlsTemplates)
    {
        this.controlsTemplates = controlsTemplates;
    }

    /**
     * The freemarker templates to includes for this suite.
     * 
     * @return The freemarker templates to includes for this suite.
     */
    public List<String> getFtlIncludes()
    {
        return ftlIncludes;
    }

    /**
     * The freemarker templates to includes for this suite.
     * 
     * @param ftlIncludes The freemarker templates to includes for this suite.
     */
    public void setFtlIncludes(List<String> ftlIncludes)
    {
        this.ftlIncludes = ftlIncludes;
    }

    /**
     * The partial UI suites declarations to include.
     * 
     * @return The partial UI suites declarations to include.
     */
    public List<String> getIncludes()
    {
        return includes;
    }

    /**
     * The partial UI suites declarations to include.
     * 
     * @param includes The partial UI suites declarations to include.
     */
    public void setIncludes(List<String> includes)
    {
        this.includes = includes;
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
    
    /**
     * Proccess the partial UI suites includes for this object.
     * 
     * @param currentDir The current dir for the includes.
     */
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
     * Loads an UISuite from a file.
     * 
     * @param xmlFile The file to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     * @throws IOException If any IO Exception occurs.
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
     * Loads an UISuite from an input stream.
     * 
     * @param is The input stream to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static UISuite load(InputStream is) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(UISuite.class);
        return (UISuite)ctx.createUnmarshaller().unmarshal(is);
    }

    /**
     * Save a UISuite to an output stream.
     * 
     * @param os The output stream to write the object to.
     * @param object The object to write.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static void save(OutputStream os, UISuite object) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(UISuite.class);
        ctx.createMarshaller().marshal(object, os);
    }
}
