
package org.bridje.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSource implements VfsSource
{
    private static final Logger LOG = Logger.getLogger(FileSource.class.getName());

    private final File file;

    public FileSource(File file) throws IOException
    {
        this.file = file.getAbsoluteFile().getCanonicalFile();
        if(!this.file.exists())
        {
            throw new FileNotFoundException();
        }
    }

    private File findFile(Path path)
    {
        if(path == null || path.isRoot()) return file;
        String basePath = file.getPath();
        return new File(basePath + File.separator + path.toString(File.separator));
    }

    @Override
    public boolean isDirectory(Path path)
    {
        File f = findFile(path);
        return f.isDirectory();
    }

    @Override
    public boolean isFile(Path path)
    {
        File f = findFile(path);
        return f.isFile();
    }

    @Override
    public boolean exists(Path path)
    {
        File f = findFile(path);
        return f != null && f.exists();
    }

    @Override
    public boolean canWrite(Path path)
    {
        File f = findFile(path);
        return f.canWrite();
    }

    @Override
    public boolean canRead(Path path)
    {
        File f = findFile(path);
        return f.canRead();
    }

    @Override
    public String[] list(Path path)
    {
        File file = findFile(path);
        if(file.isDirectory())
        {
            return file.list();
        }
        return null;
    }

    @Override
    public InputStream openForRead(Path path)
    {
        try
        {
            File pathFile = findFile(path);
            if(pathFile.isFile()) return new FileInputStream(findFile(path));
        }
        catch (FileNotFoundException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        try
        {
            File pathFile = findFile(path);
            if(pathFile.isFile()) return new FileOutputStream(pathFile);
        }
        catch (FileNotFoundException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public List<Path> search(GlobExpr globExpr, Path path)
    {
        File pathFile = findFile(path);
        if(pathFile.isDirectory())
        {
            List<Path> files = new ArrayList<>();
            search(globExpr, pathFile, path, files);
            return files;
        }
        return null;
    }

    public void search(GlobExpr globExpr, File pathFile, Path path, List<Path> files)
    {
        File[] listFiles = pathFile.listFiles();
        if (listFiles != null)
        {
            for (File f : listFiles)
            {
                if (f.isDirectory())
                {
                    search(globExpr, f, path.join(f.getName()), files);
                }
                else if (f.isFile())
                {
                    Path fullPath = path.join(f.getName());
                    if (globExpr.globMatches(fullPath))
                    {
                        files.add(fullPath);
                    }
                }
            }
        }
    }

    @Override
    public boolean createNewFile(Path path)
    {
        try
        {
            File pathFile = findFile(path);
            if(!pathFile.exists())
            {
                pathFile.getParentFile().mkdirs();
                return pathFile.createNewFile();
            }
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public boolean mkdir(Path path)
    {
        File pathFile = findFile(path);
        if(!pathFile.exists())
        {
            pathFile.getParentFile().mkdirs();
            return pathFile.mkdir();
        }
        return false;
    }

    @Override
    public boolean delete(Path path)
    {
        File pathFile = findFile(path);
        return !pathFile.exists() && pathFile.delete();
    }
}
