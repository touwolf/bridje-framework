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

import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 */
public class WriteFile
{
    private String name;
    
    private String template;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTemplate()
    {
        return template;
    }

    public void setTemplate(String template)
    {
        this.template = template;
    }

    public void generate(GenerateMojo mojo, Node node, Document doc) throws MojoExecutionException
    {
        try
        {
            NodeModel wrap = NodeModel.wrap(node);
            Map model = new HashMap();
            model.put("doc", NodeModel.wrap(doc));
            model.put("node", wrap);
            //Freemarker configuration
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            File tplFile = new File(template);
            cfg.setDirectoryForTemplateLoading(new File(mojo.getTemplatesBasePath()));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);

            //File name
            Template fileNameTpl = new Template(name, new StringReader(name), cfg);
            StringWriter out = new StringWriter();
            fileNameTpl.process(model, out);
            String fileName = out.toString();
            
            //Generate file
            File toGenerate = new File(mojo.getOutputBasePath() + "/" + fileName);
            if(toGenerate.exists())
            {
                toGenerate.delete();
            }
            toGenerate.getAbsoluteFile().getParentFile().mkdirs();
            toGenerate.createNewFile();
            Template tmpl = cfg.getTemplate(tplFile.getName());
            tmpl.process(model, new OutputStreamWriter(new FileOutputStream(toGenerate), "UTF-8"));
        }
        catch (TemplateException | IOException ex)
        {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }
}
