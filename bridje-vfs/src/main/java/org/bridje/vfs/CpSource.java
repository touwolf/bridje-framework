
package org.bridje.vfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CpSource implements VfsSource
{
    private final String resource;

    private Map<String, CpSource> childs;

    /**
     * The only constructor for this object, the resource path needs to be specified.
     * 
     * @param resource The path to the resource folder to be mount.
     * @throws IOException If any IO exception occurs scanning the resource.
     * @throws URISyntaxException If the specified resource has an invalid URI.
     */
    public CpSource(String resource) throws IOException, URISyntaxException
    {
        this.resource = new Path(resource).toString();
        List<String> resourceListing = getResourceListing(getClass(), this.resource + "/");
        if(resourceListing != null && !resourceListing.isEmpty())
        {
            this.childs = new HashMap<>();
            for (String childName : resourceListing)
            {
                this.childs.put(childName, new CpSource(resource + "/" + childName));
            }
        }
    }

    @Override
    public boolean isDirectory(Path path)
    {
        if(path == null)
        {
            return childs != null;
        }
        else
        {
            CpSource cp = childs.get(path.getFirstElement());
            if(cp == null) return false;
            return cp.isDirectory(path.getNext());
        }
    }

    @Override
    public boolean isFile(Path path)
    {
        if(path == null)
        {
            return childs == null;
        }
        else
        {
            CpSource cp = childs.get(path.getFirstElement());
            if(cp == null) return false;
            return cp.isFile(path.getNext());
        }
    }

    @Override
    public boolean exists(Path path)
    {
        if(path == null)
        {
            return true;
        }
        else
        {
            if(childs == null) return false;
            CpSource cp = childs.get(path.getFirstElement());
            if(cp == null) return false;
            return cp.exists(path.getNext());
        }
    }

    @Override
    public boolean canWrite(Path path)
    {
        return false;
    }

    @Override
    public boolean canRead(Path path)
    {
        return isFile(path);
    }

    @Override
    public String[] list(Path path)
    {
        if(childs == null) return null;
        if(path == null)
        {
            String[] arr = new String[childs.size()];
            return childs.keySet().toArray(arr);
        }
        else
        {
            CpSource cp = childs.get(path.getFirstElement());
            if(cp == null) return null;
            return cp.list(path.getNext());
        }
    }

    @Override
    public InputStream openForRead(Path path)
    {
        if(path == null)
        {
            if(childs == null)
            {
                return getClass().getClassLoader().getResourceAsStream(resource);
            }
            return null;
        }
        else
        {
            CpSource cp = childs.get(path.getFirstElement());
            if(cp == null) return null;
            return cp.openForRead(path.getNext());
        }
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        return null;
    }

    private List<String> getResourceListing(Class clazz, String path) throws URISyntaxException, IOException
    {
        String currentPath = path;
        if(currentPath.startsWith("/"))
        {
            currentPath = currentPath.substring(1);
        }
        Enumeration<URL> resources = clazz.getClassLoader().getResources(currentPath);
        Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
        while (resources.hasMoreElements())
        {
            URL dirURL = resources.nextElement();
            if (dirURL != null && dirURL.getProtocol().equals("file"))
            {
                /* A file path: easy enough */
                String[] list = new File(dirURL.toURI()).list();
                if(list == null)
                {
                    return null;
                }
                result.addAll(Arrays.asList(list));
            }

            if (dirURL == null)
            {
                /*
                 * In case of a jar file, we can't actually find a directory.
                 * Have to assume the same jar as clazz.
                 */
                String me = clazz.getName().replace(".", "/").concat(".class");
                dirURL = clazz.getClassLoader().getResource(me);
            }

            if (dirURL.getProtocol().equals("jar"))
            {
                /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                Set<String> currentProjectResource = new HashSet<>(); //avoid duplicates in case it is a subdirectory
                while (entries.hasMoreElements())
                {
                    JarEntry jarEntry = entries.nextElement();
                    String name = jarEntry.getName().trim();
                    if (name.startsWith(currentPath))
                    {
                        //filter according to the path
                        String entry = name.trim().substring(currentPath.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0)
                        {
                            // if it is a subdirectory, we just return the directory name
                            entry = entry.substring(0, checkSubdir);
                        }
                        if(!entry.isEmpty())
                        {
                            currentProjectResource.add(entry);
                        }
                    }
                }
                result.addAll(currentProjectResource);
            }
        }
        if (result.isEmpty())
        {
            return null;
        }
        List<String> res = new LinkedList<>();
        res.addAll(result);
        return res;
    }

    @Override
    public List<Path> search(GlobExpr globExpr, Path path)
    {
        CpSource resource = findResource(path);
        if(resource.isDirectory(null))
        {
            List<Path> files = new ArrayList<>();
            resource.search(globExpr, path, files);
            return files;
        }
        return null;
    }
    
    public CpSource findResource(Path path)
    {
        if(path == null || path.isRoot()) return this;
        CpSource child = childs.get(path.getFirstElement());
        if(child == null) return null;
        return child.findResource(path.getNext());
    }

    public void search(GlobExpr globExpr, Path path, List<Path> files)
    {
        for (Entry<String, CpSource> entry : childs.entrySet())
        {
            if(entry.getValue().isDirectory(null))
            {
                entry.getValue().search(globExpr, path.join(entry.getKey()), files);
            }
            else if(entry.getValue().isFile(null))
            {
                Path fullPath = path.join(entry.getKey());
                if(globExpr.globMatches(fullPath))
                {
                    files.add(fullPath);
                }
            }
        }
    }

    @Override
    public boolean createNewFile(Path path)
    {
        return false;
    }

    @Override
    public boolean mkdir(Path path)
    {
        return false;
    }

    @Override
    public boolean delete(Path path)
    {
        return false;
    }
}
