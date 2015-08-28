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
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.bridje.ioc.IocContext;

/**
 * 
 * @author gilberto
 */
class IocContextImpl implements IocContext
{
    private static final Logger LOG = Logger.getLogger(IocContextImpl.class.getName());
    
    private final String scope;

    private final ClassSet classSet;

    private final Instanciator creator;
    
    private final ServiceMap serviceMap;
    
    private final Container container;

    public IocContextImpl(String scope) throws IOException
    {
        this.scope = scope;
        creator = new Instanciator(this);
        ClassSet pcc = new ClassSet(this.getClass());
        classSet = new ClassSet(ClassSet.findByScope(scope), pcc);
        serviceMap = new ServiceMap(ServiceMap.findByScope(scope), pcc);
        container = new Container(creator, Arrays.asList(new Object[]{this}));
    }

    @Override
    public <T> T find(Class<T> service)
    {
        Class<? extends T> component = serviceMap.findOne(service);
        if(component != null)
        {
            return container.create(component);
        }
        return null;
    }

    @Override
    public <T> T[] findAll(Class<T> service)
    {
        List<Class<?>> components = serviceMap.findAll(service);
        if(components != null)
        {
            ArrayList resultList = new ArrayList(components.size());
            for (Class component : components)
            {
                resultList.add(find(component));
            }
            T[] result = (T[])Array.newInstance(service, components.size());
            return (T[])resultList.toArray(result);
        }
        return null;
    }

    @Override
    public boolean existsComponent(Class cls)
    {
        return classSet.contains(cls);
    }

    @Override
    public boolean exists(Class service)
    {
        return serviceMap.exists(service);
    }

    @Override
    public <T> T findGeneric(Type service, Class<T> resultCls)
    {
        Class component = serviceMap.findOne(service);
        if(component != null)
        {
            return (T)container.create(component);
        }
        return null;
    }

    @Override
    public <T> T[] findAllGeneric(Type service, Class<T> resultClass)
    {
        List<Class<?>> components = serviceMap.findAll(service);
        if(components != null)
        {
            List resultList = new ArrayList(components.size());
            for (Class<?> component : components)
            {
                resultList.add(find(component));
            }
            T[] result = (T[])Array.newInstance(resultClass, components.size());
            return (T[])resultList.toArray(result);
        }
        return null;
    }
}
