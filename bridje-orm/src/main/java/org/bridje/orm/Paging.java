
package org.bridje.orm;

import java.util.Arrays;
import java.util.List;
import org.bridje.sql.Limit;
import org.bridje.sql.SQL;

/**
 * An object holding the paging information for a query.
 */
public class Paging
{
    private final int recordCount;

    private final int pageSize;

    private Page[] pages;

    private int current;

    private int pageCount;

    private Paging(int count, int size)
    {
        recordCount = count;
        pageSize = size;
        pageCount = count / size;
        if (count == 0 || count % size != 0)
        {
            pageCount++;
        }
        setCurrent(1);
    }

    /**
     * The number of records for the query.
     *
     * @return The number of records for the query.
     */
    public int getRecordCount()
    {
        return recordCount;
    }

    /**
     * The size in records for the pages of this paging object.
     *
     * @return The size in records for the pages of this paging object.
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * An array with all the page of this paging object.
     *
     * @return An array with all the page of this paging object.
     */
    public Page[] getPages()
    {
        if (pages == null)
        {
            pages = new Page[pageCount];
            for (int i = 0; i < pageCount; i++)
            {
                pages[i] = Page.of(i + 1);
            }
        }
        return pages;
    }

    /**
     * Gets a section of the paging object.
     *
     * @param sectionSize The size in pages of the section to get.
     *
     * @return An array of pages that represent the section.
     */
    public Page[] getPagesSection(int sectionSize)
    {
        if (sectionSize >= pageCount)
        {
            return getPages();
        }
        if (sectionSize <= 0)
        {
            return new Page[0];
        }
        Page[] section = new Page[sectionSize];
        getPages();
        int halfSectionSize = sectionSize / 2;
        int start = Math.max(0, current - halfSectionSize);
        for (int i = 0; i < sectionSize; i++)
        {
            section[i] = pages[start + i];
        }
        return section;
    }

    /**
     * A list with all the page of this paging object.
     *
     * @return A list with all the page of this paging object.
     */
    public List<Page> getPagesAsList()
    {
        return Arrays.asList(getPages());
    }

    /**
     * Gets a section of the paging object.
     *
     * @param sectionSize The size in pages of the section to get.
     *
     * @return A list of pages that represent the section.
     */
    public List<Page> getPagesSectionAsList(int sectionSize)
    {
        return Arrays.asList(getPagesSection(sectionSize));
    }

    /**
     * The number of pages in this paging object.
     *
     * @return The number of pages in this paging object.
     */
    public int getPageCount()
    {
        return pageCount;
    }

    /**
     * The current page in this object.
     *
     * @return The current page in this object.
     */
    public Page getCurrent()
    {
        if (pages == null)
        {
            return Page.of(current);
        }
        return pages[current - 1];
    }

    /**
     * The current page in this object.
     *
     * @param current The current page in this object.
     */
    public void setCurrent(Page current)
    {
        this.current = current.getValue();
    }

    /**
     * The current page in this object.
     *
     * @param current The current page in this object.
     */
    public void setCurrent(int current)
    {
        this.current = current;
    }

    /**
     * Move this object to the next page.
     */
    public void next()
    {
        if (!isLast())
        {
            setCurrent(current + 1);
        }
    }

    /**
     * Move this object to the previous page.
     */
    public void prev()
    {
        if (!isFirst())
        {
            setCurrent(current - 1);
        }
    }

    /**
     * Determines if the current page is the first page of this object.
     *
     * @return true if the current page is the first page of this object, false
     *         otherwise.
     */
    public boolean isFirst()
    {
        return current == 1;
    }

    /**
     * Determines if the current page is the last page of this object.
     *
     * @return true if the current page is the last page of this object, false
     *         otherwise.
     */
    public boolean isLast()
    {
        return current == pageCount;
    }

    /**
     * Gets the page by the given value.
     *
     * @param value The value of the page to look for.
     *
     * @return The page found or null if it does not exists.
     */
    public Page getPage(int value)
    {
        if (pageCount == 0)
        {
            return null;
        }
        if (value <= 0)
        {
            return pages[0];
        }
        getPages();
        if (value > pageCount)
        {
            return pages[pageCount - 1];
        }
        return pages[value - 1];
    }

    /**
     * Creates a paging object out of the number of records and the size of the
     * pages.
     *
     * @param count The number of records,
     * @param size  The size of the pages.
     *
     * @return The new paging object.
     */
    public static Paging of(int count, int size)
    {
        if (count <= 0 && size <= 0)
        {
            return null;
        }
        return new Paging(count, size);
    }

    /**
     * Creates a paging object out of the number of records, the size of the
     * pages, and the current page.
     *
     * @param count   The number of records,
     * @param size    The size of the pages.
     * @param current The current page.
     *
     * @return The new paging object.
     */
    public static Paging of(int count, int size, int current)
    {
        if (count <= 0 && size <= 0)
        {
            return null;
        }
        Paging paging = new Paging(count, size);
        paging.setCurrent(current);
        return paging;
    }

    @Override
    public String toString()
    {
        return recordCount + ":" + pageSize + ":" + current;
    }

    /**
     * Create a paging object out of it string representation.
     *
     * @param value The string representation of the paging objectt.
     *
     * @return The new paging object.
     */
    public static Paging fromString(String value)
    {
        if (value == null || value.trim().isEmpty())
        {
            return null;
        }
        String[] arr = value.split(":");
        Integer count = Integer.valueOf(arr[0]);
        Integer size = Integer.valueOf(arr[1]);
        Integer current = Integer.valueOf(arr[2]);
        return Paging.of(count, size, current);
    }

    /**
     * Creates a new SQL Limit object out of this paging object.
     *
     * @return The new SQL Limit object.
     */
    public Limit toLimit()
    {
        return SQL.limit((current - 1) * pageSize, pageSize);
    }

}
