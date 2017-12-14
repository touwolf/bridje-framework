
package org.bridje.orm;

/**
 * Represent a page in a pagin object.
 */
public class Page
{
    private final int value;

    private Page(int value)
    {
        this.value = value;
    }

    /**
     * The value of the page.
     *
     * @return The value of the page.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Create a page out of the value.
     *
     * @param value The value of the page.
     *
     * @return The page.
     */
    public static Page of(int value)
    {
        if (value <= 0)
        {
            return null;
        }
        return new Page(value);
    }

    /**
     * Create a page out of the value.
     *
     * @param value The value of the page.
     *
     * @return The page.
     */
    public static Page fromString(String value)
    {
        return of(Integer.valueOf(value));
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Page other = (Page) obj;
        if (this.value != other.value)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.valueOf(value);
    }

}
