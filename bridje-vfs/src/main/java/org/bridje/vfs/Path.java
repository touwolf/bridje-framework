
package org.bridje.vfs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An utility class for virtual path management.
 * <p>
 * The path elements are separated by the {@literal "/"} character.
 */
public class Path implements Iterable<Path>
{
    private final String[] pathElements;

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
     * Gets the first element of the path.
     * <p>
     * For example, if the path represented by this object equals to {@literal "usr/local"},
     * this method will return {@literal "usr"}.
     * <p>
     * @return An String object that represents the first element of the path.
     */
    public String getFirstElement()
    {
        return pathElements[0];
    }

    /**
     * Gets the first element of the path.
     * <p>
     * For example, if the path represented by this object equals to {@literal "usr/local/somefile"},
     * this method will return {@literal "somefile"}.
     * <p>
     * @return An String object that represents the first element of the path.
     */
    public String getName()
    {
        return pathElements[pathElements.length - 1];
    }

    /**
     * Creates a new Path object that represents the path to the parent object
     * of the current path.
     * <p>
     * For example, if the path represented by this object equals to {@literal "usr/local/somefile"},
     * this method will return a path object representing {@literal "usr/local"}.
     * <p>
     * @return The Path object parent of this object, or {@literal null} if this
     *         is the last path.
     */
    public Path getParent()
    {
        if (isLast())
        {
            return null;
        }
        String[] copyOfRange = Arrays.copyOfRange(pathElements, 0, pathElements.length - 1);
        return new Path(copyOfRange);
    }

    /**
     * Creates a new Path object that does not contains the first element of
     * the current path.
     * <p>
     * For example, if the path represented by this object equals to {@literal "usr/local/somefile"},
     * this method will return a path object representing  {@literal "/local/somefile"}.
     * <p>
     * @return The Path object without the first element of the current path, or
     *         {@literal null} if this is the las path.
     */
    public Path getNext()
    {
        if (isLast())
        {
            return null;
        }

        String[] copyOfRange = Arrays.copyOfRange(pathElements, 1, pathElements.length);
        return new Path(copyOfRange);
    }

    /**
     * Determines if this path has any element left.
     * <p>
     * For example, if the path represented by this object is a multiple element
     * path like {@literal "usr/local"} the this method will return {@literal true}.
     * <p>
     * @return {@literal true} if this path is a multi element path, {@literal false}
     *         otherwise.
     */
    public boolean hasNext()
    {
        return !isLast();
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
        if(pathElements.length < 0)
        {
            return false;
        }
        return ".".equalsIgnoreCase(pathElements[0]);
    }

    /**
     * Determines if the first element of the path is the (..) identifier witch
     * represents the parent folder.
     * <p>
     * @return {@literal true} if the first element of the path is the (..) identifier,
     *         {@literal false} otherwise.
     */
    public boolean isParent()
    {
        if(pathElements.length < 0)
        {
            return false;
        }
        return "..".equalsIgnoreCase(pathElements[0]);
    }

    /**
     * Determines if this path is the last element path.
     * <p>
     * If the path represented by this object is a single element path like
     * {@literal "usr"} then this method will return {@literal true}.
     * <p>
     * @return {@literal true} if this path is a single element, {@literal false}
     *         otherwise.
     */
    public boolean isLast()
    {
        return (pathElements.length <= 1);
    }

    /**
     * This method creates a new path object that does not contains the (.) and
     * (..) identifiers.
     * <p>
     * For example, if the path represented by this element equals to {@literal "usr/./local/../etc"},
     * this method will return {@literal "usr/etc"}.
     * <p>
     * @return A new Path object representing the canonical path of this object.
     */
    public Path getCanonicalPath()
    {
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
        if (str.isEmpty())
        {
            return null;
        }
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
        return toString("/");
    }

    /**
     * Gets a string representation of the current path, separated by the
     * specified path separator.
     * <p>
     * @param pathSep The path separator to be used.
     * @return The String object representing the current path.
     */
    public String toString(String pathSep)
    {
        return String.join(pathSep, pathElements);
    }

    /**
     * Obtains the concatenation with another path.
     * <p>
     * @param path the other path to concatenate.
     * @return a new path with the concatenation.
     */
    public Path join(Path path)
    {
        String[] newElements = new String[pathElements.length + path.pathElements.length];
        System.arraycopy(pathElements, 0, newElements, 0, pathElements.length);
        System.arraycopy(path.pathElements, 0, newElements, pathElements.length, path.pathElements.length);
        return new Path(newElements);
    }

