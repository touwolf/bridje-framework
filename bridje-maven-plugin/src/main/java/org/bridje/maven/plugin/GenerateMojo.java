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

import org.bridje.maven.plugin.model.hierarchical.HModel;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bridje.maven.plugin.model.hierarchical.HEntity;
import org.bridje.maven.plugin.model.hierarchical.HEntityBase;
import org.bridje.maven.plugin.model.hierarchical.HEnum;

/**
 * This is the maven mojo for generate hierarchy classes for the given model.
 * <p>
 * <b>EXAMPLE</b>
 * <p>
 * <code>
 *  <pre>&lt;plugin&gt;
 *      &lt;groupId&gt;org.bridje&lt;/groupId&gt;
 *      &lt;artifactId&gt;bridje-maven-plugin&lt;/artifactId&gt;
 *      &lt;version&gt;0.0.3-SNAPSHOT&lt;/version&gt;
 *      &lt;executions&gt;
 *          &lt;execution&gt;
 *              &lt;id&gt;generate-hierarchy&lt;/id&gt;
 *              &lt;goals&gt;
 *                  &lt;goal&gt;generate-hierarchy&lt;/goal&gt;
 *              &lt;/goals&gt;
 *              &lt;phase&gt;generate-sources&lt;/phase&gt;
 *          &lt;/execution&gt;
 *      &lt;/executions&gt;
 *  &lt;/plugin&gt;</pre>
 * <code>
 */
@Mojo(name = "generate-hierarchy")
public class GenerateMojo extends AbstractMojo
{

    @Parameter(property = "project", defaultValue = "${project}")
    private MavenProject project;

    @Parameter(property = "generate.domainModel", defaultValue = "${basedir}/src/main/resources/BRIDJE-INF/models/hierarchical.xml")
    private File source;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/data", readonly = false)
    private File target;

    @Parameter(defaultValue = "src/main/java", readonly = false)
    private File javaSources;

    @Parameter(defaultValue = "${project.build.directory}/generated-resources/data", readonly = false)
    private File resources;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info("Generating ORM Classes");
        getLog().info("Source: " + source.getPath());
        getLog().info("Target: " + target.getPath());

        try
        {
            FileGenerator generator = new FileGenerator(target, true);
            FileGenerator srcGenerator = new FileGenerator(javaSources, false);
            FileGenerator rcGenerator = new FileGenerator(resources, true);

            HModel model = HModel.loadModel(getSource());

            if (model != null)
            {
                List<GenerateFileData> toGenerate = createFilesToGenerate(model);
                for (GenerateFileData fData : toGenerate)
                {
                    getLog().info("Generating: " + fData.getDest());
                    generator.generateFile(fData);
                }

                List<GenerateFileData> toSources = createFilesToSources(model);
                for (GenerateFileData fData : toSources)
                {
                    getLog().info("Generating: " + fData.getDest());
                    srcGenerator.generateFile(fData);
                }

                List<GenerateFileData> toResources = createFilesToResources(model);
                for (GenerateFileData fData : toResources)
                {
                    getLog().info("Generating: " + fData.getDest());
                    rcGenerator.generateFile(fData);
                }

                project.addCompileSourceRoot(target.getPath());
                Resource r = new Resource();
                r.setDirectory(resources.getAbsolutePath());
                project.addResource(r);
            }
        }
        catch (JAXBException | TemplateException | IOException ex)
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

    private List<GenerateFileData> createFilesToGenerate(HModel model)
    {
        List<GenerateFileData> result = new ArrayList<>();
        result.add(createGenerateFileData(model, "HModelParent.java", findGenFileName(model) + ".java", model.getPackage()));
        List<HEntity> entitys = model.getEntitys();
        List<HEnum> enums = model.getEnums();
        if (entitys != null)
        {
            for (HEntity entity : entitys)
            {
                result.add(createGenerateFileData(entity, "HEntityParent.java", findGenFileName(entity) + ".java", model.getPackage()));
            }
        }
        if (enums != null)
        {
            for (HEnum en : enums)
            {
                result.add(createGenerateFileData(en, "HEnum.java", en.getName() + ".java", model.getPackage()));
            }
        }
        return result;
    }

    private GenerateFileData createGenerateFileData(Object data, String tpl, String name, String pack)
    {
        String path = pack.replaceAll("[\\.]", "/") + "/" + name;
        return new GenerateFileData(data, tpl + ".ftl", path);
    }

    private String findGenFileName(HEntityBase entity)
    {
        String entityName = entity.getName();
        if (entity.getCustomizable())
        {
            entityName = entity.getName() + "Parent";
        }
        return entityName;
    }

    private List<GenerateFileData> createFilesToSources(HModel model)
    {
        List<GenerateFileData> result = new ArrayList<>();
        if (model.getCustomizable())
        {
            result.add(createGenerateFileData(model, "HModel.java", model.getName() + ".java", model.getPackage()));
        }
        List<HEntity> entitys = model.getEntitys();
        List<HEnum> enums = model.getEnums();
        if (entitys != null)
        {
            for (HEntity entity : entitys)
            {
                if (entity.getCustomizable())
                {
                    result.add(createGenerateFileData(entity, "HEntity.java", entity.getName() + ".java", model.getPackage()));
                }
            }
        }
        return result;
    }

    public List<GenerateFileData> createFilesToResources(HModel model)
    {
        List<GenerateFileData> result = new ArrayList<>();
        result.add(createGenerateFileData(model, "schema.xsd", model.getName().toLowerCase() + ".xsd", "BRIDJE-INF/schemas"));
        return result;
    }

}
