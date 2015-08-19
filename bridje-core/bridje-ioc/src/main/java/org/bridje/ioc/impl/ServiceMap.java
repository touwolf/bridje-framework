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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ServiceMap
{
    private final Map<ServiceInfo, List<Class>> map;

    public ServiceMap(ClassList list)
    {
        map = new LinkedHashMap<>();
        for (Class component : list)
        {
            List<ServiceInfo> services = findServices(component);
            for (ServiceInfo service : services)
            {
                addComponentToService(service, component);
            }
        }
    }
    
    public <T> Class<? extends T> findOne(ServiceInfo service)
    {
        List<Class> lst = map.get(service);
        if(lst == null || lst.isEmpty())
        {
            return null;
        }
        return lst.get(0);
    }

    public <T> boolean exists(ServiceInfo service)
    {
        return map.containsKey(service);
    }
    
    public ClassList findAll(ServiceInfo service)
    {
        return new ClassList(map.get(service));
    }

    private List<ServiceInfo> findServices(Class component)
    {
        List<ServiceInfo> result = new LinkedList<>();
        result.add(new ServiceInfo(component, null));
        fillServicesSuperClasses(component, result);
        fillServicesIntefaces(component, result);
        return result;
    }
    
    private void fillServicesSuperClasses(Class component, List<ServiceInfo> servicesList)
    {
        Type supClass = component.getGenericSuperclass();
        while(supClass != null && supClass != Object.class)
        {
            ServiceInfo serviceInf = ServiceInfo.createServiceInf(supClass);
            if(!servicesList.contains(serviceInf))
            {
                servicesList.add(serviceInf);
            }
            supClass = serviceInf.getMainClass().getGenericSuperclass();
        }
    }
    
    private void fillServicesIntefaces(Class cls, List<ServiceInfo> servicesList)
    {
        Type[] interfaces = cls.getGenericInterfaces();
        for (Type ifc : interfaces)
        {
            ServiceInfo serviceInf = ServiceInfo.createServiceInf(ifc);
            if(!servicesList.contains(serviceInf))
            {
                servicesList.add(serviceInf);
            }
            fillServicesIntefaces(serviceInf.getMainClass(), servicesList);
        }
    }

    private void addComponentToService(ServiceInfo service, Class component)
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
