
package org.bridje.vfs;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import org.bridje.ioc.Ioc;

public class VFile
{
    private static VfsService VFS;

    private static VfsService getVfs()
    {
        if(VFS == null)
        {
            VFS = Ioc.context().find(VfsService.class);
        }
        return VFS;
    }

    private final Path path;

    public VFile(Path path)
    {
        this.path = path;
    }

    public VFile(String path)
    {
        this.path = new Path(path);
    }

    public Path getPath()
    {
        return path;
    }
    
    public VFile getParent()
    {
        if(path.isRoot()) return null;
        return new VFile(path.getParent());
    }

    public boolean isDirectory()
    {
        return getVfs().isDirectory(path);
    }

    public boolean exists()
    {
        return getVfs().exists(path);
    }
    
    public boolean createNewFile()
    {
        return getVfs().createNewFile(path);
    }

    public boolean delete()
    {
        return getVfs().delete(path);
    }

    public boolean mkdir()
    {
        return getVfs().mkdir(path);
    }

    public boolean isFile()
    {
        return getVfs().isFile(path);
    }

    public boolean canWrite()
    {
        return getVfs().canWrite(path);
    }

    public boolean canRead()
    {
        return getVfs().canRead(path);
    }

    public void mount(VfsSource source) throws FileNotFoundException
    {
        getVfs().mount(path, source);
    }
    
    public String[] list()
    {
        return getVfs().list(path);
    }

    public VFile[] listFiles()
    {
        String[] list = getVfs().list(path);
        VFile[] result = new VFile[list.length];
        for (int i = 0; i < list.length; i++)
        {
            String name = list[i];
            result[i] = new VFile(path.join(name));
        }
        return result;
    }

    InputStream openForRead()
    {
        return getVfs().openForRead(path);
    }

    OutputStream openForWrite()
    {
        return getVfs().openForWrite(path);
    }

    public VFile[] search(GlobExpr globExpr)
    {
        return getVfs().search(globExpr, path);
    }

    public String getMimeType()
    {
        String ext = path.getExtension();
        if(ext == null || ext.trim().isEmpty()) return null;
        return getVfs().getMimeType(ext);
    }
    
    @Override
    public String toString()
    {
        return path.toString();
    }

    @Override
    public int hashCode()
    {
        return this.path.hashCode();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        final VFile other = (VFile) obj;
        return Objects.equals(this.path, other.path);
    }
}
