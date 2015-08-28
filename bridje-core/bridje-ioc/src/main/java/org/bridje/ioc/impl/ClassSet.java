/*
 * Copyright 2015 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.ioc.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.ComponentAnnotProcessor;

/**
 * This class represents a set of classes, his propouse is to serve as a container
 * for all the class that are to be managed by an IocContext instance.
 * 
 * @author gilberto
 */
class ClassSet implements Iterable<Class<?>>
{
    private static final Logger LOG = Logger.getLogger(ComponentAnnotProcessor.class.getName());

    private static Map<String,ClassSet> classSetCache;
    
    /**
     * 
     */
    private final Set<Class<?>> clsSet;

    /**
     * Default constructor for internal use of this class only.
     */
    private ClassSet()
    {
        clsSet = new HashSet<>();
    }
    
    /**
     * Constructor that receive an arbitrary collection of classes.
     * 
     * @param classes The collection of classes to be present in this set of classes.
     */
    public ClassSet(Collection<Class<?>> classes)
    {
        this();
        if(classes != null && !classes.isEmpty())
        {
            clsSet.addAll(classes);
        }
    }
    
    /**
     * Constructor that receive an array of classes.
     * 
     * @param classes The array of classes to be present in this set of classes.
     */
    public ClassSet(Class<?>... classes)
    {
        this(asList(classes));
    }

    /**
     * 
     * @param lsts 
     */
    public ClassSet(ClassSet... lsts)
    {
        this();
        if(lsts != null && lsts.length > 0)
        {
            for (ClassSet clst : lsts)
            {
                if(clst != null && !clst.isEmpty())
                {
                    clsSet.addAll(clst.clsSet);
                }
            }
        }
    }
    
    /**
     * 
     * @param cls
     * @return 
     */
    public boolean contains(Class cls)
    {
        return clsSet.contains(cls);
    }
    
    /**
     * 
     * @return 
     */
    public boolean isEmpty()
    {
        return clsSet.isEmpty();
    }

    /**
     * 
     * @return 
     */
    @Override
    public Iterator<Class<?>> iterator()
    {
        return clsSet.iterator();
    }
    
    /**
     * 
     * @param scope
     * @return 
     */
    public static ClassSet findByScope(String scope)
    {
        if(classSetCache == null)
        {
            classSetCache = new ConcurrentHashMap<>();
        }
        if(!classSetCache.containsKey(scope))
        {
            try
            {
                ClassSet result = loadFromClassPath(scope);
                if(result != null)
                {
                    classSetCache.put(scope, result);
                }
                return result;
            }
            catch(Exception ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
            return null;
        }
        return classSetCache.get(scope);
    }
    
    /**
     * 
     * @param scope
     * @return
     * @throws IOException 
     */
    private static ClassSet loadFromClassPath(String scope) throws IOException
    {
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(ComponentAnnotProcessor.COMPONENTS_RESOURCE_FILE);
        Set<Class<?>> clsList = new HashSet<>();
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
        if(clsList.isEmpty())
        {
            return null;
        }
        return new ClassSet(clsList);
    }

    /**
     * 
     * @return 
     */
    public int size()
    {
        return clsSet.size();
    }
    
    /**
     * 
     * @param clss
     * @return 
     */
    private static Collection<Class<?>> asList(Class<?>... clss)
    {
        if(clss == null || clss.length == 0)
        {
            return null;
        }
        return Arrays.asList(clss);
    }
}
