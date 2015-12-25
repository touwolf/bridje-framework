
package org.bridje.core.impl.tpl.ftl;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.bridje.core.ioc.Ioc;
import org.bridje.core.vfs.Path;
import org.bridje.core.vfs.VfsService;
import org.bridje.core.vfs.VirtualFile;

public class VfsTemplateLoader implements TemplateLoader
{
    private final VfsService vfsService;

    private final Path basePath;

    public VfsTemplateLoader(String basePath)
    {
        vfsService = Ioc.context().find(VfsService.class);
        this.basePath = new Path(basePath).getCanonicalPath();
    }

    @Override
    public Object findTemplateSource(String name) throws IOException
    {
        return vfsService.findFile(basePath.join(new Path(name)));
    }

    @Override
    public long getLastModified(Object templateSource)
    {
        return 0L;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException
    {
        if (templateSource == null)
        {
            return null;
        }
        VirtualFile vFile = (VirtualFile) templateSource;
        return new InputStreamReader(vFile.open());
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException
    {
    }
}
