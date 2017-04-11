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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bridje.ioc.Ioc;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.FileSource;
import org.bridje.vfs.VFile;

/**
 * This MOJO is responsible for launching the source generation data editor.
 */
@Mojo(
        name = "launch-editor"
)
public class LaunchEditorMojo extends AbstractMojo
{
    @Parameter(defaultValue = "src/main/resources/BRIDJE-INF/srcgen/data", readonly = false)
    private File sourceFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/bridje", readonly = false)
    private File targetFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-resources/bridje", readonly = false)
    private File targetResFolder;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            getLog().info("Generating Source Code");
            if(!sourceFolder.exists()) return;
            if(!targetFolder.exists()) targetFolder.mkdirs();
            if(!targetResFolder.exists()) targetResFolder.mkdirs();
            new VFile(SrcGenService.DATA_PATH).mount(new FileSource(sourceFolder));
            new VFile(SrcGenService.CLASSES_PATH).mount(new FileSource(targetFolder));
            new VFile(SrcGenService.RESOURCE_PATH).mount(new FileSource(targetResFolder));
            SrcGenService srcGenServ = Ioc.context().find(SrcGenService.class);
            srcGenServ.launchEditor(new String[]{});
        }
        catch (IOException e)
        {
            getLog().error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
        catch (Exception e)
        {
            getLog().error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
