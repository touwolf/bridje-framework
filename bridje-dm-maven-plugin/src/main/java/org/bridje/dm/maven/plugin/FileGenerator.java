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

package org.bridje.dm.maven.plugin;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 *
 */
public class FileGenerator
{
    private final Configuration cf;
    
    private final List<GeneratedFile> files;

    public FileGenerator()
    {
        this.cf = new Configuration(Configuration.VERSION_2_3_23);
        this.cf.setClassForTemplateLoading(FileGenerator.class, "/META-INF/templates/dm");
        this.cf.setDefaultEncoding("UTF-8");
        this.cf.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        files = new LinkedList<>();
    }
    
    public void generate() throws IOException, TemplateException
    {
        List<GeneratedFile> generated = new ArrayList<>();
        try
        {
            for (GeneratedFile gFile : files)
            {
                createFile(gFile.getOutputFile());
                Template template = this.cf.getTemplate(gFile.getTemplate());
                template.process(gFile.getData(), new FileWriter(gFile.getOutputFile()));
                generated.add(gFile);
            }
        }
        catch (IOException | TemplateException e)
        {
            throw e;
        }
        finally
        {
            files.removeAll(generated);
        }
    }

    public List<GeneratedFile> getFiles()
    {
        return files;
    }

    private void createFile(File outputFile) throws IOException
    {
        if(outputFile.exists())
        {
            return;
        }
        if(outputFile.getParentFile().exists())
        {
            return ;
        }
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();
    }
}
