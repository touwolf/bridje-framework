
package org.bridje.vfs.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsSource;

class VfsFolderNode extends VfsNode
{
    private final List<VfsNode> childs = new ArrayList<>();

    private final Map<String, VfsNode> childsMap = new HashMap<>();

    public VfsFolderNode(String name)
    {
        super(name);
    }

    public Iterable<VfsNode> getChilds()
    {
        return childs;
    }

    private VfsNode getChild(String name)
    {
        return childsMap.get(name);
    }

    private void addChild(VfsNode node)
    {
        node.setParent(this);
        childs.add(node);
        childsMap.put(node.getName(), node);
    }

    public void removeChild(VfsNode node)
    {
        node.setParent(this);
        childs.remove(node);
        childsMap.remove(node.getName());
    }

    public void mount(Path path, VfsSource source) throws FileNotFoundException
    {
        if(path == null || path.isRoot()) throw new FileNotFoundException("Could not mount the source in this folder.");
        if(path.isLast())
        {
            mountLast(path, source);
        }
        else
        {
            mountFirst(path, source);
        }
    }

    private void mountLast(Path path, VfsSource source) throws FileNotFoundException
    {
        VfsNode child = getChild(path.getName());
        if(child == null)
        {
            VfsSourceNodeProxy proxy = new VfsSourceNodeProxy(path.getName());
            addChild(proxy);
            proxy.add(source);
        }
        else
        {
            if(child instanceof VfsSourceNodeProxy)
            {
                ((VfsSourceNodeProxy) child).add(source);
            }
            else if(child instanceof VfsSourceNode)
            {
                removeChild(child);
                addChild(new VfsSourceNode(path.getName(), source));
            }
            else
            {
                throw new FileNotFoundException("Could not mount the source in " + getPath() + " folder.");
            }
        }
    }

    private void mountFirst(Path path, VfsSource source) throws FileNotFoundException
    {
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        if(child == null)
        {
            child = new VfsFolderNode(first);
            addChild(child);
            ((VfsFolderNode)child).mount(path.getNext(), source);
        }
        else
        {
            if(child instanceof VfsFolderNode)
            {
                ((VfsFolderNode)child).mount(path.getNext(), source);
            }
            else
            {
                throw new FileNotFoundException("Could not find the folder.");
            }
        }
    }

    @Override
    public boolean isDirectory(Path path)
    {
        if(path == null || path.isRoot())return true;
        VfsNode child = getChild(path.getFirstElement());
        return child != null && child.isDirectory(path.getNext());
    }

    @Override
    public boolean isFile(Path path)
    {
        if(path == null || path.isLast()) return false;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        return child != null && child.isFile(path.getNext());
    }

    @Override
    public boolean exists(Path path)
    {
        if(path == null || path.isLast()) return getChild(path.getName()) != null;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        return child != null && child.exists(path.getNext());
    }

    @Override
    public boolean canWrite(Path path)
    {
        if(path == null || path.isRoot())return false;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        return child.canWrite(path.getNext());
    }

    @Override
    public boolean canRead(Path path)
    {
        if(path == null || path.isRoot()) return false;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        return child.canRead(path.getNext());
    }

    @Override
    public String[] list(Path path)
    {
        if(path == null || path.isRoot())
        {
            String[] result = new String[childs.size()];
            childs.stream().map(VfsNode::getName).collect(Collectors.toList()).toArray(result);
            return result;
        }
        else
        {
            String first = path.getFirstElement();
            VfsNode child = getChild(first);
            return child.list(path.getNext());
        }
    }

    @Override
    public InputStream openForRead(Path path)
    {
        if(path == null || path.isRoot()) return null;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        if(child == null) return null;
        return child.openForRead(path.getNext());
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        if(path == null || path.isRoot()) return null;
        String first = path.getFirstElement();
        VfsNode child = getChild(first);
        if(child == null) return null;
        return child.openForWrite(path.getNext());
    }

    @Override
    public VFile[] search(GlobExpr globExpr, Path path)
    {
        if(path == null || path.isRoot()) return null;
        VfsNode child = getChild(path.getFirstElement());
        if(child == null) return null;
        return child.search(globExpr, path.getNext());
    }

    @Override
    public boolean createNewFile(Path path)
    {
        if(path == null || path.isRoot()) return false;
        VfsNode child = getChild(path.getFirstElement());
        return child != null && child.createNewFile(path.getNext());
    }

    @Override
    public boolean delete(Path path)
    {
        if(path == null || path.isRoot()) return false;
        VfsNode child = getChild(path.getFirstElement());
        return child != null && child.delete(path.getNext());
    }

    @Override
    public boolean mkdir(Path path)
    {
        if(path == null || path.isRoot()) return false;
        VfsNode child = getChild(path.getFirstElement());
        return child != null && child.mkdir(path.getNext());
    }
}
