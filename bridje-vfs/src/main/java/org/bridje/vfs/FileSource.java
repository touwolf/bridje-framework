
package org.bridje.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A VfsSource base on phisical file system object.
 */
public class FileSource implements VfsSource
{
    private static final Logger LOG = Logger.getLogger(FileSource.class.getName());

    private final File file;

    /**
     * The only constructor for this class, with the base folder for it.
     * 
     * @param file The file object representing the phisical folder for 
     * this VfsSource.
     */
    public FileSource(File file)
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
        return listFolders(path, null);
    }

    @Override
    public List<String> listFiles(Path path) throws IOException
    {
        return listFiles(path, null);
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

    @Override
    public InputStream openForRead(Object data) throws IOException
    {
        File f = (File)data;
        return new FileInputStream(f);
    }

    @Override
    public OutputStream openForWrite(Object data) throws IOException
    {
        File f = (File)data;
        return new FileOutputStream(f);
    }
    
    @Override
    public boolean canOpenForWrite(Object data)
    {
        return true;
    }

    @Override
    public Object createNewFile(Path filePath) throws IOException
    {
        try
        {
            File f = findRealFile(filePath);
            if(f != null && !f.exists())
            {
                f.getParentFile().getAbsoluteFile().mkdirs();
                f.createNewFile();
                return f.getName();
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        throw new IOException("Could not create the specified file.");
    }

    @Override
    public String mkDir(Path folderPath) throws IOException
    {
        try
        {
            File f = findRealFile(folderPath);
            if(f != null && !f.exists())
            {
                f.mkdirs();
                return f.getName();
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        throw new IOException("Could not create the specified folder.");
    }

    @Override
    public boolean canMkDir(Path folderPath)
    {
        try
        {
            File f = findRealFile(folderPath);
            return f != null && !f.exists();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean canCreateNewFile(Path filePath)
    {
        try
        {
            File f = findRealFile(filePath);
            return f != null && !f.exists();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    private List<String> listFolders(Path path, String regexp) throws IOException
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
        
        File[] listFiles = f.listFiles((File dir, String name) ->
        {
            return regexp == null || name.matches(regexp);
        });
        if(listFiles != null)
        {
            List<String> result = new LinkedList<>();
            for (File listFile : listFiles)
            {
                if(listFile.isDirectory())
                {
                    result.add(listFile.getName());
                }
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    private List<String> listFiles(Path path, String regexp) throws IOException
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
        File[] listFiles = f.listFiles((File dir, String name) ->
        {
            return regexp == null || name.matches(regexp);
        });
        if(listFiles != null)
        {
            List<String> result = new LinkedList<>();
            for (File listFile : listFiles)
            {
                if(listFile.isFile())
                {
                    result.add(listFile.getName());
                }
            }
            return result;
        }
        return Collections.EMPTY_LIST;
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
}
