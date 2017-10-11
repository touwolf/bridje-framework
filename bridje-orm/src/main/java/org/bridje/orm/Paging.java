
package org.bridje.orm;

import org.bridje.sql.Limit;
import org.bridje.sql.SQL;

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

    public int getRecordCount()
    {
        return recordCount;
    }

    public int getPageSize()
    {
        return pageSize;
    }

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
    
    public int getPageCount()
    {
        return pageCount;
    }

    public Page getCurrent()
    {
        if(pages == null) return Page.of(current);
        return pages[current - 1];
    }

    public void setCurrent(Page current)
    {
        this.current = current.getValue();
    }
    
    public void setCurrent(int current)
    {
        this.current = current;
    }

    public Page getPage(int value)
    {
        if(pageCount == 0) return null;
        if(value <= 0) return pages[0];
        getPages();
        if(value > pageCount) return pages[pageCount-1];
        return pages[value-1];
    }

    public static Paging of(int count, int size)
    {
        if(count <= 0 && size <= 0) return null;
        return new Paging(count, size);
    }

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

    public static Paging fromString(String value) 
    {
        if(value == null || value.trim().isEmpty()) return null;
        String[] arr = value.split(":");
        Integer count = Integer.valueOf(arr[0]);
        Integer size = Integer.valueOf(arr[1]);
        Integer current = Integer.valueOf(arr[2]);
        return Paging.of(count, size, current);
    }
    
    public Limit toLimit()
    {
        return SQL.limit((current - 1) * pageSize, pageSize);
    }
}
