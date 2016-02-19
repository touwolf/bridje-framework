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

package org.bridje.tpl.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.tpl.TemplateLoader;
import org.bridje.tpl.TemplatesProvider;
import org.bridje.tpl.TplContext;
import org.bridje.tpl.TplEngine;
import org.bridje.tpl.TplNotFoundException;
import org.bridje.tpl.TplService;

@Component
public class TplServiceImpl implements TplService
{
    private static final Logger LOG = Logger.getLogger(TplServiceImpl.class.getName());

    @Inject
    private TemplatesProvider provider;
    
    @Inject
    private TplEngine[] engines;

    private TplContext rootContext;
    
    @PostConstruct
    public void init()
    {
        TemplateLoader defTplLoader = provider.createDefaultLoader();
        rootContext = new TplContextImpl(defTplLoader, engines);
    }

    @Override
    public TplContext createTplContext(String path)
    {
        TemplateLoader loader = provider.createLoader(path);
        return new TplContextImpl(loader, engines);
    }

    @Override
    public void render(String template, Map data, File file) throws TplNotFoundException, IOException
    {
        rootContext.render(template, data, file);
    }

    @Override
    public void render(String template, Map data, OutputStream os) throws TplNotFoundException, IOException
    {
        rootContext.render(template, data, os);
    }

    @Override
    public void render(String template, Map data, Writer writer) throws TplNotFoundException, IOException
    {
        rootContext.render(template, data, writer);
    }

    @Override
    public String render(String template, Map data) throws TplNotFoundException, IOException
    {
        return rootContext.render(template, data);
    }

    @Override
    public boolean exists(String template)
    {
        return rootContext.exists(template);
    }
}
