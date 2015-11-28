
package org.bridje.core.vfs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Path implements Iterable<Path>
{
    private final String[] pathElements;

    public Path(String path)
    {
        this(createElements(path));
    }

    private Path(String[] pathElements)
    {
        if(pathElements == null || pathElements.length == 0)
        {
            throw new IllegalArgumentException("The specified path is not valid .");
        }
        this.pathElements = pathElements;
    }

    public String getFirstElement()
    {
        return pathElements[0];
    }

    public String getName()
    {
        return pathElements[pathElements.length - 1];
    }

    public Path getParent()
    {
        if(isLast())
        {
            return null;
        }
        String[] copyOfRange = Arrays.copyOfRange(pathElements, 0, pathElements.length - 1);
        return new Path(copyOfRange);
    }

    public Path getNext()
    {
        if(isLast())
        {
            return null;
        }

        String[] copyOfRange = Arrays.copyOfRange(pathElements, 1, pathElements.length);
        return new Path(copyOfRange);
    }
    
    public boolean hasNext()
    {
        return !isLast();
    }

    public boolean isSelf()
    {
        return ".".equalsIgnoreCase(pathElements[0]);
    }

    public boolean isParent()
    {
        return "..".equalsIgnoreCase(pathElements[0]);
    }

    public boolean isLast()
    {
        return (pathElements.length == 1);
    }

    public Path getCanonicalPath()
    {
        List<String> str = new LinkedList<>();
        for (String pe : pathElements)
        {
            if(pe.equalsIgnoreCase(".."))
            {
                if(str.isEmpty())
                {
                    return null;
                }
                else
                {
                    str.remove(str.size()-1);
                }
            }
            else if(!pe.equalsIgnoreCase("."))
            {
                str.add(pe);
            }
        }
        if(str.isEmpty())
        {
            return null;
        }
        String[] els = new String[str.size()];
        return new Path(str.toArray(els));
    }
    
    @Override
    public String toString()
    {
        return toString("/");
    }

    public String toString(String pathSep)
    {
        return String.join(pathSep, pathElements);
    }

    public Path join(Path path)
    {
        String[] newElements = new String[pathElements.length + path.pathElements.length];
        System.arraycopy(pathElements, 0, newElements, 0, pathElements.length);
        System.arraycopy(path.pathElements, 0, newElements, pathElements.length, path.pathElements.length);
        return new Path(newElements);
    }

    public Path join(String path)
    {
        return join(new Path(path));
    }

    @Override
    public Iterator<Path> iterator()
    {
        return new Iterator<Path>()
        {
            private int currentIndex = 0;

            @Override
            public boolean hasNext()
            {
                return (currentIndex < pathElements.length);
            }

            @Override
            public Path next()
            {
                String[] copyOfRange = Arrays.copyOfRange(pathElements, 0, currentIndex + 1);
                currentIndex++;
                return new Path(copyOfRange);
            }
        };
    }
    
    private static String[] createElements(String path)
    {
        if (path == null || path.trim().isEmpty())
        {
            throw new IllegalArgumentException("The specified path is not valid.");
        }

        String newPath = path;
        String toReplace = "\\";
        while (newPath.contains(toReplace))
        {
            newPath = newPath.replace(toReplace, "/");
        }

        if (path.startsWith("/"))
        {
            newPath = newPath.substring(1);
        }
        if (newPath.endsWith("/"))
        {
           newPath = newPath.substring(0, newPath.length() - 1);
        }

        if(newPath.trim().isEmpty())
        {
            throw new IllegalArgumentException("The specified path is not valid .");
        }

        String[] arr = newPath.trim().split("/");
        return arr;
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Path other = (Path) obj;
        return toString().equals(other.toString());
    }
}
