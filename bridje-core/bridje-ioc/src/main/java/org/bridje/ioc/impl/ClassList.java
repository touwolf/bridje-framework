
package org.bridje.ioc.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.ContextAnnotProcessor;

class ClassList implements Iterable<Class<?>>
{
    private static final Logger LOG = Logger.getLogger(ContextAnnotProcessor.class.getName());

    private final List<Class<?>> list;

    private ClassList()
    {
        list = new ArrayList<>();
    }
    
    public ClassList(Collection<Class<?>> classes)
    {
        this();
        if(classes != null)
        {
            list.addAll(classes);
        }
    }
    
    public ClassList(Class<?>... classes)
    {
        this(Arrays.asList(classes));
    }

    public ClassList(ClassList... lsts)
    {
        this();
        for (ClassList clst : lsts)
        {
            for (Class cls : clst)
            {
                if(!list.contains(cls))
                {
                    list.add(cls);
                }
            }
        }
    }
    
    public Class getLast()
    {
        return list.get(list.size()-1);
    }
    
    public Class getFirst()
    {
        if(isEmpty())
        {
            return null;
        }
        
        return list.get(0);
    }

    public Class get(int index)
    {
        if(index >= list.size())
        {
            return null;
        }
        
        return list.get(index);
    }
    
    public boolean contains(Class cls)
    {
        return list.contains(cls);
    }
    
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    @Override
    public Iterator<Class<?>> iterator()
    {
        return list.iterator();
    }

    public static ClassList loadFromClassPath(String scope) throws IOException
    {
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(ContextAnnotProcessor.COMPONENTS_RESOURCE_FILE);
        List<Class<?>> clsList = new ArrayList<>();
        while (resources.hasMoreElements())
        {
            URL nextElement = resources.nextElement();
            Properties prop = new Properties();
            try(InputStream is = nextElement.openStream())
            {
                prop.load(is);
            }
            prop.forEach((key, value) -> 
            {
                String clsName = (String)key;
                String compScope = (String)value;
                if(null != compScope && compScope.equalsIgnoreCase(scope))
                {
                    try
                    {
                        clsList.add(Class.forName(clsName));
                    }
                    catch (ClassNotFoundException ex)
                    {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        return new ClassList(clsList);
    }

    public int size()
    {
        return list.size();
    }
}
