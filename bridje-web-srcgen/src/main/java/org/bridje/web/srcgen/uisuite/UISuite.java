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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;
import org.w3c.dom.Document;

/**
 * An UI suite definition that can be use to generate the controls and templates 
 * to be use by the web API to render the view of an application.
 */
@XmlRootElement(name = "uisuite")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={
    "name", 
    "packageName",
    "namespace",
    "renderView",
    "renderBody",
    "renderHead",
    "includes",
    "standalone",
    "defines",
    "defaultResources",
    "resources",
    "ftlImports",
    "ftlMacros",
    "ftlFunctions",
    "ftlIncludes",
    "fields",
    "templates",
    "controls",
    "enums"
})
public class UISuite extends UISuiteBase
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute(name = "package")
    private String packageName;
    
    @XmlAttribute
    private String namespace;

    private String renderView;

    private String renderBody;

    private String renderHead;
    
    private StandaloneDef standalone;
    
    private StandaloneDef defines;

    private Resource defaultResources;

    @XmlElementWrapper(name = "fields")
    @XmlElements(
    {
        @XmlElement(name = "outAttr", type = OutAttrField.class),
        @XmlElement(name = "attr", type = AttrField.class),
        @XmlElement(name = "outEl", type = OutElementField.class),
        @XmlElement(name = "el", type = ElementField.class),
        @XmlElement(name = "outValue", type = OutValueField.class),
        @XmlElement(name = "value", type = ValueField.class),
        @XmlElement(name = "child", type = ChildField.class),
        @XmlElement(name = "children", type = ChildrenField.class)
    })
    private List<FieldDef> fields;
    
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
     * @throws javax.xml.parsers.ParserConfigurationException If any Exception occurs writing the suite.
     * @throws javax.xml.transform.TransformerConfigurationException If any Exception occurs writing the suite.
     */
    public static void save(OutputStream os, UISuite object) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        JAXBContext ctx = JAXBContext.newInstance(UISuite.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = docBuilderFactory.newDocumentBuilder().newDocument();
        marshaller.marshal(object, document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer nullTransformer = transformerFactory.newTransformer();
        nullTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        nullTransformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"yes");
        nullTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        nullTransformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "render renderHead renderBody renderView");
        nullTransformer.transform(new DOMSource(document), new StreamResult(os));
    }

    public List<FieldDef> getFields()
    {
        return fields;
    }

    public void setFields(List<FieldDef> fields)
    {
        this.fields = fields;
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
}
