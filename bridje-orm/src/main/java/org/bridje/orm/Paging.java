
package org.bridje.orm;

import java.util.Arrays;
import java.util.List;
import org.bridje.sql.Limit;
import org.bridje.sql.SQL;

/**
 * 
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
        if(count == 0 || count % size != 0) pageCount++;
        setCurrent(1);
    }

    /**
     * 
     * @return 
     */
    public int getRecordCount()
    {
        return recordCount;
    }

    /**
     * 
     * @return 
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * 
     * @return 
     */
    public Page[] getPages()
    {
        if(pages == null)
        {
            pages = new Page[pageCount];
            for (int i = 0; i < pageCount; i++)
            {
                pages[i] = Page.of(i+1);
            }
        }
        return pages;
    }
    
    /**
     * 
     * @param sectionSize
     * @return 
     */
    public Page[] getPagesSection(int sectionSize)
    {
        if(sectionSize >= pageCount) return getPages();
        if(sectionSize <= 0) return new Page[0];
        Page[] section = new Page[sectionSize];
        getPages();
        int start = Math.max(0, current - sectionSize);
        for (int i = 0; i < sectionSize; i++)
        {
            section[i] = pages[start + i];
        }
        return section;
    }

    /**
     * 
     * @return 
     */
    public List<Page> getPagesAsList()
    {
        return Arrays.asList(getPages());
    }
    
    /**
     * 
     * @param sectionSize
     * @return 
     */
    public List<Page> getPagesSectionAsList(int sectionSize)
    {
        return Arrays.asList(getPagesSection(sectionSize));
    }
    
    /**
     * 
     * @return 
     */
    public int getPageCount()
    {
        return pageCount;
    }

    /**
     * 
     * @return 
     */
    public Page getCurrent()
    {
        if(pages == null) return Page.of(current);
        return pages[current - 1];
    }

    /**
     * 
     * @param current 
     */
    public void setCurrent(Page current)
    {
        this.current = current.getValue();
    }
    
    /**
     * 
     * @param current 
     */
    public void setCurrent(int current)
    {
        this.current = current;
    }
    
    /**
     * 
     */
    public void next()
    {
        if(!isLast())
        {
            setCurrent(current + 1);
        }
    }
    
    /**
     * 
     */
    public void prev()
    {
        if(!isFirst())
        {
            setCurrent(current - 1);
        }
    }
    
    /**
     * 
     * @return 
     */
    public boolean isFirst()
    {
        return current == 1;
    }

    /**
     * 
     * @return 
     */
    public boolean isLast()
    {
        return current == pageCount;
    }

    /**
     * 
     * @param value
     * @return 
     */
    public Page getPage(int value)
    {
        if(pageCount == 0) return null;
        if(value <= 0) return pages[0];
        getPages();
        if(value > pageCount) return pages[pageCount-1];
        return pages[value-1];
    }

    /**
     * 
     * @param count
     * @param size
     * @return 
     */
    public static Paging of(int count, int size)
    {
        if(count <= 0 && size <= 0) return null;
        return new Paging(count, size);
    }

    /**
     * 
     * @param count
     * @param size
     * @param current
     * @return 
     */
    public static Paging of(int count, int size, int current)
    {
        if(count <= 0 && size <= 0) return null;
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
     * 
     * @param value
     * @return 
     */
    public static Paging fromString(String value) 
    {
        if(value == null || value.trim().isEmpty()) return null;
        String[] arr = value.split(":");
        Integer count = Integer.valueOf(arr[0]);
        Integer size = Integer.valueOf(arr[1]);
        Integer current = Integer.valueOf(arr[2]);
        return Paging.of(count, size, current);
    }
    
    /**
     * 
     * @return 
     */
    public Limit toLimit()
    {
        return SQL.limit((current - 1) * pageSize, pageSize);
    }
}
