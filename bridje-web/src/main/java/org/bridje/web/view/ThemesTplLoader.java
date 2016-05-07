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

package org.bridje.web.view;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VirtualFile;

@Component
class ThemesTplLoader implements TemplateLoader
{
    private static final Logger LOG = Logger.getLogger(ThemesTplLoader.class.getName());

    @Inject
    private VfsService vfs;
    
    private final Long time = System.currentTimeMillis();
    
    @Override
    public Object findTemplateSource(String name) throws IOException
    {
        VirtualFile tpl = vfs.findFile("/web/themes/" + name);
        if(tpl == null)
        {
            return null;
        }
        return tpl.open();
    }

    @Override
    public long getLastModified(Object templateSource)
    {
        return time;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException
    {
        InputStream is = (InputStream)templateSource;
        return new InputStreamReader(is);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException
    {
        InputStream is = (InputStream)templateSource;
        is.close();
    }
}
