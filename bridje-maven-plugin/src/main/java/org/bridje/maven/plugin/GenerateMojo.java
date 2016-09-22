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

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.classworlds.DuplicateRealmException;

/**
 * This MOJO is responsible for generating the code specified by the other APIs.
 * Each API defines the code that it needs and the user need`s only to define the 
 * data for the code.
 */
@Mojo(
    name = "generate-sources"
)
public class GenerateMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project.basedir}/src/main/bridje", readonly = false)
    private String dataFilesBasePath;

    @Parameter(defaultValue="${project.build.directory}/generated-sources/bridje", readonly = false)
    private String outputBasePath;

    @Parameter(defaultValue="${project}", readonly=true, required=true)
    private MavenProject project;

    @Parameter(defaultValue = "true", readonly = false)
    private boolean autoCreate;

    private ClassLoader clsRealm;

    private Configuration cfg;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File f = new File(getDataFilesBasePath());
        if(!f.exists())
        {
            if(autoCreate)
            {
                f.getAbsoluteFile().mkdirs();
            }
        }

        try
        {
            clsRealm = ClassPathUtils.createClassPath(project);
            createFreeMarkerConfiguration();
        }
        catch (IOException | DuplicateRealmException | DependencyResolutionRequiredException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        getLog().info("Generating Classes");
        try
        {
            List<GenerationConfig> genConfigs = readAllConfigs();
            for (GenerationConfig genConfig : genConfigs)
            {
                DataFile[] df = genConfig.getDataFiles();
                if(df != null)
                {
                    for (DataFile dataFile : df)
                    {
                        try
                        {
                            if(!dataFile.exists(this))
                            {
                                if(autoCreate)
                                {
                                    dataFile.create(this);
                                }
                            }
                            dataFile.generate(this);
                        }
                        catch (IOException e)
                        {
                            throw new MojoExecutionException(e.getMessage(), e);
                        }
                    }
                }
            }
            project.addCompileSourceRoot(outputBasePath);
        }
        catch (MojoExecutionException e)
        {
            getLog().error(e.getMessage(), e);
            throw  e;
        }
    }

    /**
     * The current data files input folder path for this mojo.
     * 
     * @return The current data files input folder path for this mojo.
     */
    protected String getDataFilesBasePath()
    {
        return dataFilesBasePath;
    }

    /**
     * The current output folder path for this mojo.
     * 
     * @return The current output folder path for this mojo.
     */
    protected String getOutputBasePath()
    {
        return outputBasePath;
    }

    /**
     * The current maven project.
     * 
     * @return The maven project.
     */
    protected MavenProject getProject()
    {
        return project;
    }

    /**
     * Gets the current freemarker configuration object.
     * 
     * @return The freemarker Configuration object for this MOJO.
     */
    public Configuration getFreeMarkerConfiguration()
    {
        return cfg;
    }

    private void createFreeMarkerConfiguration() throws IOException
    {
        //Freemarker configuration
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        TemplateLoader cpLoader = new ClassTemplateLoader(clsRealm, "/BRIDJE-INF/srcgen/");
        cfg.setTemplateLoader(cpLoader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
    }

    private List<GenerationConfig> readAllConfigs()
    {
        List<GenerationConfig> result = new LinkedList<>();
        String file = "BRIDJE-INF/srcgen/generation.xml";
        try
        {
            Enumeration<URL> resources = clsRealm.getResources(file);
            while (resources.hasMoreElements())
            {
                URL url = resources.nextElement();
                GenerationConfig cfg = readConfig(url);
                if(cfg != null)
                {
                    result.add(cfg);
                }
            }
        }
        catch (IOException | JAXBException ex)
        {
            getLog().error(ex.getMessage(), ex);
        }
        return result;
    }

    private GenerationConfig readConfig(URL url) throws JAXBException, IOException
    {
        JAXBContext ctx = JAXBContext.newInstance(GenerationConfig.class);
        Unmarshaller unm = ctx.createUnmarshaller();
        Object result = unm.unmarshal(url.openStream());
        return (GenerationConfig)result;
    }

    public Enumeration<URL> findResources(String resource) throws IOException
    {
        return clsRealm.getResources(resource);
    }

    public Reader findScript(String script)
    {
        try
        {
            String file = "BRIDJE-INF/srcgen/" + script;
            URL resources = clsRealm.getResource(file);
            return new InputStreamReader(resources.openStream());
        }
        catch (IOException ex)
        {
            getLog().error(ex.getMessage());
        }
        return null;
    }
}