    /**
     * Obtains the concatenation with a string.
     * <p>
     * @param path the string to concatenate.
     * @return a new path with the concatenation.
     */
    public Path join(String path)
    {
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

    /**
     * Tests the path against a string path with glob syntax.
     * <p>
     * Glob syntax follows the following simple rules:
     * <ul>
     * <li>Asterisk {@literal "*"}: matches any number of characters (including none).</li>
     * <li>Two asterisks {@literal "**"}: is like {@literal "*"} but includes directory separator.
     * Is generally used for matching complete paths.</li>
     * <li>Question mark {@literal "?"}: matches exactly one character.</li>
     * <li>Braces specify a collection of sub patterns. For example:<br>
     *  - {@literal "{java,maven,bridje}"} matches {@literal "java"}, {@literal "maven"}, or {@literal "bridje"}.<br>
     *  - {@literal "{gradle*,ant*}"} matches all strings beginning with {@literal "gradle"} or {@literal "ant"}.</li>
     * <li>Square brackets defines a set of single characters or, when used with the hyphen character {@literal "-"},
     * a range of characters. For example:<br>
     *  - {@literal "[aeiou]"} matches any lowercase vowel.<br>
     *  - {@literal "[0-9]"} matches any digit.<br>
     *  - {@literal "[A-Z]"} matches any uppercase letter.<br>
     *  - {@literal "[a-z,A-Z]"} matches any uppercase or lowercase letter.<br>
     * Within the square brackets, {@literal "*"}, {@literal "?"}, and {@literal "\"} match themselves.<li>
     * <li>All other characters match themselves.</li>
     * </ul>
     * <p>
     * To match {@literal "*"}, {@literal "?"}, or the other special characters,
     * you can escape them by using the backslash character, {@literal "\"}.
     * For example: {@literal "\\"} matches a single backslash, and {@literal "\?"} matches the question mark.
     *
     * @param glob the requested glob to test.
     * @return {@literal true} if the glob match this path, {@literal false} otherwise.
     */
    public boolean globMatches(String glob)
    {
        if (glob == null || glob.trim().isEmpty())
        {
            return false;
        }

        String regex = globToRegex(glob);
        String normPath = toString("/");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(normPath);

        return matcher.matches();
    }

    private String globToRegex(String glob)
    {
        String regex = normalize(glob);
        // replace []
        regex = replaceGlobBoundaries(regex, "[", "]", true);
        // replace .
        regex = regex.replaceAll("\\.", "\\\\.");
        // replace **
        regex = regex.replaceAll("\\*\\*", "(\\\\w|-|\\\\.|/)+");
        // replace *
        regex = regex.replaceAll("\\*", "(\\\\w|-|\\\\.)*");
        // replace ?
        regex = regex.replaceAll("\\?", "(\\\\w|-|\\\\.)");
        // replace {}
        regex = replaceGlobBoundaries(regex, "{", "}", false);
        // replace /
        regex = regex.replaceAll("/", "\\\\/");
        // replace literals
        regex = regex.replaceAll("ASTERISK", "\\\\*");
        regex = regex.replaceAll("QUESTION", "\\\\?");

        return regex;
    }

    private String replaceGlobBoundaries(String glob, String open, String close, boolean escape)
    {
        String regex = glob;
        String newOpen = "(";
        String newClose = ")";

        int index = regex.indexOf(open);
        while (index >= 0)
        {
            int endIndex = regex.indexOf(close, index);
            if (endIndex < 0)
            {
                // malformed glob
                return glob;
            }
            // replace , by |
            String[] globs = regex.substring(index + 1, endIndex).split(",");
            String postRegex = regex.substring(endIndex + 1);
            regex = regex.substring(0, index) + newOpen;
            for (int i = 0; i < globs.length; i++)
            {
                if (i > 0)
                {
                    regex += "|";
                }
                String globChild = globs[i].trim();
                // escape special characters so they won´t get processed further
                if (escape)
                {
                    globChild = globChild.replaceAll("\\*", "ASTERISK");
                    globChild = globChild.replaceAll("\\?", "QUESTION");
                }
                // hyphen means range, so apply [] to glob part if it's not a regex already
                if (globChild.contains("-") && !globChild.contains("|-|"))
                {
                    globChild = "[" + globChild + "]";
                }

                regex += globChild;
            }

            int nextIndex = regex.length();
            regex += newClose + postRegex;
            index = regex.indexOf(open, nextIndex);
        }

        return regex;
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
