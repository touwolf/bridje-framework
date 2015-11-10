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

import org.bridje.maven.plugin.model.herarchical.HierarchicalModel;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author Gilberto
 */
@Mojo( name = "generate-hierarchy")
public class GenerateMojo extends AbstractMojo
{
    @Parameter( property = "project", defaultValue = "${project}" )
    private MavenProject project;

    @Parameter( property = "generate.domainModel", defaultValue = "${basedir}/src/main/resources/domainModel.xml" )
    private File source;
    
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/data", readonly = false)
    private File target;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info("Generating ORM Classes");
        getLog().info("Source: " + source.getPath());
        getLog().info("Target: " + target.getPath());
        
        try
        {
            FileGenerator generator = new FileGenerator(target);
            HierarchicalModel model = HierarchicalModel.loadModel(getSource());
            
            if(model != null)
            {
                generator.generateFile(model, "HierarchicalModel.java.ftl", model.getPackage().replaceAll("[\\.]", "/") + "/" + model.getName() + ".java");

                project.addCompileSourceRoot(target.getPath());
            }
        }
        catch(JAXBException | TemplateException | IOException ex)
        {
            getLog().error(ex.getMessage(), ex);
            throw new MojoFailureException(ex.getMessage());
        }
    }

    public File getSource()
    {
        return source;
    }

    public void setSource(File source)
    {
        this.source = source;
    }

    public File getTarget()
    {
        return target;
    }

    public void setTarget(File target)
    {
        this.target = target;
    }
}
