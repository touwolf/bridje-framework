
package org.bridje.core.impl.vfs.src;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bridje.core.vfs.Path;
import org.bridje.core.vfs.VfsSource;

/**
 *
 */
public class ClassPathVfsSource implements VfsSource
{
    private final String resource;

    private Map<String, ClassPathVfsSource> childs;

    public ClassPathVfsSource(String resource) throws IOException, URISyntaxException
    {
        this.resource = new Path(resource).toString();
        List<String> resourceListing = getResourceListing(getClass(), resource);
        if(resourceListing != null && !resourceListing.isEmpty())
        {
            this.childs = new HashMap<>();
            for (String childName : resourceListing)
            {
                this.childs.put(childName, new ClassPathVfsSource(resource + "/" + childName));
            }
        }
    }

    @Override
    public URL[] getFiles(Path path) throws IOException
    {
        List<URL> urls = new LinkedList<>();
        Enumeration<URL> resources;
        if(path == null)
        {
            resources = getClass().getClassLoader().getResources(resource);
        }
        else
        {
            resources = getClass().getClassLoader().getResources(resource + "/" + path);
        }
        if(resources != null)
        {
            while (resources.hasMoreElements())
            {
                URL nextElement = resources.nextElement();
                urls.add(nextElement);
            }
        }
        URL[] result = new URL[urls.size()];
        return urls.toArray(result);
    }

    @Override
    public List<String> listFolders(Path path) throws IOException
    {
        if(childs == null)
        {
            return null;
        }
        if(path == null)
        {
            List<String> result = new LinkedList<>();
            for (Map.Entry<String, ClassPathVfsSource> entry : childs.entrySet())
            {
                String key = entry.getKey();
                ClassPathVfsSource value = entry.getValue();
                if (value.isFolder())
                {
                    result.add(key);
                }
            }
            return result;
        }
        else
        {
            ClassPathVfsSource src = childs.get(path.getFirstElement());
            if(src != null)
            {
                return src.listFolders(path.getNext());
            }
        }
        return null;
    }

    @Override
    public List<String> listFiles(Path path) throws IOException
    {
        if(childs == null)
        {
            return null;
        }
        if(path == null)
        {
            List<String> result = new LinkedList<>();
            childs.entrySet().stream().forEach((entry) ->
            {
                String key = entry.getKey();
                ClassPathVfsSource value = entry.getValue();
                if (!value.isFolder())
                {
                    result.add(key);
                }
            });
            return result;
        }
        else
        {
            ClassPathVfsSource src = childs.get(path.getFirstElement());
            if(src != null)
            {
                return src.listFiles(path.getNext());
            }
        }
        return null;
    }

    @Override
    public boolean fileExists(Path path) throws IOException
    {
        if(path == null)
        {
            return !isFolder();
        }
        if(childs == null)
        {
            return false;
        }
        ClassPathVfsSource src = childs.get(path.getFirstElement());
        if(src != null)
        {
            return src.fileExists(path.getNext());
        }
        return false;
    }

    @Override
    public boolean folderExists(Path path) throws IOException
    {
        if(path == null)
        {
            return isFolder();
        }
        if(childs == null)
        {
            return false;
        }
        ClassPathVfsSource src = childs.get(path.getFirstElement());
        if(src != null)
        {
            return src.folderExists(path.getNext());
        }
        return false;
    }

    private List<String> getResourceListing(Class clazz, String path) throws URISyntaxException, IOException
    {
        if(path.startsWith("/"))
        {
            path = path.substring(1);
        }
        Enumeration<URL> resources = clazz.getClassLoader().getResources(path);
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
                    String name = entries.nextElement().getName().trim();
                    if (name.startsWith(path))
                    {
                        //filter according to the path
                        String entry = name.trim().substring(path.length());
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

    private boolean isFolder()
    {
        return childs != null;
    }

    @Override
    public InputStream open(Object data) throws IOException
    {
        URL url = (URL)data;
        return url.openStream();
    }
}
