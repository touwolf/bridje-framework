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

import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Priority;

/**
 * Utility method for class and type handling.
 */
public class ClassUtils
{
    private static final String JAVA_PACKAGE_PREFIX = "java.";

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(ClassUtils.class.getName());

    /**
     * Gets the upper bounds Type for a WildcarType.
     * <pre>
     * ? extends SomeService               = SomeService
     * ? extends SomeService&lt;String&gt; = SomeService&lt;String&gt;
     * </pre>
     *
     * @param service The wildcardType to inspect.
     *
     * @return The upper bounds Type of the WildcardType.
     */
    public static Type typeOf(WildcardType service)
    {
        Type[] upperBounds = service.getUpperBounds();
        if (upperBounds.length == 1)
        {
            return (upperBounds[0]);
        }
        else if (upperBounds.length == 0)
        {
            return Object.class;
        }
        return null;
    }

    /**
     * Find the Type of an array, map or collection.
     * <pre>
     * SomeService[]                 = SomeService
     * List&lt;SomeService&gt;       = SomeService
     * Map&lt;Class, SomeService&gt; = SomeService
     * </pre>
     *
     * @param service The inner type of the multiple type passed.
     *
     * @return A type representing the inner type for the collection, array or
     *         map passed.
     */
    public static Type multipleType(Type service)
    {
        if (isArray(service))
        {
            return arrayType(service);
        }
        else if (isCollection(service))
        {
            return collectionType(service);
        }
        else if (isMap(service))
        {
            return mapType(service);
        }
        return null;
    }

    /**
     * Find the Type of an array.
     * <pre>
     * SomeService[] = SomeService
     * </pre>
     *
     * @param service The inner type of the array type passed.
     *
     * @return A type representing the inner type for the array type passed.
     */
    public static Type arrayType(Type service)
    {
        if (service instanceof GenericArrayType)
        {
            Type rawType = ((GenericArrayType) service).getGenericComponentType();
            return rawType;
        }
        else if (service instanceof Class)
        {
            return ((Class) service).getComponentType();
        }
        return null;
    }

    /**
     * Find the Type of collection.
     * <pre>
     * List&lt;SomeService&gt; = SomeService
     * Set&lt;SomeService&gt;  = SomeService
     * </pre>
     *
     * @param service The inner type of the array type passed.
     *
     * @return A type representing the inner type for the collection type
     *         passed.
     */
    public static Type collectionType(Type service)
    {
        if (service instanceof ParameterizedType)
        {
            Type[] args = ((ParameterizedType) service).getActualTypeArguments();
            if (args.length == 1)
            {
                return args[0];
            }
        }
        return null;
    }

    /**
     * Find the Type of a map.
     * <pre>
     * Map&lt;Class, SomeService&gt;     = SomeService
     * HashMap&lt;Class, SomeService&gt; = SomeService
     * </pre>
     *
     * @param service The inner type of the array type passed.
     *
     * @return A type representing the inner type for the map type passed.
     */
    public static Type mapType(Type service)
    {
        if (service instanceof ParameterizedType)
        {
            Type[] args = ((ParameterizedType) service).getActualTypeArguments();
            if (args.length == 2)
            {
                return args[1];
            }
        }
        return null;
    }

    /**
     * Returns the specified parameter from the ParameterizedType.
     *
     * @param service The ParameterizedType type to search the parameter.
     * @param index   The index of the actual parameter.
     *
     * @return If service is a ParameterizedType and the parameter is available
     *         it will return it, or null otherwise.
     */
    public static Type parameterType(Type service, int index)
    {
        if (service instanceof ParameterizedType)
        {
            Type[] args = ((ParameterizedType) service).getActualTypeArguments();
            if (index < args.length)
            {
                return args[index];
            }
        }
        return null;
    }

    /**
     * Determines whenever the passed type is an array, a java collection like
     * List or Set, or a java Map.
     * <pre>
     * SomeService                       = false
     * SomeService[]                     = true
     * List&lt;SomeService&gt;           = true
     * Map&lt;Class, SomeService&gt;     = true
     * MyListImpl&lt;SomeService&gt;     = false
     * HashMap&lt;Class, SomeService&gt; = true
     * </pre>
     *
     * @param service The type to inspect.
     *
     * @return true if the especified type is an array, a collection or map from
     *         java languaje.
     */
    public static boolean isMultiple(Type service)
    {
        return isArray(service) || isCollection(service) || isMap(service);
    }

    /**
     * Determines whenever the passed type is an array.
     * <pre>
     * SomeService                       = false
     * SomeService[]                     = true
     * List&lt;SomeService&gt;           = false
     * List&lt;SomeService&gt;[]         = true
     * Map&lt;Class, SomeService&gt;     = false
     * MyListImpl&lt;SomeService&gt;     = false
     * HashMap&lt;Class, SomeService&gt; = false
     * </pre>
     *
     * @param service The type to inspect.
     *
     * @return true if the especified type is an array.
     */
    public static boolean isArray(Type service)
    {
        if (service instanceof Class)
        {
            return ((Class) service).isArray();
        }
        else
        {
            return service instanceof GenericArrayType;
        }
    }

