
package org.bridje.orm;

/**
 * 
 */
public class Page
{
    private final int value;

    private Page(int value)
    {
        this.value = value;
    }

    /**
     * 
     * @return 
     */
    public int getValue()
    {
        return value;
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static Page of(int value)
    {
        if(value <= 0) return null;
        return new Page(value);
    }

    /**
     * 
     * @param value
     * @return 
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
