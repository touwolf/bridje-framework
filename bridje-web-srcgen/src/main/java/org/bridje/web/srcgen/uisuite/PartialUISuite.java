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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * A partial UI suite definition.
 */
@XmlRootElement(name = "partialuisuite")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={
    "includes",
    "standalone",
    "defines",
    "resources",
    "ftlImports",
    "ftlMacros",
    "ftlFunctions",
    "ftlIncludes",
    "templates",
    "controls",
    "enums"
})
public class PartialUISuite extends UISuiteBase
{
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
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws javax.xml.transform.TransformerConfigurationException
     */
    public static void save(OutputStream os, PartialUISuite object) throws JAXBException, ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        JAXBContext ctx = JAXBContext.newInstance(PartialUISuite.class);
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
}
