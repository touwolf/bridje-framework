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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bridje.ioc.Ioc;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.CpSource;
import org.bridje.vfs.FileSource;
import org.bridje.vfs.VFile;

/**
 * This MOJO is responsible for generating the code specified by the other APIs.
 * Each API defines the code that it needs and the user need`s only to define
 * the data for the code.
 */
@Mojo(name = "generate-sources")
public class GenerateMojo extends AbstractMojo
{
    @Parameter(defaultValue = "src/main/resources/BRIDJE-INF/srcgen/data", readonly = false)
    private File dataFolder;

    @Parameter(defaultValue = "src/main/java", readonly = true)
    private File sourcesFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/bridje", readonly = false)
    private File targetFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-resources/bridje", readonly = false)
    private File targetResFolder;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            getLog().info("Generating Source Code");
            if(!dataFolder.exists()) return;
            if(!targetFolder.exists()) targetFolder.mkdirs();
            if(!targetResFolder.exists()) targetResFolder.mkdirs();
            new VFile(SrcGenService.DATA_PATH).mount(new FileSource(dataFolder));
            new VFile(SrcGenService.SUPL_PATH).mount(new CpSource("/BRIDJE-INF/srcgen/data"));
            if(sourcesFolder.exists()) new VFile(SrcGenService.SOURCES_PATH).mount(new FileSource(sourcesFolder));
            new VFile(SrcGenService.CLASSES_PATH).mount(new FileSource(targetFolder));
            new VFile(SrcGenService.RESOURCE_PATH).mount(new FileSource(targetResFolder));
            SourceGenerator<Object>[] generators = Ioc.context().findAll(SourceGenerator.class);
            for (SourceGenerator<Object> generator : generators)
            {
                Map<Object, VFile> datas = generator.findData();
                if(datas != null)
                {
                    for (Entry<Object, VFile> data : datas.entrySet())
                    {
                        generator.generateSources(data.getKey(), data.getValue());
                    }
                }
            }
            project.addCompileSourceRoot(targetFolder.getAbsolutePath());
            Resource res = new Resource();
            res.setDirectory(targetResFolder.getAbsolutePath());
            project.addResource(res);
        }
        catch (IOException e)
        {
            getLog().error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
        catch (URISyntaxException e)
        {
            getLog().error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
