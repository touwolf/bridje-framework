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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import org.bridje.ioc.ClassNavigator;
import org.bridje.ioc.ClassRepository;
import org.bridje.ioc.FieldNavigator;
import org.bridje.ioc.MethodNavigator;

/**
 * This class represents a set of classes, his propouse is to serve as a
 * container for all the class that are to be managed by an IocContext instance.
 * <p>
 */
class ClassSet implements Iterable<Class<?>>, ClassRepository
{
    /**
     * The set of classes for this instance.
     */
    private final Set<Class<?>> clsSet;

    /**
     * The ordered list of classes for this instance
     */
    private final List<Class<?>> sortedClasses;

    /**
     * Default constructor for internal use of this class only.
     */
    private ClassSet()
    {
        clsSet = new HashSet<>();
        sortedClasses = new ArrayList<>();
    }

    /**
     * Constructor that receive an arbitrary collection of classes.
     *
     * @param classes The collection of classes to be present in this set of
     *                classes.
     */
    public ClassSet(Collection<Class<?>> classes)
    {
        this();
        if (classes != null && !classes.isEmpty())
        {
            clsSet.addAll(classes);
            sortedClasses.addAll(clsSet);
            ClassUtils.sort(sortedClasses);
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
        sortedClasses.addAll(clsSet);
        ClassUtils.sort(sortedClasses);
    }

    /**
     * Determines whenever a class exist whiting the class set.
     *
     * @param cls The class to lookup.
     *
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
        return sortedClasses.iterator();
    }

    /**
     * Finds a ClassSet that contains all the classes in the specified scope.
     *
     * @param scope The scope of the classes to lookup.
     *
     * @return A ClassSet containing all the classes in the specified scope.
     */
    public static ClassSet findByScope(Class<?> scope)
    {
        return ClassSetLoader.instance().findByScope(scope);
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
     *
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
    public <A extends Annotation> void forEachMethod(Class<A> annotation, MethodNavigator<A> navigator)
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
    public <A extends Annotation> void forEachField(Class<A> annotation, FieldNavigator<A> navigator)
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
    public <A extends Annotation> void forEachClass(Class<A> annotation, ClassNavigator<A, Object> navigator)
    {
        for (Class<?> cls : this)
        {
            A annot = cls.getAnnotation(annotation);
            if (annot != null)
            {
                navigator.accept((Class<Object>) cls, annot);
            }
        }
    }

    @Override
    public <A extends Annotation, T> void forEachClass(Class<A> annotation, Class<T> service, ClassNavigator<A, T> navigator)
    {
        for (Class<?> cls : this)
        {
            A annot = cls.getAnnotation(annotation);
            if (annot != null && service.isAssignableFrom(cls))
            {
                navigator.accept((Class<T>) cls, annot);
            }
        }
    }

}
