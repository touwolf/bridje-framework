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
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import org.bridje.ioc.Priority;

class ClassUtils
{

    public static Type findParameterType(Parameter param)
    {
        if(param.getType().isArray())
        {
            return findTypeFromArray(param.getParameterizedType());
        }
        return param.getParameterizedType();
    }
    
    public static Type findTypeFromArray(Type supClass)
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
    
    public static Class findClassFromType(Type supClass)
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
        return null;
    }    

    public static Collection<Class<?>> findClasses(Collection instances)
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

    public static boolean isCollection(Type service)
    {
        Class cls = findClassFromType(service);
        if(cls == null)
        {
            return false;
        }
        return cls.getPackage().getName().equals("java.util") 
                && Collection.class.isAssignableFrom(cls) 
                && !Map.class.isAssignableFrom(cls);
    }

    public static boolean isMap(Type service)
    {
        Class cls = findClassFromType(service);
        if(cls == null)
        {
            return false;
        }
        return cls.getPackage().getName().equals("java.util") 
                && Map.class.isAssignableFrom(cls);
    }

    public static Collection createCollection(Class collectionCls, Object[] data) throws InstantiationException, IllegalAccessException
    {
        Collection res = null;
        Constructor construct = findDefConstructor(collectionCls);
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

    private static Constructor findDefConstructor(Class collectionCls)
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

    public static Map createMap(Class mapCls, Object[] data) throws InstantiationException, IllegalAccessException
    {
        Map map = null;
        Constructor construct = findDefConstructor(mapCls);
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
}
