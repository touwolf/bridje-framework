
package org.bridje.vfs.impl;

import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import java.io.InputStream;
import java.io.OutputStream;

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
    
    abstract VFile[] search(GlobExpr globExpr, Path path);

    abstract boolean isDirectory(Path path);

    abstract boolean isFile(Path path);

    abstract boolean exists(Path path);

    abstract boolean canWrite(Path path);

    abstract boolean canRead(Path path);
    
    abstract String[] list(Path path);

    abstract InputStream openForRead(Path path);

    abstract OutputStream openForWrite(Path path);

    abstract boolean createNewFile(Path path);
    
    abstract boolean delete(Path path);

    abstract boolean mkdir(Path path);
}
