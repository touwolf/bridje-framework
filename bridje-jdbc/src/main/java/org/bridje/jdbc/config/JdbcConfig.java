/*
 * Copyright 2016 Bridje Framework.
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
package org.bridje.jdbc.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

/**
 * Configuration object for the JdbcService. It specify all the datasources
 * available for the application. Users can specify their configurations by
 * putting the jdbc.xml file into the configuration folder.
 */
@XmlRootElement(name = "jdbc")
@XmlAccessorType(XmlAccessType.FIELD)
public class JdbcConfig
{
    @XmlElementWrapper(name = "datasources")
    @XmlElements(
            {
                @XmlElement(name = "datasource", type = DataSourceConfig.class)
            })
    private List<DataSourceConfig> dataSources;

    /**
     * Gets all the DataSourceConfig objects specified by the user in the
     * jdbc.xml configuration file.
     *
     * @return A list of DataSorucesConfig objects specified by the user.
     */
    public List<DataSourceConfig> getDataSources()
    {
        if (dataSources == null)
        {
            dataSources = new ArrayList<>();
        }
        return dataSources;
    }

    /**
     * Sets all the DataSourceConfig objects that can be user by the
     * JdbcService.
     *
     * @param dataSources A list of DataSorucesConfig objects to be use by the
     * JdbcService.
     */
    public void setDataSources(List<DataSourceConfig> dataSources)
    {
        this.dataSources = dataSources;
    }

    /**
     * Loads a HttpServerConfig from a file.
     * 
     * @param xmlFile The file to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     * @throws IOException If any IO Exception occurs.
     */
    public static JdbcConfig load(VFile xmlFile) throws JAXBException, IOException
    {
        if(!xmlFile.exists()) return null;
        try(InputStream is = new VFileInputStream(xmlFile))
        {
            return load(is);
        }
    }

    /**
     * Loads a JdbcConfig from an input stream.
     * 
     * @param is The input stream to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static JdbcConfig load(InputStream is) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(JdbcConfig.class);
        return (JdbcConfig)ctx.createUnmarshaller().unmarshal(is);
    }

    /**
     * Save a SipServerConfig to an output stream.
     * 
     * @param os The output stream to write the object to.
     * @param object The object to write.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static void save(OutputStream os, JdbcConfig object) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(JdbcConfig.class);
        ctx.createMarshaller().marshal(object, os);
    }
}
