
package org.bridje.vfs.impl.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsSource;

public class FileVfsSource implements VfsSource
{
    private static final Logger LOG = Logger.getLogger(FileVfsSource.class.getName());

    private final File file;

    public FileVfsSource(File file)
    {
        this.file = file;
    }

    @Override
    public Object[] getFiles(Path path) throws IOException
    {
        File f = new File(file.getCanonicalPath() + File.separator + path.toString(File.separator));
        if(f.exists() && f.isFile() && f.canRead())
        {
            return new File[]{f};
        }
        return null;
    }

    @Override
    public List<String> listFolders(Path path) throws IOException
    {
        File f = findRealFile(path);
        if(!f.exists())
        {
            return null;
        }
        if(!f.isDirectory())
        {
            throw new IOException("Can´t open " + f.getCanonicalPath() + ". is not a folder.");
        }
        List<String> result = new LinkedList<>();
        File[] listFiles = f.listFiles();
        for (File listFile : listFiles)
        {
            if(listFile.isDirectory())
            {
                result.add(listFile.getName());
            }
        }
        return result;
    }

    @Override
    public List<String> listFiles(Path path) throws IOException
    {
        File f = findRealFile(path);
        if(!f.exists())
        {
            return null;
        }
        if(!f.isDirectory())
        {
            throw new IOException("Can´t open " + f.getCanonicalPath() + ". is not a folder.");
        }
        List<String> result = new LinkedList<>();
        File[] listFiles = f.listFiles();
        for (File listFile : listFiles)
        {
            if(listFile.isFile())
            {
                result.add(listFile.getName());
            }
        }
        return result;
    }

    @Override
    public boolean fileExists(Path path) throws IOException
    {
        File f = findRealFile(path);
        return (f.exists() && f.isFile());
    }

    @Override
    public boolean folderExists(Path path) throws IOException
    {
        File f = findRealFile(path);
        return (f.exists() && f.isDirectory());
    }

    private File findRealFile(Path path) throws IOException
    {
        File f = file;
        if(path != null)
        {
            f = new File(file.getCanonicalPath() + File.separator + path.toString(File.separator));
        }
        return f;
    }

    @Override
    public InputStream open(Object data)
    {
        try
        {
            File f = (File)data;
            return new FileInputStream(f);
        }
        catch (FileNotFoundException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
