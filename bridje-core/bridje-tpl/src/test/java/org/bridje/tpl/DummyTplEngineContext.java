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

package org.bridje.tpl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Gilberto
 */
public class DummyTplEngineContext implements TplEngineContext
{
    private static final Logger LOG = Logger.getLogger(DummyTplEngineContext.class.getName());

    private TemplateLoader loader;

    public DummyTplEngineContext(TemplateLoader loader)
    {
        this.loader = loader;
    }

    @Override
    public void render(String template, Map data, OutputStream os)
    {
        InputStream is = loader.loadTemplate(template);
        try
        {
            IOUtils.copy(is, os);
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void render(String template, Map data, Writer writer)
    {
        InputStream is = loader.loadTemplate(template);
        try
        {
            IOUtils.copy(is, writer);
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public String render(String template, Map data)
    {
        StringWriter sw = new StringWriter();
        render(template, data, sw);
        return sw.toString();
    }
    
}
