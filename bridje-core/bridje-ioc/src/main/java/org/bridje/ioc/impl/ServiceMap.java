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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ServiceMap
{
    private final Map<Class, List<Class>> map;

    public ServiceMap(ClassList list)
    {
        map = new LinkedHashMap<>();
        for (Class component : list)
        {
            List<Class> services = findServices(component);
            for (Class service : services)
            {
                addComponentToService(service, component);
            }
        }
    }
    
    public <T> Class<? extends T> findOne(Class<T> service)
    {
        List<Class> lst = map.get(service);
        if(lst == null || lst.isEmpty())
        {
            return null;
        }
        return lst.get(0);
    }

    public <T> boolean exists(Class<T> service)
    {
        return map.containsKey(service);
    }
    
    public ClassList findAll(Class service)
    {
        return new ClassList(map.get(service));
    }

    private List<Class> findServices(Class component)
    {
        List<Class> result = new LinkedList<>();
        result.add(component);
        fillServicesSuperClasses(component, result);
        fillServicesIntefaces(component, result);
        return result;
    }
    
    private void fillServicesSuperClasses(Class component, List<Class> servicesList)
    {
        Class supClass = component.getSuperclass();
        while(supClass != null && supClass != Object.class)
        {
            if(!servicesList.contains(supClass))
            {
                servicesList.add(supClass);
            }
            supClass = supClass.getSuperclass();
        }
    }
    
    private void fillServicesIntefaces(Class cls, List<Class> servicesList)
    {
        Class[] interfaces = cls.getInterfaces();
        for (Class ifc : interfaces)
        {
            if(!servicesList.contains(ifc))
            {
                servicesList.add(ifc);
            }
            fillServicesIntefaces(ifc, servicesList);
        }
    }
    
    private void addComponentToService(Class service, Class component)
    {
        List<Class> components = map.get(service);
        if(components == null)
        {
            components = new LinkedList<>();
            map.put(service, components);
        }
        if(!components.contains(component))
        {
            components.add(component);
        }
    }
}
