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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bridje.ioc.annotations.Priority;

class ServiceMap
{
    private static Map<String, ServiceMap> serviceMapCache;

    private final Map<Type, List<Class<?>>> map;

    public ServiceMap(ServiceMap baseMap, ClassSet list)
    {
        map = new HashMap<>();
        if(baseMap != null)
        {
            map.putAll(baseMap.map);
        }
        if(list != null)
        {
            for (Class component : list)
            {
                List<Type> services = findServices(component);
                for (Type service : services)
                {
                    addComponentToService(service, component);
                }
            }
        }
        for (List<Class<?>> value : map.values())
        {
            sort(value);
        }
    }

    public <T> Class<? extends T> findOne(Type service)
    {
        List<Class<?>> lst = map.get(service);
        if(lst == null || lst.isEmpty())
        {
            return null;
        }
        return (Class)lst.get(0);
    }

    public <T> boolean exists(Type service)
    {
        return map.containsKey(service);
    }

    public List<Class<?>> findAll(Type service)
    {
        return Collections.unmodifiableList(map.get(service));
    }

    public static ServiceMap findByScope(String scope)
    {
        if(serviceMapCache == null)
        {
            serviceMapCache = new ConcurrentHashMap<>();
        }
        if(!serviceMapCache.containsKey(scope))
        {
            ClassSet classSet = ClassSet.findByScope(scope);
            if(classSet != null)
            {
                ServiceMap result = new ServiceMap(null, classSet);
                serviceMapCache.put(scope, result);
                return result;
            }
            return null;
        }
        return serviceMapCache.get(scope);
    }
    
    private List<Type> findServices(Class<?> component)
    {
        List<Type> result = new ArrayList<>();
        result.add(component);
        fillServicesSuperClasses(component, result);
        fillServicesIntefaces(component, result);
        return result;
    }

    private void fillServicesSuperClasses(Class<?> component, List<Type> servicesList)
    {
        Type supClass = component.getGenericSuperclass();
        while(supClass != null && supClass != Object.class)
        {
            if(!servicesList.contains(supClass))
            {
                servicesList.add(supClass);
            }
            Class cls = ClassUtils.findClassFromType(supClass);
            if(cls != null)
            {
                supClass = cls.getGenericSuperclass();
            }
            else
            {
                supClass = null;
            }
        }
    }
    
    private void fillServicesIntefaces(Class<?> cls, List<Type> servicesList)
    {
        Type[] interfaces = cls.getGenericInterfaces();
        for (Type ifc : interfaces)
        {
            if(!servicesList.contains(ifc))
            {
                servicesList.add(ifc);
            }
            Class icfCls = ClassUtils.findClassFromType(ifc);
            if(icfCls != null)
            {
                fillServicesIntefaces(icfCls, servicesList);
            }
        }
    }

    private void addComponentToService(Type service, Class<?> component)
    {
        List<Class<?>> components = map.get(service);
        if(components == null)
        {
            components = new ArrayList<>();
            map.put(service, components);
        }
        if(!components.contains(component))
        {
            components.add(component);
        }
    }

    private void sort(List<Class<?>> value)
    {
        Collections.sort(value, (Class<?> c1, Class<?> c2) ->
        {
            Priority a1 = c1.getAnnotation(Priority.class);
            Priority a2 = c2.getAnnotation(Priority.class);
            int v1 = Integer.MAX_VALUE;
            int v2 = Integer.MAX_VALUE;
            if(a1 != null)
            {
                v1 = a1.value();
            }
            if(a2 != null)
            {
                v2 = a2.value();
            }
            return v1 - v2;
        });
    }
}
