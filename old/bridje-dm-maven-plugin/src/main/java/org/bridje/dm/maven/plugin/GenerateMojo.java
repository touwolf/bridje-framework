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

package org.bridje.dm.maven.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bridje.dm.DataModel;
import org.bridje.dm.DataTypeBase;
import org.bridje.dm.Entity;
import org.bridje.dm.EntityBase;
import org.bridje.dm.EnumDataType;

/**
 *
 */
@Mojo(
    name = "generate-sources"
)
public class GenerateMojo extends AbstractMojo
{
    @Parameter( property = "generate.dataModel", defaultValue = "${project.basedir}/src/main/resources/BRIDJE-INF/dm/dataModel.xml" )
    private File source;
    
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/bridjedm", readonly = false)
    private File target;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            getLog().info("Generating Data Model Classes");
            getLog().info("Source: " + source.getPath());
            getLog().info("Target: " + target.getPath());
            
            DataModel dataModel = DataModel.read(new FileInputStream(source));
            
            FileGenerator fileGenerator = new FileGenerator();
            fileGenerator.getFiles().add(createModelFile(dataModel, "model.ftl"));
            for(EntityBase entBase : dataModel.getConcreteEntitys())
            {
                fileGenerator.getFiles().add(createEntityFile(dataModel, (Entity)entBase, "entity.ftl"));
            }
            for(DataTypeBase dataTypeBase : dataModel.getEnums())
            {
                fileGenerator.getFiles().add(createEnumFile(dataModel, (EnumDataType)dataTypeBase, "enum.ftl"));
            }
            fileGenerator.generate();
        }
        catch (Exception ex)
        {
            getLog().error(ex);
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    private GeneratedFile createClassFile(String packageName, String className, String template)
    {
        String filePath = String.format("%s/%s/%s.java", target.getAbsolutePath(), packageName.replace(".", "/"), className);
        return new GeneratedFile(new File(filePath), template);
    }

    private GeneratedFile createModelFile(DataModel dataModel, String template)
    {
        GeneratedFile result = createClassFile(dataModel.getPackage(), dataModel.getName(), template);
        result.getData().put("dataModel", dataModel);
        return result;
    }

    private GeneratedFile createEntityFile(DataModel dataModel, Entity entity, String template)
    {
        GeneratedFile result = createClassFile(dataModel.getPackage(), entity.getName(), template);
        result.getData().put("dataModel", dataModel);
        result.getData().put("entity", entity);
        return result;
    }

    private GeneratedFile createEnumFile(DataModel dataModel, EnumDataType enumDataType, String template)
    {
        GeneratedFile result = createClassFile(dataModel.getPackage(), enumDataType.getName(), template);
        result.getData().put("dataModel", dataModel);
        result.getData().put("enum", enumDataType);
        return result;
    }
}
