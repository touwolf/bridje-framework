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

package org.bridje.srcgen.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.VfsService;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;

@Component
public class SrcGenServicesImpl implements SrcGenService
{
    private static final Logger LOG = Logger.getLogger(SrcGenServicesImpl.class.getName());

    @Inject
    private VfsService vfs;

    @Inject
    private SrcGenTplLoader srcGenTpl;

    private Configuration ftlCfg;

    public void init()
    {
        ftlCfg = new Configuration(Configuration.VERSION_2_3_23);
        ftlCfg.setTemplateLoader(srcGenTpl);
        ftlCfg.setDefaultEncoding("UTF-8");
        ftlCfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        ftlCfg.setLogTemplateExceptions(false);
    }

    @Override
    public void createClass(String clsFullName, String tplPath, Object data) throws IOException
    {
        LOG.log(Level.INFO, "Generating Class {0} from {1}", new Object[]{clsFullName, tplPath});
        String clsPath = toClassPath(clsFullName);
        Path resultPath = CLASSES_PATH.join(clsPath);
        VFile file = vfs.findFile(resultPath);
        if(file != null)
        {
            file.delete();
        }
        file = vfs.findFolder(resultPath.getParent()).createNewFile(resultPath.getName());
        try(OutputStream os = file.openForWrite())
        {
            render(os, tplPath, data);
        }
    }

    @Override
    public void createResource(String resourcePath, String tplPath, Object data) throws IOException
    {
        LOG.log(Level.INFO, "Generating Resource {0} from {1}", new Object[]{resourcePath, tplPath});
        Path resultPath = RESOURCE_PATH.join(resourcePath);
        VFile file = vfs.findFile(resultPath);
        if(file != null)
        {
            file.delete();
        }
        file = vfs.createNewFile(resultPath);
        try(OutputStream os = file.openForWrite())
        {
            render(os, tplPath, data);
        }
    }
    
    @Override
    public <T> List<T> findData(String path, Class<T> cls) throws IOException
    {
        List<T> result = new ArrayList<>();
        List<VFile> files = vfs.findFolder(DATA_PATH).listFiles(path);
        for (VFile file : files)
        {
            LOG.log(Level.INFO, "Reading Data File {0}", file.getPath().toString());
            T data = file.readFile(cls);
            result.add(data);
        }
        return result;
    }

    @Override
    public <T> List<T> findSuplementaryData(String path, Class<T> cls) throws IOException
    {
        List<T> result = new ArrayList<>();
        List<VFile> files = vfs.findFolder(DATA_PATH).listFiles(path);
        for (VFile file : files)
        {
            LOG.log(Level.INFO, "Reading Suplementary Data File {0}", file.getPath().toString());
            T data = file.readFile(cls);
            result.add(data);
        }
        return result;
    }

    private String toClassPath(String clsFullName)
    {
        try
        {
            return clsFullName.replaceAll("[\\.]", "\\\\") + ".java";
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public void render(OutputStream os, String template, Object data)
    {
        try(Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            Template tpl = ftlCfg.getTemplate(template);
            tpl.process(data, w);
            w.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
