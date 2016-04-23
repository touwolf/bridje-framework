/*
 * Copyright 2015 Bridje Framework.
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 */
@Mojo(
    name = "generate-sources"
)
public class GenerateMojo extends AbstractMojo
{
    @Parameter(defaultValue = "src/main/bridje", readonly = false)
    private String dataFilesBasePath;

    @Parameter(defaultValue = "src/main/bridje", readonly = false)
    private String templatesBasePath;

    @Parameter(defaultValue="${project.build.directory}/generated-sources/bridje", readonly = false)
    private String outputBasePath;
    
    @Parameter(readonly = false)
    private DataFile[] dataFiles;
    
    @Parameter(defaultValue="${project}", readonly=true, required=true)
    private MavenProject project;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            getLog().info("Generating Classes");
            for (DataFile dataFile : dataFiles)
            {
                dataFile.generate(this);
            }
            
            project.addCompileSourceRoot(outputBasePath);
        }
        catch (ParserConfigurationException ex)
        {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    public String getDataFilesBasePath()
    {
        return dataFilesBasePath;
    }

    public String getTemplatesBasePath()
    {
        return templatesBasePath;
    }

    public String getOutputBasePath()
    {
        return outputBasePath;
    }

    public DataFile[] getDataFiles()
    {
        return dataFiles;
    }

    public MavenProject getProject()
    {
        return project;
    }
}
