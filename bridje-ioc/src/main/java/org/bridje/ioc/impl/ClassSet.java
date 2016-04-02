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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.ClassRepository;
import org.bridje.ioc.ClassNavigator;
import org.bridje.ioc.MethodNavigator;
import org.bridje.ioc.FieldNavigator;

/**
 * This class represents a set of classes, his propouse is to serve as a
 * container for all the class that are to be managed by an IocContext instance.
 *
 */
class ClassSet implements Iterable<Class<?>>, ClassRepository
{
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(ComponentProcessor.class.getName());

    /**
     * All ClassSets availables by scope.
     */
    private static Map<String, ClassSet> classSetCache;

    /**
     * Al the components declared in the components.properties files.
     */
    private static Map<String, String> propFilesCache;

    /**
     * The set of classes for this intance.
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
     * @param classes The collection of classes to be present in this set of
     * classes.
     */
    public ClassSet(Collection<Class<?>> classes)
    {
        this();
        if (classes != null && !classes.isEmpty())
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
     * Constructor that receive an array of ClassSets.
     *
     * @param lsts The array of ClassSets to be present in this set of classes.
     */
    public ClassSet(ClassSet... lsts)
    {
        this();
        if (lsts != null && lsts.length > 0)
        {
            for (ClassSet clst : lsts)
            {
                if (clst != null && !clst.isEmpty())
                {
                    clsSet.addAll(clst.clsSet);
                }
            }
        }
    }

    /**
     * Determines whenever a class exist whiting the class set.
     *
     * @param cls The class to lookup.
     * @return true the class exists, false otherwise.
     */
    public boolean contains(Class cls)
    {
        return clsSet.contains(cls);
    }

    /**
     * Determines if this class set has any classes or not.
     *
     * @return true this CassSet does not have any classes, false otherwise.
     */
    public boolean isEmpty()
    {
        return clsSet.isEmpty();
    }

    /**
     * Gets the class iterator for this class set.
     *
     * @return An iterator of classes.
     */
    @Override
    public Iterator<Class<?>> iterator()
    {
        return clsSet.iterator();
    }

    /**
     * Finds a ClassSet that contains all the classes in the especified scope.
     *
     * @param scope The scope of the classes to lookup.
     * @return A ClassSet containing all the classes in the especified scope.
     */
    public static ClassSet findByScope(String scope)
    {
        if (classSetCache == null)
        {
            classSetCache = new ConcurrentHashMap<>();
        }
        if (classSetCache.containsKey(scope))
        {
            return classSetCache.get(scope);
        }
        try
        {
            ClassSet result = loadFromClassPath(scope);
            if (result != null)
            {
                classSetCache.put(scope, result);
                return result;
            }
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Load all classes of the especified scope from the class path.
     *
     * @param scope The scope to load.
     * @return A ClassSet containing all the classes in the scope, that where
     * found in the classpath.
     * @throws IOException If something whent wrong.
     */
    private static ClassSet loadFromClassPath(String scope) throws IOException
    {
        Set<Class<?>> clsList = new HashSet<>();
        //An instance of IocContextImpl is always a component in every scope.
        clsList.add(ContextImpl.class);
        if (propFilesCache == null)
        {
            propFilesCache = loadPropFilesCache();
        }
        for (Map.Entry<String, String> entrySet : propFilesCache.entrySet())
        {
            String clsName = entrySet.getKey();
            String compScope = entrySet.getValue();
            if (null != compScope && compScope.equalsIgnoreCase(scope))
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
        }
        if (clsList.isEmpty())
        {
            return null;
        }
        return new ClassSet(clsList);
    }

    /**
     * Loads all of the components.properties files in the class path.
     *
     * @return A map containing the combination of all the components.properties
     * files present in the class path.
     * @throws IOException If a file cannot be readed.
     */
    private static Map<String, String> loadPropFilesCache() throws IOException
    {
        Map<String, String> result = new HashMap<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(ComponentProcessor.COMPONENTS_RESOURCE_FILE);
        while (resources.hasMoreElements())
        {
            URL nextElement = resources.nextElement();
            Properties prop = new Properties();
            try (InputStream is = nextElement.openStream())
            {
                prop.load(is);
            }
            prop.forEach((key, value) -> 
            {
                String clsName = (String) key;
                String compScope = (String) value;
                result.put(clsName, compScope);
            });
        }
        return result;
    }

    /**
     * The size of this ClassSet.
     *
     * @return The amount of classes this ClassSet contains.
     */
    public int size()
    {
        return clsSet.size();
    }

    /**
     * Utility method to convert from array to Collection of classes.
     *
     * @param clss The array of clases to be converted.
     * @return A collection containing the classes in the passed array.
     */
    private static Collection<Class<?>> asList(Class<?>... clss)
    {
        if (clss == null || clss.length == 0)
        {
            return null;
        }
        return Arrays.asList(clss);
    }

    @Override
    public <A extends Annotation> void navigateAnnotMethods(Class<A> annotation, MethodNavigator<A> navigator)
    {
        for (Class<?> cls : this)
        {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods)
            {
                A annInst = method.getAnnotation(annotation);
                if (annInst != null)
                {
                    navigator.accept(method, cls, annInst);
                }
            }
        }
    }

    @Override
    public <A extends Annotation> void navigateAnnotFileds(Class<A> annotation, FieldNavigator<A> navigator)
    {
        for (Class<?> cls : this)
        {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields)
            {
                A annInst = field.getAnnotation(annotation);
                if (annInst != null)
                {
                    navigator.accept(field, cls, annInst);
                }
            }
        }
    }

    @Override
    public <A extends Annotation> void navigateAnnotClasses(Class<A> annotation, ClassNavigator<A> navigator)
    {
        for (Class<?> cls : this)
        {
            A annot = cls.getAnnotation(annotation);
            if (annot != null)
            {
                navigator.accept(cls, annot);
            }
        }
    }
}
