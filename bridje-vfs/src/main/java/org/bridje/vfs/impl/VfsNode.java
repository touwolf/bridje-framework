
package org.bridje.vfs.impl;

import java.io.InputStream;
import java.io.OutputStream;
import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;

abstract class VfsNode
{
    private final String name;

    private VfsFolderNode parent;

    public VfsNode(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }

    public VfsFolderNode getParent()
    {
        return parent;
    }

    public void setParent(VfsFolderNode parent)
    {
        this.parent = parent;
    }

    public void removeFromParent()
    {
        if(parent != null)
        {
            parent.removeChild(this);
        }
    }
    
    public Path getPath()
    {
        if(name == null) return new Path();
        return getParentPath().join(name);
    }
    
    private Path getParentPath()
    {
        if(name == null) return new Path();
        if(parent == null) return new Path();
        return getParent().getPath();
    }
    
    protected abstract VFile[] search(GlobExpr globExpr, Path path);

    protected abstract boolean isDirectory(Path path);

    protected abstract boolean isFile(Path path);

    protected abstract boolean exists(Path path);

    protected abstract boolean canWrite(Path path);

    protected abstract boolean canRead(Path path);
    
    protected abstract String[] list(Path path);

    protected abstract InputStream openForRead(Path path);

    protected abstract OutputStream openForWrite(Path path);

    protected abstract boolean createNewFile(Path path);
    
    protected abstract boolean delete(Path path);

    protected abstract boolean mkdir(Path path);
}
