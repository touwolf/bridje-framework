
package org.bridje.vfs.impl;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.ioc.PostConstruct;
import org.bridje.vfs.*;

@Component
class VfsServiceImpl implements VfsService
{
    private static final Logger LOG = Logger.getLogger(VfsServiceImpl.class.getName());

    private final VfsFolderNode root;

    private final Properties mimeTypes;

    public VfsServiceImpl()
    {
        this.root = new VfsFolderNode(null);
        this.mimeTypes = new Properties();
    }

    @PostConstruct
    public synchronized void init() throws IOException, URISyntaxException
    {
        VFile vfsBridje = new VFile("/vfs/bridje");
        vfsBridje.mount(new CpSource("/BRIDJE-INF/vfs"));
        VFile mimeTypesFile = new VFile(vfsBridje.getPath().join("mime-types.properties"));
        try(VFileInputStream is = new VFileInputStream(mimeTypesFile))
        {
            this.mimeTypes.load(is);
        }
        VFile[] sources = vfsBridje.search(new GlobExpr("*-classpath-sources.properties"));
        for (VFile source : sources)
        {
            try(VFileInputStream is = new VFileInputStream(source))
            {
                Properties prop = new Properties();
                prop.load(is);
                Set<Map.Entry<Object, Object>> entrySet = prop.entrySet();
                for (Map.Entry<Object, Object> entry : entrySet)
                {
                    try
                    {
                        VFile folder = new VFile((String)entry.getKey());
                        folder.mount(new CpSource((String)entry.getValue()));
                    }
                    catch (IOException | URISyntaxException e)
                    {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            }
            catch (IOException ex)
            {
                LOG.log(Level.SEVERE, "Reading " + source.getName() + ": " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public synchronized void mount(Path path, VfsSource source) throws FileNotFoundException
    {
        root.mount(path, source);
    }

    @Override
    public synchronized void unmount(Path path) throws FileNotFoundException
    {
        root.unmount(path);
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
    public boolean delete(Path path)
    {
        return root.delete(path);
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

    @Override
    public File getRawFile(Path path)
    {
        return root.getRawFile(path);
    }
}
