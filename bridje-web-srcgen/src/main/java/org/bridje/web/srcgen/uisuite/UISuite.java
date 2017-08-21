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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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

    private String renderView;

    private String renderBody;

    private String renderHead;
    
    @XmlElementWrapper(name = "includes")
    @XmlElements(
    {
        @XmlElement(name = "include", type = String.class)
    })
    private List<String> includes;
    
    private StandaloneDef standalone;
    
    private StandaloneDef defines;

    @XmlElementWrapper(name = "ftlMacros")
    @XmlElements(
    {
        @XmlElement(name = "ftlMacro", type = FtlMacro.class)
    })
    private List<FtlMacro> ftlMacros;

    @XmlElementWrapper(name = "ftlFunctions")
    @XmlElements(
    {
        @XmlElement(name = "ftlFunction", type = FtlFunction.class)
    })
    private List<FtlFunction> ftlFunctions;
    
    @XmlElementWrapper(name = "ftlIncludes")
    @XmlElements(
    {
        @XmlElement(name = "ftlInclude", type = String.class)
    })
    private List<String> ftlIncludes;

    @XmlElementWrapper(name = "ftlImports")
    @XmlElements(
    {
        @XmlElement(name = "ftlImport", type = FtlImport.class)
    })
    private List<FtlImport> ftlImports;
    
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

    @XmlElementWrapper(name = "templates")
    @XmlElements(
    {
        @XmlElement(name = "template", type = TemplateControlDef.class)
    })
    private List<TemplateControlDef> templates;
    
    @XmlTransient
    private Map<String,List<ControlDef>> controlsMap;

    @XmlTransient
    private Map<String,List<TemplateControlDef>> templatesMap;

    @XmlElementWrapper(name = "enums")
    @XmlElements(
    {
        @XmlElement(name = "enum", type = ControlEnum.class)
    })
    private List<ControlEnum> enums;
    
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
    public String getRenderView()
    {
        return renderView;
    }

    /**
     * The HTML to render for the view container.
     * 
     * @param renderView The HTML to render for the view container.
     */
    public void setRenderView(String renderView)
    {
        this.renderView = renderView;
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
        if(resources == null) resources = new ArrayList<>();
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
        if(defaultResources == null) defaultResources = new Resource();
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
    public List<TemplateControlDef> getTemplates()
    {
        return templates;
    }

    /**
     * The list of controls templates for this suite.
     * 
     * @param templates The list of controls templates for this suite.
     */
    public void setTemplates(List<TemplateControlDef> templates)
    {
        this.templates = templates;
    }

    /**
     * The list of enums for this suite.
     * 
     * @return The list of enums for this suite.
     */
    public List<ControlEnum> getEnums()
    {
        return enums;
    }

    /**
     * The list of enums for this suite.
     * 
     * @param enums The list of enums for this suite.
     */
    public void setEnums(List<ControlEnum> enums)
    {
        this.enums = enums;
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
     * The freemarker templates to import for this suite.
     * 
     * @return The freemarker templates to import for this suite.
     */
    public List<FtlImport> getFtlImports()
    {
        return ftlImports;
    }

    /**
     * The freemarker templates to import for this suite.
     * 
     * @param ftlImports The freemarker templates to import for this suite.
     */
    public void setFtlImports(List<FtlImport> ftlImports)
    {
        this.ftlImports = ftlImports;
    }

    /**
     * The freemarker macros to includes for this suite.
     * 
     * @return The freemarker macros to includes for this suite.
     */
    public List<FtlMacro> getFtlMacros()
    {
        return ftlMacros;
    }

    /**
     * The freemarker macros to includes for this suite.
     * 
     * @param ftlMacros The freemarker macros to includes for this suite.
     */
    public void setFtlMacros(List<FtlMacro> ftlMacros)
    {
        this.ftlMacros = ftlMacros;
    }

    /**
     * The freemarker functions to includes for this suite.
     * 
     * @return The freemarker functions to includes for this suite.
     */
    public List<FtlFunction> getFtlFunctions()
    {
        return ftlFunctions;
    }

    /**
     * The freemarker functions to includes for this suite.
     * 
     * @param ftlFunctions The freemarker functions to includes for this suite.
     */
    public void setFtlFunctions(List<FtlFunction> ftlFunctions)
    {
        this.ftlFunctions = ftlFunctions;
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

    /**
     * The definition of the standalone children.
     * 
     * @return The definition of the standalone children.
     */
    public StandaloneDef getStandalone()
    {
        return standalone;
    }

    /**
     * The definition of the standalone children.
     * 
     * @param standalone The definition of the standalone children.
     */
    public void setStandalone(StandaloneDef standalone)
    {
        this.standalone = standalone;
    }

    /**
     * The defines for this UI suite.
     * 
     * @return The defines for this UI suite.
     */
    public StandaloneDef getDefines()
    {
        return defines;
    }

    /**
     * The defines for this UI suite.
     * 
     * @param defines The defines for this UI suite.
     */
    public void setDefines(StandaloneDef defines)
    {
        this.defines = defines;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    private VFile findIncludeFile(String include, VFile currentDir)
    {
        VFile includeFile;
        if(include.startsWith("/"))
        {
            includeFile = new VFile(include);
        }
        else
        {
            includeFile = new VFile(currentDir.getPath().join(new Path(include)));
        }
        return includeFile;
    }

    /**
     * Proccess the partial UI suites includes for this object.
     * 
     * @param currentDir The current dir for the includes.
     */
    public void processIncludes(VFile currentDir)
    {
        List<PartialUISuite> partialSuites = findIncludes(currentDir);
        includes.clear();

        partialSuites.forEach(this::importEnums);
        partialSuites.forEach(this::importResources);
        partialSuites.forEach(this::importFtlIncludes);
        partialSuites.forEach(this::importFtlMacros);
        partialSuites.forEach(this::importFtlFunctions);
        partialSuites.forEach(this::importFtlImports);

        partialSuites.forEach(p -> importTemplatesToMap(p.getTemplates()));
        importTemplatesToMap(templates);
        updateTemplateList();

        partialSuites.forEach(p -> importControlsToMap(p.getControls()));
        importControlsToMap(controls);
        updateControlList();
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
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, os);
    }

    private void importControlsToMap(List<ControlDef> controls)
    {
        if(controlsMap == null) controlsMap = new LinkedHashMap<>();
        if(controls != null) controls.forEach(this::addControlToMap);
    }

    private void importTemplatesToMap(List<TemplateControlDef> templates)
    {
        if(templatesMap == null) templatesMap = new LinkedHashMap<>();
        if(templates != null) templates.forEach(this::addTemplateToMap);
    }

    private void addControlToMap(ControlDef c)
    {
        List<ControlDef> lst = controlsMap.get(c);
        if(lst == null)
        {
            lst = new ArrayList<>();
            controlsMap.put(c.getName(), lst);
        }
        c.setUiSuite(this);
        lst.add(c);
    }

    private void addTemplateToMap(TemplateControlDef c)
    {
        List<TemplateControlDef> lst = templatesMap.get(c);
        if(lst == null)
        {
            lst = new ArrayList<>();
            templatesMap.put(c.getName(), lst);
        }
        c.setUiSuite(this);
        lst.add(c);
    }

    private void importEnums(PartialUISuite partial)
    {
        if(partial.getEnums() != null)
        {
            if(enums == null) enums = new ArrayList<>();
            partial.getEnums().stream().forEach(c -> c.setUiSuite(this));
            partial.getEnums().stream()
                    .filter(t -> !hasEnum(t.getName()))
                    .forEach(t -> enums.add(t));
        }
    }

    private void importResources(PartialUISuite partial)
    {
        if(partial.getResources() != null)
        {
            if(resources == null) resources = new ArrayList<>();
            partial.getResources().stream()
                    .filter(t -> !hasResource(t.getName()))
                    .forEach(t -> resources.add(t));
        }
    }

    private void importFtlIncludes(PartialUISuite partial)
    {
        if(partial.getFtlIncludes() != null)
        {
            if(ftlIncludes == null) ftlIncludes = new ArrayList<>();
            partial.getFtlIncludes().stream()
                    .filter(t -> !ftlIncludes.contains(t))
                    .forEach(t -> ftlIncludes.add(t));
        }
    }

    private void importFtlMacros(PartialUISuite partial)
    {
        if(partial.getFtlMacros() != null)
        {
            if(ftlMacros == null) ftlMacros = new ArrayList<>();
            partial.getFtlMacros().stream()
                    .filter(t -> !hasFtlMacro(t.getName()))
                    .forEach(t -> ftlMacros.add(t));
        }
    }

    private void importFtlFunctions(PartialUISuite partial)
    {
        if(partial.getFtlFunctions()!= null)
        {
            if(ftlFunctions == null) ftlFunctions = new ArrayList<>();
            partial.getFtlFunctions().stream()
                    .filter(t -> !hasFtlFunction(t.getName()))
                    .forEach(t -> ftlFunctions.add(t));
        }
    }

    private void importFtlImports(PartialUISuite partial)
    {
        if(partial.getFtlImports()!= null)
        {
            if(ftlImports == null) ftlImports = new ArrayList<>();
            partial.getFtlImports().stream()
                    .filter(t -> !hasFtlImport(t.getName()))
                    .forEach(t -> ftlImports.add(t));
        }
    }

    public boolean hasTemplate(String name)
    {
        return templates.stream().anyMatch(t -> t.getName().equals(name));
    }
    
    private boolean hasEnum(String name)
    {
        return enums.stream().anyMatch(t -> t.getName().equals(name));
    }

    private boolean hasResource(String name)
    {
        return resources.stream().anyMatch(t -> t.getName().equals(name));
    }

    private boolean hasFtlImport(String name)
    {
        return ftlImports.stream().anyMatch(t -> t.getName().equals(name));
    }

    private boolean hasFtlMacro(String name)
    {
        return ftlMacros.stream().anyMatch(t -> t.getName().equals(name));
    }

    private boolean hasFtlFunction(String name)
    {
        return ftlFunctions.stream().anyMatch(t -> t.getName().equals(name));
    }

    private void updateControlList()
    {
        controls = new ArrayList<>();
        controlsMap.forEach(this::mergeControls);
    }
    
    private void mergeControls(String name, List<ControlDef> controls)
    {
        ControlDef result = null;
        for (ControlDef control : controls)
        {
            if(result == null)
            {
                result = control;
            }
            else
            {
                result.override(control);
            }
        }
        this.controls.add(result);
    }

    private void updateTemplateList()
    {
        templates = new ArrayList<>();
        templatesMap.forEach(this::mergeTemplates);
    }

    private void mergeTemplates(String name, List<TemplateControlDef> templates)
    {
        TemplateControlDef result = null;
        for (TemplateControlDef template : templates)
        {
            if(result == null)
            {
                result = template;
            }
            else
            {
                result.override(template);
            }
        }
        this.templates.add(result);
    }

    private List<PartialUISuite> findIncludes(VFile currentDir)
    {
        List<PartialUISuite> result = new ArrayList<>();
        if(includes != null)
        {
            List<String> reverseIncludes = includes;
            Collections.reverse(reverseIncludes);
            for (String include : includes)
            {
                VFile includeFile = findIncludeFile(include, currentDir);
                if(includeFile.exists())
                {
                    try
                    {
                        PartialUISuite partial = PartialUISuite.load(includeFile);
                        if(partial != null) result.add(partial);
                    }
                    catch (IOException | JAXBException e)
                    {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            }
        }
        return result;
    }
}