    /**
     * Determines whenever the passed type is a collection.
     * <pre>
     * SomeService                       = false
     * SomeService[]                     = false
     * List&lt;SomeService&gt;           = true
     * List&lt;SomeService&gt;[]         = true
     * Map&lt;Class, SomeService&gt;     = false
     * MyListImpl&lt;SomeService&gt;     = false
     * HashMap&lt;Class, SomeService&gt; = false
     * </pre>
     *
     * @param service The type to inspect.
     *
     * @return true if the especified type is a List Set or other java
     *         collection except a map.
     */
    public static boolean isCollection(Type service)
    {
        Class cls = rawClass(service);
        if (cls == null || cls.isArray() || cls.getPackage() == null)
        {
            return false;
        }
        return isJavaPackage(cls.getPackage().getName())
                && Collection.class.isAssignableFrom(cls)
                && !Map.class.isAssignableFrom(cls);
    }

    /**
     * Determines whenever the passed type is a map.
     * <pre>
     * SomeService                       = false
     * SomeService[]                     = false
     * List&lt;SomeService&gt;           = false
     * List&lt;SomeService&gt;[]         = false
     * Map&lt;Class, SomeService&gt;     = true
     * MyListImpl&lt;SomeService&gt;     = false
     * HashMap&lt;Class, SomeService&gt; = true
     * </pre>
     *
     * @param service The type to inspect.
     *
     * @return true if the especified type is a java Map.
     */
    public static boolean isMap(Type service)
    {
        Class cls = rawClass(service);
        if (cls == null || cls.isArray() || cls.getPackage() == null)
        {
            return false;
        }
        return isJavaPackage(cls.getPackage().getName())
                && Map.class.isAssignableFrom(cls);
    }

    /**
     * Gets the raw class for the especified Type.
     * <pre>
     * SomeService                       = SomeService
     * SomeService[]                     = SomeService[]
     * List&lt;SomeService&gt;           = List
     * List&lt;SomeService&gt;[]         = List[]
     * Map&lt;Class, SomeService&gt;     = Map
     * MyListImpl&lt;SomeService&gt;     = MyListImpl
     * HashMap&lt;Class, SomeService&gt; = HashMap
     * </pre>
     *
     * @param service The type to take the raw class from.
     *
     * @return The raw class for the given type.
     */
    public static Class rawClass(Type service)
    {
        if (service instanceof Class)
        {
            return ((Class) service);
        }
        else if (service instanceof ParameterizedType)
        {
            Type rawType = ((ParameterizedType) service).getRawType();
            if (rawType instanceof Class)
            {
                return ((Class) rawType);
            }
        }
        else if (service instanceof WildcardType)
        {
            Type wildCardType = typeOf((WildcardType) service);
            return rawClass(wildCardType);
        }
        return null;
    }

    /**
     * Gets a collection with all the classes of the objects from the especified
     * collection.
     *
     * @param instances The objects to obtain it´s classes from.
     *
     * @return A Collection object with all the classes of the object´s in the
     *         collection passed.
     */
    public static Collection<Class<?>> toClasses(Collection instances)
    {
        List<Class<?>> arrList = new ArrayList<>();
        for (Object instance : instances)
        {
            arrList.add(instance.getClass());
        }
        return arrList;
    }

    /**
     * Finds te priority value for a class, from it´s @Priority annotation if it
     * haveit.
     *
     * @param cls The class to find it´s priority.
     *
     * @return The int value of the priority, by default the priority will be
     *         Integer.MAX_VALUE if not especified directly in the component.
     */
    public static int findPriority(Class<?> cls)
    {
        Priority a1 = cls.getAnnotation(Priority.class);
        int v1 = Integer.MAX_VALUE;
        if (a1 != null)
        {
            v1 = a1.value();
        }
        return v1;
    }

