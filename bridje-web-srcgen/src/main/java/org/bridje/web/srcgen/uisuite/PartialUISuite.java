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
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;

/**
 * A partial UI suite definition.
 */
@XmlRootElement(name = "partialuisuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class PartialUISuite
{
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

    @XmlElementWrapper(name = "resources")
    @XmlElements(
    {
        @XmlElement(name = "resource", type = Resource.class)
    })
    private List<Resource> resources;

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

    @XmlElementWrapper(name = "enums")
    @XmlElements(
    {
        @XmlElement(name = "enum", type = ControlEnum.class)
    })
    private List<ControlEnum> enums;

    @XmlElementWrapper(name = "ftlImports")
    @XmlElements(
    {
        @XmlElement(name = "ftlImport", type = FtlImport.class)
    })
    private List<FtlImport> ftlImports;

    /**
     * The resources for this partial UI suite declaration.
     * 
     * @return The resources for this partial UI suite declaration.
     */
    public List<Resource> getResources()
    {
        return resources;
    }

    /**
     * The resources for this partial UI suite declaration.
     * 
     * @param resources The resources for this partial UI suite declaration.
     */
    public void setResources(List<Resource> resources)
    {
        this.resources = resources;
    }

    /**
     * The list of controls for this partial declaration.
     * 
     * @return The list of controls for this partial declaration.
     */
    public List<ControlDef> getControls()
    {
        return controls;
    }

    /**
     * The list of controls for this partial declaration.
     * 
     * @param controls The list of controls for this partial declaration.
     */
    public void setControls(List<ControlDef> controls)
    {
        this.controls = controls;
    }

    /**
     * The list of controls templates for this partial declaration.
     * 
     * @return The list of controls templates for this partial declaration.
     */
    public List<TemplateControlDef> getTemplates()
    {
        return templates;
    }

    /**
     * The list of controls templates for this partial declaration.
     * 
     * @param templates The list of controls templates for this partial declaration.
     */
    public void setTemplates(List<TemplateControlDef> templates)
    {
        this.templates = templates;
    }

    /**
     * The list of freemarker templates to be includes.
     * 
     * @return The list of freemarker templates to be includes.
     */
    public List<String> getFtlIncludes()
    {
        return ftlIncludes;
    }

    /**
     * The list of freemarker templates to be includes.
     * 
     * @param ftlIncludes The list of freemarker templates to be includes.
     */
    public void setFtlIncludes(List<String> ftlIncludes)
    {
        this.ftlIncludes = ftlIncludes;
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
     * Loads a PartialUISuite from a file.
     * 
     * @param xmlFile The file to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     * @throws IOException If any IO Exception occurs.
     */
    public static PartialUISuite load(VFile xmlFile) throws JAXBException, IOException
    {
        if(!xmlFile.exists()) return null;
        try(InputStream is = new VFileInputStream(xmlFile))
        {
            return load(is);
        }
    }

    /**
     * Loads a PartialUISuite from an input stream.
     * 
     * @param is The input stream to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static PartialUISuite load(InputStream is) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(PartialUISuite.class);
        return (PartialUISuite)ctx.createUnmarshaller().unmarshal(is);
    }

    /**
     * Save a PartialUISuite to an output stream.
     * 
     * @param os The output stream to write the object to.
     * @param object The object to write.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static void save(OutputStream os, PartialUISuite object) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(PartialUISuite.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, os);
    }
}
