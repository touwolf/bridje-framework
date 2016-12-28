
package org.bridje.vfs.impl;

import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

class VfsSourceNode extends VfsNode
{
    private final VfsSource source;

    public VfsSourceNode(String name, VfsSource source)
    {
        super(name);
        this.source = source;
    }

    public VfsSource getSource()
    {
        return source;
    }

    @Override
    public boolean isDirectory(Path path)
    {
        return source.isDirectory(path);
    }

    @Override
    public boolean isFile(Path path)
    {
        return source.isFile(path);
    }

    @Override
    public boolean exists(Path path)
    {
        return source.exists(path);
    }

    @Override
    public boolean canWrite(Path path)
    {
        return source.canWrite(path);
    }

    @Override
    public boolean canRead(Path path)
    {
        return source.canRead(path);
    }

    @Override
    public String[] list(Path path)
    {
        return source.list(path);
    }

    @Override
    public InputStream openForRead(Path path)
    {
        return source.openForRead(path);
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        return source.openForWrite(path);
    }

    @Override
    public VFile[] search(GlobExpr globExpr, Path path)
    {
        GlobExpr expr = new GlobExpr(path.toString() + "/" + globExpr.getValue());
        List<Path> lst = source.search(expr, path);
        if(lst == null) return null;
        List<VFile> lstFiles = new ArrayList<>();
        VFile[] result = new VFile[lst.size()];
        for (Path p : lst)
        {
            lstFiles.add(new VFile(getPath().join(p)));
        }
        lstFiles.toArray(result);
        return result;
    }

    @Override
    public boolean createNewFile(Path path)
    {
        return source.createNewFile(path);
    }

    @Override
    public boolean mkdir(Path path)
    {
        return source.mkdir(path);
    }

    @Override
    public boolean delete(Path path)
    {
        return source.delete(path);
    }
}
