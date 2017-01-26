
package org.bridje.vfs;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import org.bridje.ioc.Ioc;

/**
 * Represents a file of the VFS tree.
 */
public class VFile
{
    private static VfsService VFS;
    
    
    /**
     * Return VfsService component, after we can call of this component to all the methods.
     * <p>
     * @return VfsService component.
     */

    private static VfsService getVfs()
    {
        if(VFS == null)
        {
            VFS = Ioc.context().find(VfsService.class);
        }
        return VFS;
    }

    private final Path path;
    
    /**
     * Constructor of this class.
     * <p>
     * @param path The Path object that represent the file or folder.
     */

    public VFile(Path path)
    {
        this.path = path;
    }
    
    /**
     * Another constructor of this class.
     * <p>
     * @param path The string representation of the path (file or folder).
     */

    public VFile(String path)
    {
        this.path = new Path(path);
    }
       
    
    /**
     * Call to getName method of Path class.
     * <p>
     * @return A String object that represents the first element of the path.
     */

    public String getName()
    {
        return path.getName();
    }
    
    /**
     * Return the path representation of the path attribute
     * <p>
     * @return A Path object. 
     */

    public Path getPath()
    {
        return path;
    }
    
    /**
     * Return VFile object that represent the parent of the file in the VFS tree.
     * <p>
     * For example, if the path represented by this object equals to {@literal "usr/local/somefile"},
     * this method will return a VFile object where the path attribute has value{@literal "usr/local"}.
     * <p>
     * @return A VFile object, this is the parent of the file. 
     */
    
    public VFile getParent()
    {
        if(path.isRoot()) return null;
        return new VFile(path.getParent());
    }
    
    /**
     * Check if the node is a directory 
     * <p>
     * @return Boolean, true if the node is a directory and false otherwise. 
     */

    public boolean isDirectory()
    {
        return getVfs().isDirectory(path);
    }
    
    /**
     * Check if the file exist really. 
     * <p>
     * @return Boolean, true if the file exist and false otherwise. 
     */

    public boolean exists()
    {
        return getVfs().exists(path);
    }
    
    /**
     * Create a new file. 
     * <p>
     * @return Boolean, true if the file is created without problem and false otherwise.
     */
    
    public boolean createNewFile()
    {
        return getVfs().createNewFile(path);
    }
    
    
    /**
     * Delete a file in the VFS tree. 
     * <p>
     * @return Boolean, true if the file is deleted without problem and false otherwise.
     */

    public boolean delete()
    {
        return getVfs().delete(path);
    }
    
    
    /**
     * Create a new directory. 
     * <p>
     * @return Boolean, true if the directory is created without problem and false otherwise.
     */

    public boolean mkdir()
    {
        return getVfs().mkdir(path);
    }
    
    
    /**
     * Check if the node is a file. 
     * <p>
     * @return Boolean, true if the node is a file and false otherwise. 
     */

    public boolean isFile()
    {
        return getVfs().isFile(path);
    }
    
    /**
     * Check if we can write on this node.
     * <p>
     * @return Boolean, true if we can write and false otherwise. 
     */

    public boolean canWrite()
    {
        return getVfs().canWrite(path);
    }
    
    /**
     * Check if we can read on this node.
     * <p>
     * @return Boolean, true if we can read and false otherwise. 
     */

    public boolean canRead()
    {
        return getVfs().canRead(path);
    }
    
     /**
     * Mount a node(file or folder) on VFS tree.
     * for example, Let's suppose that we want to mount the folder ("/etc/resources"),
     * the call to the method:  mount(new FileSource(new File("/etc/resources")))
     * @param source VfsSource object
     * <p>
     */

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
