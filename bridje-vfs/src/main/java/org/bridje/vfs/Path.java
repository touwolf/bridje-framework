
package org.bridje.vfs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An utility class for virtual path management.
 * <p>
 * The path elements are separated by the {@literal "/"} character.
 */
public class Path implements Iterable<Path>
{
    private final String[] pathElements;

    public Path()
    {
        this.pathElements = null;
    }

    /**
     * Creates a Path object from String
     * <p>
     * @param path The String object representing the path
     */
    public Path(String path)
    {
        this(createElements(path));
    }

    /**
     * Internal constructor that creates a Path from it´s elements.
     * <p>
     * @param pathElements The elements for the path.
     */
    private Path(String[] pathElements)
    {
        this.pathElements = pathElements;
    }

    /**
     *
     * @return
     */
    public String[] getPathElements()
    {
        return pathElements;
    }

    /**
     * Gets the first element of the path.
     * <p>
     * For example, if the path represented by this object equals to
     * {@literal "usr/local"}, this method will return {@literal "usr"}.
     * <p>
     * @return An String object that represents the first element of the path.
     */
    public String getFirstElement()
    {
        if(pathElements == null) return null;
        return pathElements[0];
    }

    /**
     * Gets the first element of the path.
     * <p>
     * For example, if the path represented by this object equals to
     * {@literal "usr/local/somefile"}, this method will return
     * {@literal "somefile"}.
     * <p>
     * @return An String object that represents the first element of the path.
     */
    public String getName()
    {
        if(pathElements == null) return "/";
        return pathElements[pathElements.length - 1];
    }

    /**
     * Creates a new Path object that represents the path to the parent object
     * of the current path.
     * <p>
     * For example, if the path represented by this object equals to
     * {@literal "usr/local/somefile"}, this method will return a path object
     * representing {@literal "usr/local"}.
     * <p>
     * @return The Path object parent of this object, or {@literal null} if this
     *         is the last path.
     */
    public Path getParent()
    {
        if (isLast()) return new Path();
        String[] copyOfRange = Arrays.copyOfRange(pathElements, 0, pathElements.length - 1);
        return new Path(copyOfRange);
    }

    /**
     * Creates a new Path object that does not contains the first element of the
     * current path.
     * <p>
     * For example, if the path represented by this object equals to
     * {@literal "usr/local/somefile"}, this method will return a path object
     * representing {@literal "/local/somefile"}.
     * <p>
     * @return The Path object without the first element of the current path, or
     *         {@literal null} if this is the las path.
     */
    public Path getNext()
    {
        if (isLast()) return null;
        String[] copyOfRange = Arrays.copyOfRange(pathElements, 1, pathElements.length);
        return new Path(copyOfRange);
    }

    /**
     * Determines if this path has any element left.
     * <p>
     * For example, if the path represented by this object is a multiple element
     * path like {@literal "usr/local"} the this method will return
     * {@literal true}.
     * <p>
     * @return {@literal true} if this path is a multi element path,
     *         {@literal false} otherwise.
     */
    public boolean hasNext()
    {
        return !isLast();
    }

    /**
     * If this path is the root path "/".
     *
     * @return true this path is the root path.
     */
    public boolean isRoot()
    {
        return pathElements == null;
    }

    /**
     * Determines if the first element of the path is the dot (.) character
     * witch represents the current folder.
     * <p>
     * @return {@literal true} if the first element of the path is the dot (.)
     *         character, {@literal false} otherwise.
     */
    public boolean isSelf()
    {
        if(isRoot()) return false;
        return ".".equalsIgnoreCase(pathElements[0]);
    }

    /**
     * Determines if the first element of the path is the (..) identifier witch
     * represents the parent folder.
     * <p>
     * @return {@literal true} if the first element of the path is the (..)
     *         identifier, {@literal false} otherwise.
     */
    public boolean isParent()
    {
        if(isRoot()) return false;
        return "..".equalsIgnoreCase(pathElements[0]);
    }

    /**
     * Determines if this path is the last element path.
     * <p>
     * If the path represented by this object is a single element path like
     * {@literal "usr"} then this method will return {@literal true}.
     * <p>
     * @return {@literal true} if this path is a single element,
     *         {@literal false} otherwise.
     */
    public boolean isLast()
    {
        if(isRoot()) return true;
        return (pathElements.length <= 1);
    }

    /**
     * This method creates a new path object that does not contains the (.) and
     * (..) identifiers.
     * <p>
     * For example, if the path represented by this element equals to
     * {@literal "usr/./local/../etc"}, this method will return
     * {@literal "usr/etc"}.
     * <p>
     * @return A new Path object representing the canonical path of this object.
     */
    public Path getCanonicalPath()
    {
        if(isRoot()) return this;
        List<String> str = new LinkedList<>();
        for (String pe : pathElements)
        {
            if (pe.equalsIgnoreCase(".."))
            {
                if (str.isEmpty())
                {
                    return null;
                }
                else
                {
                    str.remove(str.size() - 1);
                }
            }
            else if (!pe.equalsIgnoreCase("."))
            {
                str.add(pe);
            }
        }
        if (str.isEmpty()) return null;
        String[] els = new String[str.size()];
        return new Path(str.toArray(els));
    }

    /**
     * Gets a string representation of the current path.
     * <p>
     * @return The String object representing the current path.
     */
    @Override
    public String toString()
    {
        if(isRoot()) return "/";
        return toString("/");
    }

    /**
     * Gets a string representation of the current path, separated by the
     * specified path separator.
     * <p>
     * @param pathSep The path separator to be used.
     *
     * @return The String object representing the current path.
     */
    public String toString(String pathSep)
    {
        if(isRoot()) return pathSep;
        return String.join(pathSep, pathElements);
    }

    /**
     * Obtains the concatenation with another path.
     * <p>
     * @param path the other path to concatenate.
     *
     * @return a new path with the concatenation.
     */
    public Path join(Path path)
    {
        if(isRoot()) return new Path(path.pathElements);
        String[] newElements = new String[pathElements.length + path.pathElements.length];
        System.arraycopy(pathElements, 0, newElements, 0, pathElements.length);
        System.arraycopy(path.pathElements, 0, newElements, pathElements.length, path.pathElements.length);
        return new Path(newElements);
    }

    /**
     * Obtains the concatenation with a string.
     * <p>
     * @param path the string to concatenate.
     *
     * @return a new path with the concatenation.
     */
    public Path join(String path)
    {
        if(isRoot()) return new Path(path);
        return join(new Path(path));
    }

    /**
     * @see Iterable
     */
    @Override
    public Iterator<Path> iterator()
    {
        return new Iterator<Path>()
        {
            private int currentIndex;

            @Override
            public boolean hasNext()
            {
                return !isRoot() && (currentIndex < pathElements.length);
            }

            @Override
            public Path next()
            {
                if(isRoot()) return null;
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

        String normPath = normalize(path);
        String[] arr = normPath.split("/");

        return arr;
    }

    public String getExtension()
    {
        if(isRoot()) return null;
        String[] split = getName().split("[\\.]");
        if(split.length > 1) return split[split.length - 1];
        return null;
    }

    private static String normalize(String path)
    {
        String normPath = path;
        String toReplace = "\\";
        while (normPath.contains(toReplace))
        {
            normPath = normPath.replace(toReplace, "/");
        }

        if (path.startsWith("/"))
        {
            normPath = normPath.substring(1);
        }
        if (normPath.endsWith("/"))
        {
            normPath = normPath.substring(0, normPath.length() - 1);
        }

        return normPath.trim();
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
