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

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Inject;
import org.bridje.ioc.annotations.PostConstruct;
import org.bridje.tpl.TemplateLoader;
import org.bridje.tpl.TemplatesProvider;
import org.bridje.tpl.TplContext;
import org.bridje.tpl.TplEngine;
import org.bridje.tpl.TplEngineContext;
import org.bridje.tpl.TplService;

@Component
public class TplServiceImpl implements TplService
{
    @Inject
    private TemplatesProvider provider;
    
    @Inject
    private TplEngine[] engines;
    
    private Map<String,TplEngineContext> extContexts;

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
    public void render(String template, Map data, OutputStream os)
    {
        rootContext.render(template, data, os);
    }

    @Override
    public void render(String template, Map data, Writer writer)
    {
        rootContext.render(template, data, writer);
    }

    @Override
    public String render(String template, Map data)
    {
        return rootContext.render(template, data);
    }
    
}
