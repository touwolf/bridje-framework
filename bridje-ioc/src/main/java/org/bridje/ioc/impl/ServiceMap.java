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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An object to keep track of the services of the components.
 */
class ServiceMap
{
    /**
     * A cache for the ServiceMap of all scopes.
     */
    private static Map<String, ServiceMap> serviceMapCache;

    /**
     * The service map, who links a services to a list of components.
     */
    private final Map<Type, List<Class<?>>> map;

    /**
     * A map who links the components to a list of all it´s services.
     */
    private final Map<Class<?>, List<Type>> compMap;

    /**
     * Constructor for this class.
     *
     * @param baseMap A service map to base this service map from.
     * @param list The components to create this services map for.
     */
    public ServiceMap(ServiceMap baseMap, ClassSet list)
    {
        map = new HashMap<>();
        compMap = new HashMap<>();
        if (baseMap != null)
        {
            map.putAll(baseMap.map);
            compMap.putAll(baseMap.compMap);
        }
        if (list != null)
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
            ClassUtils.sort(value);
        }
    }

    /**
     * Finds the first component class by the given service type.
     *
     * @param service The type of the service.
     * @return The component class or null if none can be found.
     */
    public Class<?> findOne(Type service)
    {
        return findOne(service, null);
    }

    /**
     * Finds the first component class by the given service type.
     *
     * @param service The type of the service.
     * @param priority The priority
     * @return The component class or null if none can be found.
     */
    public Class<?> findOne(Type service, Integer priority)
    {
        List<Class<?>> lst = map.get(service);
        if (lst == null || lst.isEmpty())
        {
            return null;
        }
        if (priority == null)
        {
            return (Class) lst.get(0);
        }
        else
        {
            for (Class<?> cls : lst)
            {
                int v1 = ClassUtils.findPriority(cls);
                if (v1 > priority || v1 == Integer.MAX_VALUE)
                {
                    return (Class) cls;
                }
            }
        }
        return null;
    }

    /**
     * Determines whenever a service is provided by a least one component.
     *
     * @param service The serivce to look for.
     * @return true at least one component provides the given service.
     */
    public boolean exists(Type service)
    {
        return map.containsKey(service);
    }

    /**
     * Finds all components classes by the given service type.
     *
     * @param service The service to lookup
     * @return The list of components that provides the given service if any.
     */
    public List<Class<?>> findAll(Type service)
    {
        if (service instanceof WildcardType)
        {
            service = ClassUtils.typeOf((WildcardType) service);
        }
        if (service != null)
        {
            List<Class<?>> result = map.get(service);
            if (result == null)
            {
                return null;
            }
            return Collections.unmodifiableList(result);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Gets the service map by the given scope.
     *
     * @param scope The scope to look for.
     * @return A service map with all the service in the given scope or null if
     * the given scope has no components.
     */
    public static ServiceMap findByScope(String scope)
    {
        if (serviceMapCache == null)
        {
            serviceMapCache = new ConcurrentHashMap<>();
        }
        if (!serviceMapCache.containsKey(scope))
        {
            ClassSet classSet = ClassSet.findByScope(scope);
            if (classSet != null)
            {
                ServiceMap result = new ServiceMap(null, classSet);
                serviceMapCache.put(scope, result);
                return result;
            }
            return null;
        }
        return serviceMapCache.get(scope);
    }

    /**
     * Gets all the services provided by a component.
     *
     * @param component The component to look for.
     * @return The list of service the especified component provides.
     */
    public List<Type> getServices(Class<?> component)
    {
        return compMap.get(component);
    }

    /**
     * Creates a list of all services the especified component provides.
     *
     * @param component The component to search.
     * @return The list of all services the especified component provides.
     */
    private static List<Type> findServices(Class<?> component)
    {
        List<Type> result = new ArrayList<>();
        result.add(Object.class);
        result.add(component);
        fillServicesSuperClasses(component, result);
        fillServicesIntefaces(component, result);
        return result;
    }

    /**
     * Fills all the services provided by all supper classes of the component.
     *
     * @param component The component to look for it´s services for.
     * @param servicesList The list to put all finded services.
     */
    private static void fillServicesSuperClasses(Class<?> component, List<Type> servicesList)
    {
        Type supClass = component.getGenericSuperclass();
        while (supClass != null && supClass != Object.class)
        {
            if (!ClassUtils.hasGenericDeclaration(supClass))
            {
                if (!servicesList.contains(supClass))
                {
                    servicesList.add(supClass);
                }
            }
            Class cls = ClassUtils.rawClass(supClass);
            servicesList.add(cls);
            if (cls != null)
            {
                supClass = cls.getGenericSuperclass();
            }
            else
            {
                supClass = null;
            }
        }
    }

    /**
     * Fills all the services provided by all supper interfaces of the
     * component.
     *
     * @param component The component to look for it´s services for.
     * @param servicesList The list to put all finded services.
     */
    private static void fillServicesIntefaces(Class<?> cls, List<Type> servicesList)
    {
        Type[] interfaces = cls.getGenericInterfaces();
        for (Type ifc : interfaces)
        {
            if (!ClassUtils.hasGenericDeclaration(ifc))
            {
                if (!servicesList.contains(ifc))
                {
                    servicesList.add(ifc);
                }
            }
            Class icfCls = ClassUtils.rawClass(ifc);
            servicesList.add(icfCls);
            if (icfCls != null)
            {
                fillServicesIntefaces(icfCls, servicesList);
            }
        }
    }

    /**
     * Adds a component to a list of components that provides the given service.
     *
     * @param service The service provided
     * @param component The component who provides the given services.
     */
    private void addComponentToService(Type service, Class<?> component)
    {
        List<Class<?>> components = map.get(service);
        if (components == null)
        {
            components = new ArrayList<>();
            map.put(service, components);
        }
        if (!components.contains(component))
        {
            components.add(component);
        }
    }
}
