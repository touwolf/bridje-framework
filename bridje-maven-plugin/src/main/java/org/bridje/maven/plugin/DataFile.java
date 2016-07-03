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

package org.bridje.maven.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Represents a data file from witch the data will be taken to generate the
 * code.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataFile
{
    private String path;

    @XmlElementWrapper(name = "nodes")
    @XmlElements(
            {
                @XmlElement(name = "node", type = Node.class)
            })
    private Node[] nodes;

    private String defContent;

    /**
     * The path of the file.
     *
     * @return An String representing the path of the data file.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * The nodes expression for this data file.
     *
     * @return An array with all the xml nodes configuration for the code
     * generation.
     */
    public Node[] getNodes()
    {
        return nodes;
    }

    /**
     * Generates the files described by this data file.
     *
     * @param mojo The GenerateMojo instance.
     * @throws MojoExecutionException If any exception occurs.
     */
    protected void generate(GenerateMojo mojo) throws MojoExecutionException
    {
        try
        {
            if (path == null || nodes == null)
            {
                return;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(mojo.getDataFilesBasePath() + "/" + path));
            for (Node node : nodes)
            {
                node.generate(mojo, doc);
            }
        }
        catch (ParserConfigurationException | SAXException | IOException ex)
        {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    /**
     * Determines when ever the data file given by this object exists or not.
     *
     * @param mojo The GenerateMojo instance.
     * @return true the data file exists, false otherwise.
     */
    public boolean exists(GenerateMojo mojo)
    {
        return new File(mojo.getDataFilesBasePath() + "/" + path).exists();
    }

    /**
     * Create the data file specified by this object if it does not exists.
     *
     * @param mojo The GenerateMojo instance.
     * @throws IOException If any exception occurs.
     */
    public void create(GenerateMojo mojo) throws IOException
    {
        File f = new File(mojo.getDataFilesBasePath() + "/" + path);
        if (!f.exists())
        {
            f.createNewFile();
            if (getDefContent() != null)
            {
                try (FileOutputStream fos = new FileOutputStream(f))
                {
                    fos.write(getDefContent().getBytes());
                    fos.flush();
                }
            }
        }
    }

    /**
     * Gets the default content that this data object will have in case it need
     * to be created.
     * 
     * @return The default content o the file.
     */
    public String getDefContent()
    {
        return defContent;
    }

    /**
     * Sets the default content that this data object will have in case it need
     * to be created.
     * 
     * @param defContent The default content o the file.
     */
    public void setDefContent(String defContent)
    {
        this.defContent = defContent;
    }
}
