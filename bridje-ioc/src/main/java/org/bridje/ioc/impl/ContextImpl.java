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
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.ClassRepository;
import org.bridje.ioc.IocContext;
import static org.bridje.ioc.impl.ClassUtils.*;

class ContextImpl implements IocContext
{
    private static final Logger LOG = Logger.getLogger(ContextImpl.class.getName());
    
    private final String scope;

    private final ClassSet classSet;

    private final ServiceMap serviceMap;
    
    private final Container container;
    
    private final IocContext parent;
    
    public ContextImpl() throws IOException
    {
        this("APPLICATION");
    }
    
    private ContextImpl(String scope) throws IOException
    {
        this(scope, null, null);
    }
    
    private ContextImpl(String scope, Collection instances) throws IOException
    {
        this(scope, instances, null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private ContextImpl(String scope, Collection instances, IocContext parent) throws IOException
    {
        this.scope = scope;
        this.parent = parent;
        if(instances != null && !instances.isEmpty())
        {
            ClassSet cs = new ClassSet(toClasses(instances));
            classSet = new ClassSet(ClassSet.findByScope(scope), cs);
            serviceMap = new ServiceMap(ServiceMap.findByScope(scope), cs);
        }
        else
        {
            classSet = ClassSet.findByScope(scope);
            serviceMap = ServiceMap.findByScope(scope);
        }
        Instanciator creator = new Instanciator(this, serviceMap);
        List allInstances = new ArrayList(instances != null ? instances.size() + 1 : 1);
        allInstances.add(this);
        if(instances != null)
        {
            allInstances.addAll(instances);
        }
        container = new Container(creator, allInstances);
    }

    @Override
    public <T> T find(Class<T> service)
    {
        T result = findInternal(service);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.find(service);
        }
        return null;
    }

    @Override
    public <T> T findNext(Class<T> service, int priority)
    {
        return (T)findNextGeneric(service, priority);
    }

    @Override
    public Object findGeneric(Type service)
    {
        Object result = findGenericInternal(service);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findGeneric(service);
        }
        return null;
    }
    
    @Override
    public Object findNextGeneric(Type service, int priority)
    {
        Object result = findGenericInternal(service, priority);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findNextGeneric(service, priority);
        }
        return null;
    }

    @Override
    public <T> T[] findAll(Class<T> service)
    {
        T[] result = findAllInternal(service);
        if(result != null && result.length > 0)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findAll(service);
        }
        return result;
    }

    @Override
    public boolean existsComponent(Class cls)
    {
        if(classSet.contains(cls))
        {
            return true;
        }
        if(parent != null)
        {
            return parent.existsComponent(cls);
        }
        return false;
    }

    @Override
    public boolean exists(Type service)
    {
        if(isMultiple(service))
        {
            Type type = multipleType(service);
            if(serviceMap.exists(type))
            {
                return true;
            }
        }
        else
        {
            if(serviceMap.exists(service))
            {
                return true;
            }
        }
        if(parent != null)
        {
            return parent.exists(service);
        }
        return false;
    }

    @Override
    public IocContext getParent()
    {
        return parent;
    }

    @Override
    public IocContext createChild(String scope)
    {
        return createChild(scope, null);
    }

    @Override
    public IocContext createChild(String scope, Collection instances)
    {
        try
        {
            return new ContextImpl(scope, instances, this);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private <T> T findInternal(Class<T> service)
    {
        if(service.isArray())
        {
            return (T)findGenericInternal(service);
        }
        else
        {
            Class<?> component = serviceMap.findOne(service);
            if(component != null)
            {
                return (T)container.create(component);
            }
        }
        return null;
    }

    private <T> T[] findAllInternal(Class<T> service)
    {
        List<Class<?>> components = serviceMap.findAll(service);
        if(components != null)
        {
            ArrayList resultList = new ArrayList(components.size());
            for (Class component : components)
            {
                Object compInstance = container.create(component);
                if(compInstance != null)
                {
                    resultList.add(compInstance);
                }
            }
            T[] result = (T[])Array.newInstance(service, components.size());
            return (T[])resultList.toArray(result);
        }
        return (T[])Array.newInstance(service, 0);
    }
    
    private Object findGenericInternal(Type service)
    {
        return findGenericInternal(service, null);
    }

    private Object findGenericInternal(Type service, Integer priority)
    {
        if(isMultiple(service))
        {
            Type type = multipleType(service);
            Object[] data = findAllGenericInternal(type);
            return createMultiple(service, data);
        }
        else
        {
            return findOneGenericInternal(service, priority);
        }
    }
    
    private Object findOneGenericInternal(Type service, Integer priority)
    {
        Class component = serviceMap.findOne(service, priority);
        if(component != null)
        {
            return container.create(component);
        }
        return null;
    }
    
    private Object[] findAllGenericInternal(Type service)
    {
        if(isMultiple(service))
        {
            return null;
        }
        Class resultClass = rawClass(service);
        if(resultClass == null)
        {
            return null;
        }
        Object[] result = null;
        List<Class<?>> components = serviceMap.findAll(service);
        if(components != null)
        {
            List resultList = new ArrayList(components.size());
            for (Class<?> component : components)
            {
                Object compInstance = container.create(component);
                if(compInstance != null)
                {
                    resultList.add(compInstance);
                }
            }
            result = (Object[])Array.newInstance(resultClass, components.size());
            result = resultList.toArray(result);
        }
        else
        {
            result = (Object[])Array.newInstance(resultClass, 0);
        }
        return result;
    }

    @Override
    public ClassRepository getClassRepository()
    {
        return classSet;
    }

    @Override
    public String getScope()
    {
        return scope;
    }

    @Override
    public String toString()
    {
        return "IocContext: " + scope;
    }
}
