
package org.bridje.core.impl.tpl.ftl;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FtlVfsTemplateLoader implements TemplateLoader
{
    private final org.bridje.core.tpl.TemplateLoader tplLoader;

    public FtlVfsTemplateLoader(org.bridje.core.tpl.TemplateLoader tplLoader)
    {
        this.tplLoader = tplLoader;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException
    {
        return tplLoader.findTemplate(name);
    }

    @Override
    public long getLastModified(Object templateSource)
    {
        return 0L;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException
    {
        return new InputStreamReader(tplLoader.loadTemplate(templateSource));
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException
    {
    }
}
