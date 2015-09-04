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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.IocContext;

/**
 * 
 * @author gilberto
 */
class ContextImpl implements IocContext
{
    private static final Logger LOG = Logger.getLogger(ContextImpl.class.getName());
    
    private final String scope;

    private final ClassSet classSet;

    private final Instanciator creator;
    
    private final ServiceMap serviceMap;
    
    private final Container container;
    
    private final IocContext parent;

    private List<Register> registers;
    
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

    private ContextImpl(String scope, Collection instances, IocContext parent) throws IOException
    {
        this.scope = scope;
        this.parent = parent;
        creator = new Instanciator(this);
        if(instances != null && !instances.isEmpty())
        {
            ClassSet instancesClassSet = new ClassSet(ClassUtils.findClasses(instances));
            classSet = new ClassSet(ClassSet.findByScope(scope), instancesClassSet);
            serviceMap = new ServiceMap(ServiceMap.findByScope(scope), instancesClassSet);
        }
        else
        {
            classSet = ClassSet.findByScope(scope);
            serviceMap = ServiceMap.findByScope(scope);
        }
        container = new Container(creator, Arrays.asList(new Object[]{this}));
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
    public <T> T[] findAll(Class<T> service)
    {
        T[] result = findAllInternal(service);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findAll(service);
        }
        return null;
    }

    @Override
    public <T> T findGeneric(Type service, Class<T> resultCls)
    {
        T result = findGenericInternal(service, resultCls);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findGeneric(service, resultCls);
        }
        return null;
    }

    @Override
    public <T> T[] findAllGeneric(Type service, Class<T> resultCls)
    {
        T[] result = findAllGenericInternal(service, resultCls);
        if(result != null)
        {
            return result;
        }
        if(parent != null)
        {
            return parent.findAllGeneric(service, resultCls);
        }
        return null;
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
        if(serviceMap.exists(service))
        {
            return true;
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
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private <T> T findInternal(Class<T> service)
    {
        Class<? extends T> component = findRegisterService(service);
        if(component != null)
        {
            return container.create(component);
        }
        
        component = serviceMap.findOne(service);
        if(component != null)
        {
            return container.create(component);
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
        return null;
    }

    private <T> T findGenericInternal(Type service, Class<T> resultCls)
    {
        Class component = serviceMap.findOne(service);
        if(component != null)
        {
            return (T)container.create(component);
        }
        return null;
    }
    
    private <T> T[] findAllGenericInternal(Type service, Class<T> resultClass)
    {
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
            T[] result = (T[])Array.newInstance(resultClass, components.size());
            return (T[])resultList.toArray(result);
        }
        return null;
    }

    @Override
    public void register(Register... registers) 
    {
        if(null == this.registers)
        {
            this.registers = new LinkedList<>();
        }
        
        this.registers.addAll(Arrays.asList(registers));
    }

    private <T> Class<? extends T> findRegisterService(Class<T> service) 
    {
        for (Register register : registers) 
        {
            if(register.getService() == service)
            {
                return (Class<T>)register.getComponent();
            }
        }
        
        return null;
    }
}
