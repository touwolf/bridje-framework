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

package org.bridje.web.view.themes;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.bridje.ioc.Component;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;

@Component
class ThemesTplLoader implements TemplateLoader
{
    private final Long time = System.currentTimeMillis();

    @Override
    public Object findTemplateSource(String name) throws IOException
    {
        VFile tpl = new VFile("/web/themes/" + name);
        if(!tpl.isFile()) return null;
        return new VFileInputStream(tpl);
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
