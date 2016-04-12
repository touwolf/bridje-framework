
package org.bridje.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class FileVfsSource implements VfsSource
{
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
        return listFolders(path, null);
    }

    @Override
    public List<String> listFiles(Path path) throws IOException
    {
        return listFiles(path, null);
    }

    public List<String> listFolders(Path path, String regexp) throws IOException
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
        File[] listFiles = f.listFiles((File dir, String name) ->
        {
            return regexp == null || name.matches(regexp);
        });
        for (File listFile : listFiles)
        {
            if(listFile.isDirectory())
            {
                result.add(listFile.getName());
            }
        }
        return result;
    }

    public List<String> listFiles(Path path, String regexp) throws IOException
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
        File[] listFiles = f.listFiles((File dir, String name) ->
        {
            return regexp == null || name.matches(regexp);
        });
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
    public InputStream open(Object data) throws IOException
    {
        File f = (File)data;
        return new FileInputStream(f);
    }
}
