
package org.bridje.vfs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A glob syntax expression for file search.
 */
public class GlobExpr
{
    private final String glob;

    private final Pattern pattern;

    /**
     * Default constructor for the GlobExpr object.
     *
     * @param glob THe glob syntax expression for this object.
     */
    public GlobExpr(String glob)
    {
        this.glob = glob;
        String regex = globToRegex();
        pattern = Pattern.compile(regex);
    }

    /**
     * The glob syntax expression.
     * 
     * @return The glob syntax expression.
     */
    public String getValue()
    {
        return glob;
    }

    @Override
    public String toString()
    {
        return glob;
    }

    /**
     * Tests the path against a string path with glob syntax.
     * <p>
     * Glob syntax follows the following simple rules:
     * <ul>
     * <li>Asterisk {@literal "*"}: matches any number of characters (including
     * none).</li>
     * <li>Two asterisks {@literal "**"}: is like {@literal "*"} but includes
     * directory separator. Is generally used for matching complete paths.</li>
     * <li>Question mark {@literal "?"}: matches exactly one character.</li>
     * <li>Braces specify a collection of sub patterns. For example:<br> -
     * {@literal "{java,maven,bridje}"} matches
     * {@literal "java"}, {@literal "maven"}, or {@literal "bridje"}.<br> -
     * {@literal "{gradle*,ant*}"} matches all strings beginning with
     * {@literal "gradle"} or {@literal "ant"}.</li>
     * <li>Square brackets defines a set of single characters or, when used with
     * the hyphen character {@literal "-"}, a range of characters. For
     * example:<br> - {@literal "[aeiou]"} matches any lowercase vowel.<br> -
     * {@literal "[0-9]"} matches any digit.<br> - {@literal "[A-Z]"} matches
     * any uppercase letter.<br> - {@literal "[a-z,A-Z]"} matches any uppercase
     * or lowercase letter.<br>
     * Within the square brackets, {@literal "*"}, {@literal "?"}, and
     * {@literal "\"} match themselves.<li>
     * <li>All other characters match themselves.</li>
     * </ul>
     * <p>
     * To match {@literal "*"}, {@literal "?"}, or the other special characters,
     * you can escape them by using the backslash character, {@literal "\"}. For
     * example: {@literal "\\"} matches a single backslash, and {@literal "\?"}
     * matches the question mark.
     *
     * @param path path to test
     *
     * @return {@literal true} if the glob match this path, {@literal false}
     *         otherwise.
     */
    public boolean globMatches(Path path)
    {
        if (path.isRoot()) return false;
        String normPath = path.toString();
        Matcher matcher = pattern.matcher(normPath);

        return matcher.matches();
    }

    /**
     * Obtains the path remaining of matches a path with glob syntax.
     *
     * @param path path to search
     *
     * @return if the glob match this path beginning will return the remaining
     *         path, this full path otherwise.
     *
     * @see GlobExpr#globMatches(Path)
     */
    public Path globRemaining(Path path)
    {
        if (path.isRoot()) return null;
        String normPath = path.toString();
        Matcher matcher = pattern.matcher(normPath);
        if (matcher.find())
        {
            int end = matcher.end();
            if (end > 0 && end < normPath.length())
            {
                return new Path(normPath.substring(end));
            }
        }
        return path;
    }

    private String globToRegex()
    {
        String regex = normalize(glob);
        // replace []
        regex = replaceGlobBoundaries(regex, "[", "]", true);
        // replace .
        regex = regex.replaceAll("\\.", "\\\\.");
        // replace **
        regex = regex.replaceAll("\\*\\*", "(\\\\s|\\\\w|-|\\\\.|/)+");
        // replace *
        regex = regex.replaceAll("\\*", "(\\\\s|\\\\w|-|\\\\.)*");
        // replace ?
        regex = regex.replaceAll("\\?", "(\\\\s|\\\\w|-|\\\\.)");
        // replace {}
        regex = replaceGlobBoundaries(regex, "{", "}", false);
        // replace /
        regex = regex.replaceAll("/", "\\\\/");
        // replace literals
        regex = regex.replaceAll("ASTERISK", "\\\\*");
        regex = regex.replaceAll("QUESTION", "\\\\?");

        return regex;
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
}