    /**
     * Given a multiple type, (array, collection, or map) this method will
     * return the appropied instance for it.
     * <pre>
     * SomeService                       = null
     * SomeService[]                     = SomeService[]
     * List&lt;SomeService&gt;           = ArrayList
     * List&lt;SomeService&gt;[]         = List[]
     * Map&lt;Class, SomeService&gt;     = Map
     * MyListImpl&lt;SomeService&gt;     = null
     * HashMap&lt;Class, SomeService&gt; = HashMap
     * </pre>
     *
     * @param service The type of the array, collection or map.
     * @param data    The data to put on the result listing.
     *
     * @return The proper object for the especified type.
     */
    public static Object createMultiple(Type service, Object[] data)
    {
        try
        {
            if (data != null)
            {
                if (ClassUtils.isCollection(service))
                {
                    Class resultClass = rawClass(service);
                    return ClassUtils.createCollection(resultClass, data);
                }
                else if (ClassUtils.isMap(service))
                {
                    Class resultClass = rawClass(service);
                    return ClassUtils.createCollection(resultClass, data);
                }
                else if (ClassUtils.isArray(service))
                {
                    return data;
                }
            }
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Given a collection type this method will return the appropied instance
     * for it.
     * <pre>
     * SomeService                       = null
     * SomeService[]                     = null
     * List&lt;SomeService&gt;           = ArrayList
     * LinkedList&lt;SomeService&gt;     = LinkedList
     * List&lt;SomeService&gt;[]         = null
     * Map&lt;Class, SomeService&gt;     = null
     * MyListImpl&lt;SomeService&gt;     = null
     * HashMap&lt;Class, SomeService&gt; = null
     * </pre>
     *
     * @param collectionCls The class for the collection.
     * @param data          The data for the collection.
     *
     * @return The collection.
     *
     * @throws InstantiationException if the collection cannot be instance.
     * @throws IllegalAccessException security exception.
     */
    public static Collection createCollection(Class collectionCls, Object[] data) throws InstantiationException, IllegalAccessException
    {
        Collection res = null;
        Constructor construct = findConstructor(collectionCls);
        if (construct != null && construct.isAccessible())
        {
            res = (Collection) collectionCls.newInstance();
        }
        else if (collectionCls.isAssignableFrom(ArrayList.class))
        {
            res = new ArrayList(data.length);
        }
        else if (collectionCls.isAssignableFrom(LinkedHashSet.class))
        {
            res = new LinkedHashSet(data.length);
        }
        else if (collectionCls.isAssignableFrom(PriorityBlockingQueue.class))
        {
            res = new PriorityBlockingQueue(data.length);
        }
        if (res != null)
        {
            res.addAll(Arrays.asList(data));
        }
        return res;
    }

    /**
     * Given a map type this method will return the appropied instance for it.
     * <pre>
     * SomeService                       = null
     * SomeService[]                     = null
     * List&lt;SomeService&gt;           = null
     * List&lt;SomeService&gt;[]         = null
     * Map&lt;Class, SomeService&gt;     = Map
     * MyListImpl&lt;SomeService&gt;     = null
     * HashMap&lt;Class, SomeService&gt; = HashMapl
     * </pre>
     *
     * @param mapCls The clss for the map.
     * @param data   The data for the map.
     *
     * @return The map.
     *
     * @throws InstantiationException if the collection cannot be instance.
     * @throws IllegalAccessException security exception.
     */
    public static Map createMap(Class mapCls, Object[] data) throws InstantiationException, IllegalAccessException
    {
        Map map = null;
        Constructor construct = findConstructor(mapCls);
        if (construct != null && construct.isAccessible())
        {
            map = (Map) mapCls.newInstance();
        }
        else if (mapCls.isAssignableFrom(LinkedHashMap.class))
        {
            map = new LinkedHashMap(data.length);
        }
        if (map != null)
        {
            for (Object cmp : data)
            {
                map.put(cmp.getClass(), cmp);
            }
        }
        return map;
    }

    /**
     * Finds the default constructor for the especified class.
     *
     * @param cls The class to find the constructor for.
     *
     * @return The default constructor for the class of null if none can be
     *         found.
     */
    private static Constructor findConstructor(Class cls)
    {
        Constructor[] constructors = cls.getConstructors();
        for (Constructor constructor : constructors)
        {
            if (constructor.getParameterCount() == 0)
            {
                return constructor;
            }
        }
        return null;
    }

    /**
     * Sorts a list of components by priority.
     *
     * @param value The list of classes to sort.
     */
    public static void sort(List<Class<?>> value)
    {
        Collections.sort(value, (Class<?> c1, Class<?> c2)
                ->
        {
            int v1 = ClassUtils.findPriority(c1);
            int v2 = ClassUtils.findPriority(c2);
            return Integer.compare(v1, v2);
        });
    }

    /**
     * Determines whenever the especified type has a wildcard or TypeVariable
     * declaration whiting.
     * <pre>
     * SomeService&lt;T&gt;      = true
     * SomeService&lt;?&gt;      = true
     * SomeService               = false
     * SomeService&lt;String&gt; = false
     * </pre>
     *
     * @param type The type to inspect.
     *
     * @return true the especified type has a generic declaration, false
     *         otherwise.
     */
    public static boolean hasGenericDeclaration(Type type)
    {
        if (type instanceof ParameterizedType)
        {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] types = pType.getActualTypeArguments();
            for (Type t : types)
            {
                if (hasGenericDeclaration(t))
                {
                    return true;
                }
            }
        }
        return type instanceof TypeVariable || type instanceof WildcardType;
    }

    private static boolean isJavaPackage(String name)
    {
        return name.startsWith(JAVA_PACKAGE_PREFIX);
    }

}
