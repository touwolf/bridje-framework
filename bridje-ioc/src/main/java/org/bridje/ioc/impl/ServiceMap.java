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
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ServiceMap
{
    private static Map<String, ServiceMap> serviceMapCache;

    private final Map<Type, List<Class<?>>> map;
    
    private final Map<Class<?>, List<Type>> compMap;

    public ServiceMap(ServiceMap baseMap, ClassSet list)
    {
        map = new HashMap<>();
        compMap = new HashMap<>();
        if(baseMap != null)
        {
            map.putAll(baseMap.map);
            compMap.putAll(baseMap.compMap);
        }
        if(list != null)
        {
            for (Class component : list)
            {
                List<Type> services = findServices(component);
                compMap.put(component, services);
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
        return findOne(service, null);
    }

    public <T> Class<? extends T> findOne(Type service, Integer priority)
    {
        List<Class<?>> lst = map.get(service);
        if(lst == null || lst.isEmpty())
        {
            return null;
        }
        if(priority == null)
        {
            return (Class)lst.get(0);
        }
        else
        {
            for (Class<?> cls : lst)
            {
                int v1 = ClassUtils.findPriority(cls);
                if(v1 > priority || v1 == Integer.MAX_VALUE)
                {
                    return (Class)cls;
                }
            }
        }
        return null;
    }
    
    public <T> boolean exists(Type service)
    {
        return map.containsKey(service);
    }

    public List<Class<?>> findAll(Type service)
    {
        if(service instanceof WildcardType)
        {
            service = ClassUtils.typeOf((WildcardType)service);
        }
        if(service != null)
        {
            List<Class<?>> result = map.get(service);
            if(result == null)
            {
                return null;
            }
            return Collections.unmodifiableList(result);
        }
        return Collections.EMPTY_LIST;
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
    
    public List<Type> getServices(Class<?> component)
    {
        return compMap.get(component);
    }
    
    private List<Type> findServices(Class<?> component)
    {
        List<Type> result = new ArrayList<>();
        result.add(Object.class);
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
            Class cls = ClassUtils.rawClass(supClass);
            servicesList.add(cls);
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
            Class icfCls = ClassUtils.rawClass(ifc);
            servicesList.add(icfCls);
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
            int v1 = ClassUtils.findPriority(c1);
            int v2 = ClassUtils.findPriority(c2);
            return v1 - v2;
        });
    }

}
