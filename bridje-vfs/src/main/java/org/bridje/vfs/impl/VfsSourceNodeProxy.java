package org.bridje.vfs.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsSource;

public class VfsSourceNodeProxy extends VfsNode
{
    private final List<VfsSourceNode> nodes;

    public VfsSourceNodeProxy(String name)
    {
        super(name);
        nodes = new ArrayList<>();
    }

    public void add(VfsSource source)
    {
        VfsSourceNode node = new VfsSourceNode(getName(), source);
        node.setParent(this.getParent());
        nodes.add(node);
    }

    @Override
    public VFile[] search(GlobExpr globExpr, Path path)
    {
        if (nodes.isEmpty())
        {
            return new VFile[0];
        }
        final List<VFile> files = new LinkedList<>();
        List<VfsSourceNode> reverseNodes = new LinkedList<>(nodes);
        Collections.reverse(reverseNodes);
        reverseNodes.forEach(node ->
        {
            VFile[] nodeFiles = node.search(globExpr, path);
            for (VFile file : nodeFiles)
            {
                if (!files.contains(file))
                {
                    files.add(file);
                }
            }
        });
        return files.toArray(new VFile[0]);
    }

    @Override
    public boolean isDirectory(Path path)
    {
        VfsSourceNode last = last();
        return last != null && last.isDirectory(path);
    }

    @Override
    public boolean isFile(Path path)
    {
        VfsSourceNode last = last();
        return last != null && last.isFile(path);
    }

    @Override
    public boolean exists(Path path)
    {
        if (nodes.isEmpty())
        {
            return false;
        }
        for (int i = nodes.size() - 1; i >= 0; i--)
        {
            if (nodes.get(i).exists(path))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canWrite(Path path)
    {
        VfsSourceNode last = last();
        return last != null && last.canWrite(path);
    }

    @Override
    public boolean canRead(Path path)
    {
        VfsSourceNode last = last();
        return last != null && last.canRead(path);
    }

    @Override
    public String[] list(Path path)
    {
        if (nodes.isEmpty())
        {
            return new String[0];
        }
        final List<String> list = new LinkedList<>();
        List<VfsSourceNode> reverseNodes = new LinkedList<>(nodes);
        Collections.reverse(reverseNodes);
        reverseNodes.forEach(node ->
        {
            String[] nodeList = node.list(path);
            for (String name : nodeList)
            {
                if (!list.contains(name))
                {
                    list.add(name);
                }
            }
        });
        return list.toArray(new String[0]);
    }

    @Override
    public InputStream openForRead(Path path)
    {
        if (nodes.isEmpty())
        {
            return null;
        }
        for (int i = nodes.size() - 1; i >= 0; i--)
        {
            if (nodes.get(i).exists(path))
            {
                return nodes.get(i).openForRead(path);
            }
        }
        return null;
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        if (nodes.isEmpty())
        {
            return null;
        }
        for (int i = nodes.size() - 1; i >= 0; i--)
        {
            if (nodes.get(i).exists(path))
            {
                return nodes.get(i).openForWrite(path);
            }
        }
        return null;
    }

    @Override
    public boolean createNewFile(Path path)
    {
        VfsSourceNode last = last();
        return last != null && last.createNewFile(path);
    }

    @Override
    public boolean delete(Path path)
    {
        VfsSourceNode last = last();
        return last != null && last.delete(path);
    }

    @Override
    public boolean mkdir(Path path)
    {
        VfsSourceNode last = last();
        return last != null && last.mkdir(path);
    }

    private VfsSourceNode last()
    {
        if (!nodes.isEmpty())
        {
            return nodes.get(nodes.size() - 1);
        }
        return null;
    }
}
