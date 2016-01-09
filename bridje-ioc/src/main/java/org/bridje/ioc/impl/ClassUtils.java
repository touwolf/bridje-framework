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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Priority;

class ClassUtils
{
    private static final Logger LOG = Logger.getLogger(ClassUtils.class.getName());

    public static Type typeOf(Parameter param)
    {
        if(param.getType().isArray())
        {
            return arrayType(param.getParameterizedType());
        }
        return param.getParameterizedType();
    }

    public static Type typeOf(WildcardType service)
    {
        Type[] upperBounds = ((WildcardType)service).getUpperBounds();
        if(upperBounds.length == 1)
        {
            return (upperBounds[0]);
        }
        else if(upperBounds.length == 0)
        {
            return Object.class;
        }
        return null;
    }

    public static Type multipleType(Type service)
    {
        if(isArray(service))
        {
            return arrayType(service);
        }
        else if(isCollection(service))
        {
            return collectionType(service);
        }
        else if(isMap(service))
        {
            return mapType(service);
        }
        return null;
    }

    public static Type arrayType(Type supClass)
    {
        if(supClass instanceof GenericArrayType)
        {
            Type rawType = ((GenericArrayType)supClass).getGenericComponentType();
            return rawType;
        }
        else if(supClass instanceof Class)
        {
            return ((Class)supClass).getComponentType();
        }
        return null;
    }

    public static Type collectionType(Type supClass)
    {
        if(supClass instanceof ParameterizedType)
        {
            Type[] args = ((ParameterizedType) supClass).getActualTypeArguments();
            if(args.length == 1)
            {
                return args[0];
            }
        }
        return null;
    }

    public static Type mapType(Type supClass)
    {
        if(supClass instanceof ParameterizedType)
        {
            Type[] args = ((ParameterizedType) supClass).getActualTypeArguments();
            if(args.length == 2)
            {
                return args[1];
            }
        }
        return null;
    }

    public static boolean isMultiple(Type service)
    {
        return isArray(service) || isCollection(service) || isMap(service);
    }

    public static boolean isArray(Type supClass)
    {
        if(supClass instanceof Class)
        {
            return ((Class) supClass).isArray();
        }
        else
        {
            return supClass instanceof GenericArrayType;
        }
    }
    
    public static boolean isCollection(Type service)
    {
        Class cls = rawClass(service);
        if(cls == null || cls.isArray() || cls.getPackage() == null)
        {
            return false;
        }
        return cls.getPackage().getName().startsWith("java.") 
                && Collection.class.isAssignableFrom(cls) 
                && !Map.class.isAssignableFrom(cls);
    }

    public static boolean isMap(Type service)
    {
        Class cls = rawClass(service);
        if(cls == null || cls.isArray() || cls.getPackage() == null)
        {
            return false;
        }
        return cls.getPackage().getName().startsWith("java.") 
                && Map.class.isAssignableFrom(cls);
    }

    public static Class rawClass(Type supClass)
    {
        if(supClass instanceof Class)
        {
            return ((Class)supClass);
        }
        else if(supClass instanceof ParameterizedType)
        {
            Type rawType = ((ParameterizedType)supClass).getRawType();
            if(rawType instanceof Class)
            {
                return ((Class)rawType);
            }
        }
        else if(supClass instanceof WildcardType)
        {
            Type wildCardType = typeOf((WildcardType)supClass);
            return rawClass(wildCardType);
        }
        return null;
    }

    public static Collection<Class<?>> toClasses(Collection instances)
    {
        List<Class<?>> arrList = new ArrayList();
        for (Object instance : instances)
        {
            arrList.add(instance.getClass());
        }
        return arrList;
    }

    public static int findPriority(Class<?> cls)
    {
        Priority a1 = cls.getAnnotation(Priority.class);
        int v1 = Integer.MAX_VALUE;
        if(a1 != null)
        {
            v1 = a1.value();
        }
        return v1;
    }
    
    public static Object createMultiple(Type service, Object[] data)
    {
        try
        {
            if(data != null)
            {
                if(ClassUtils.isCollection(service))
                {
                    Class resultClass = rawClass(service);
                    return ClassUtils.createCollection(resultClass, data);
                }
                else if(ClassUtils.isMap(service))
                {
                    Class resultClass = rawClass(service);
                    return ClassUtils.createCollection(resultClass, data);
                }
                else if(ClassUtils.isArray(service))
                {
                    return data;
                }
            }
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public static Collection createCollection(Class collectionCls, Object[] data) throws InstantiationException, IllegalAccessException
    {
        Collection res = null;
        Constructor construct = findConstructor(collectionCls);
        if(construct != null && construct.isAccessible())
        {
            res = (Collection) collectionCls.newInstance();
        }
        else if(collectionCls.isAssignableFrom(LinkedList.class))
        {
            res = new ArrayList(data.length);
        }
        else if(collectionCls.isAssignableFrom(LinkedHashSet.class))
        {
            res = new LinkedHashSet(data.length);
        }
        else if(collectionCls.isAssignableFrom(PriorityBlockingQueue.class))
        {
            PriorityBlockingQueue q = new PriorityBlockingQueue(data.length);
        }
        if(res != null)
        {
            res.addAll(Arrays.asList(data));
        }
        return res;
    }

    public static Map createMap(Class mapCls, Object[] data) throws InstantiationException, IllegalAccessException
    {
        Map map = null;
        Constructor construct = findConstructor(mapCls);
        if(construct != null && construct.isAccessible())
        {
            map = (Map) mapCls.newInstance();
        }
        else if(mapCls.isAssignableFrom(LinkedHashMap.class))
        {
            map = new LinkedHashMap(data.length);
        }
        if(map != null)
        {
            for (Object cmp : data)
            {
                map.put(cmp.getClass(), cmp);
            }
        }
        return map;
    }

    private static Constructor findConstructor(Class collectionCls)
    {
        Constructor[] constructors = collectionCls.getConstructors();
        for (Constructor constructor : constructors)
        {
            if(constructor.getParameterCount() == 0)
            {
                return constructor;
            }
        }
        return null;
    }    
}
