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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;

@XmlRootElement(name = "partialuisuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class PartialUISuite
{
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
     * 
     * @return 
     */
    public List<Resource> getResources()
    {
        return resources;
    }

    /**
     * 
     * @param resources 
     */
    public void setResources(List<Resource> resources)
    {
        this.resources = resources;
    }

    /**
     * 
     * @return 
     */
    public Resource getDefaultResources()
    {
        return defaultResources;
    }

    /**
     * 
     * @param defaultResources 
     */
    public void setDefaultResources(Resource defaultResources)
    {
        this.defaultResources = defaultResources;
    }

    /**
     * 
     * @return 
     */
    public List<ControlDef> getControls()
    {
        return controls;
    }

    /**
     * 
     * @param controls 
     */
    public void setControls(List<ControlDef> controls)
    {
        this.controls = controls;
    }

    /**
     * 
     * @return 
     */
    public List<ControlDef> getControlsTemplates()
    {
        return controlsTemplates;
    }

    /**
     * 
     * @param controlsTemplates 
     */
    public void setControlsTemplates(List<ControlDef> controlsTemplates)
    {
        this.controlsTemplates = controlsTemplates;
    }

    /**
     * 
     * @return 
     */
    public List<String> getFtlIncludes()
    {
        return ftlIncludes;
    }

    /**
     * 
     * @param ftlIncludes 
     */
    public void setFtlIncludes(List<String> ftlIncludes)
    {
        this.ftlIncludes = ftlIncludes;
    }

    /**
     * 
     * @param xmlFile 
     * @return 
     * @throws JAXBException 
     * @throws IOException 
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
     * 
     * @param is 
     * @return 
     * @throws JAXBException 
     */
    public static PartialUISuite load(InputStream is) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(PartialUISuite.class);
        return (PartialUISuite)ctx.createUnmarshaller().unmarshal(is);
    }

    /**
     * 
     * @param os 
     * @param config 
     * @throws JAXBException 
     */
    public static void save(OutputStream os, PartialUISuite config) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(PartialUISuite.class);
        ctx.createMarshaller().marshal(config, os);
    }
}
