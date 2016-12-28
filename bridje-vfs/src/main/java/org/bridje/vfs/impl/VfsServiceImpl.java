
package org.bridje.vfs.impl;

import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VfsSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import org.bridje.ioc.Component;
import org.bridje.vfs.CpSource;
import org.bridje.vfs.VFileInputStream;

@Component
class VfsServiceImpl implements VfsService
{
    private final VfsFolderNode root;
    
    private final Properties mimeTypes;

    public VfsServiceImpl()
    {
        this.root = new VfsFolderNode(null);
        this.mimeTypes = new Properties();
    }

    public void init() throws IOException, URISyntaxException
    {
        this.root.mount(new Path("/vfs/org/bridje"), new CpSource("/BRIDJE-INF/vfs"));
        VFile mimeFiles = new VFile("/vfs/org/bridje/mime-types.properties");
        try(VFileInputStream is = new VFileInputStream(mimeFiles))
        {
            this.mimeTypes.load(is);
        }
    }

    @Override
    public void mount(Path path, VfsSource source) throws FileNotFoundException
    {
        root.mount(path, source);
    }

    @Override
    public boolean isDirectory(Path path)
    {
        return root.isDirectory(path);
    }

    @Override
    public boolean isFile(Path path)
    {
        return root.isFile(path);
    }

    @Override
    public boolean exists(Path path)
    {
        return root.exists(path);
    }

    @Override
    public boolean canWrite(Path path)
    {
        return root.canWrite(path);
    }

    @Override
    public boolean canRead(Path path)
    {
        return root.canRead(path);
    }

    @Override
    public String[] list(Path path)
    {
        return root.list(path);
    }

    @Override
    public InputStream openForRead(Path path)
    {
        return root.openForRead(path);
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        return root.openForWrite(path);
    }

    @Override
    public VFile[] search(GlobExpr globExpr, Path path)
    {
        return root.search(globExpr, path);
    }

    @Override
    public boolean createNewFile(Path path)
    {
        return root.createNewFile(path);
    }

    @Override
    public boolean mkdir(Path path)
    {
        return root.mkdir(path);
    }
    
    @Override
    public String getMimeType(String extension)
    {
        return (String)mimeTypes.get(extension);
    }
}
