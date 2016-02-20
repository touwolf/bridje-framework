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
import java.util.HashMap;
import java.util.Map;
import org.bridje.tpl.TemplateLoader;
import org.bridje.tpl.TplContext;
import org.bridje.tpl.TplEngine;
import org.bridje.tpl.TplEngineContext;
import org.bridje.tpl.TplNotFoundException;

public class TplContextImpl implements TplContext
{
    private final Map<String,TplEngineContext> extContexts;

    public TplContextImpl(TemplateLoader loader, TplEngine[] engines)
    {
        extContexts = createEngineContexts(loader, engines);
    }

    @Override
    public void render(String template, Map data, File file) throws TplNotFoundException, IOException
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            tplEngCtx.render(template, data, file);
        }
    }

    @Override
    public void render(String template, Map data, OutputStream os) throws TplNotFoundException, IOException
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            tplEngCtx.render(template, data, os);
        }
    }

    @Override
    public void render(String template, Map data, Writer writer) throws TplNotFoundException, IOException
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            tplEngCtx.render(template, data, writer);
        }
    }

    @Override
    public String render(String template, Map data) throws TplNotFoundException, IOException
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            return tplEngCtx.render(template, data);
        }
        return null;
    }

    private String findExtension(String template)
    {
        int lastIndexOf = template.lastIndexOf(".");
        if(lastIndexOf > 0 && lastIndexOf < template.length())
        {
            return template.substring(lastIndexOf + 1);
        }
        return null;
    }

    private TplEngineContext findTplEngineContext(String template)
    {
        String ext = findExtension(template);
        if(ext != null && !ext.isEmpty())
        {
            return extContexts.get(ext);
        }
        return null;
    }

    private Map<String,TplEngineContext> createEngineContexts(TemplateLoader loader,TplEngine[] engines)
    {
        Map<String,TplEngineContext> result = new HashMap<>();
        for (TplEngine engine : engines)
        {
            if(engine.getExtension() != null && !engine.getExtension().isEmpty())
            {
                result.put(engine.getExtension(), engine.createContext(loader));
            }
        }
        return result;
    }

    @Override
    public boolean exists(String template)
    {
        TplEngineContext tplEngCtx = findTplEngineContext(template);
        if(tplEngCtx != null)
        {
            return tplEngCtx.exists(template);
        }
        return false;
    }
}
