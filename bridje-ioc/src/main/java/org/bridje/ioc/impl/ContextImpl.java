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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bridje.ioc.Application;
import org.bridje.ioc.ClassRepository;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Scope;
import static org.bridje.ioc.impl.ClassUtils.*;

final class ContextImpl<S extends Scope> implements IocContext<S>
{
    private static final Logger LOG = Logger.getLogger(ContextImpl.class.getName());
    
    private final S scope;

    private final ClassSet classSet;

    private final ServiceMap serviceMap;
    
    private final Container container;
    
    private final IocContext<?> parent;
    
    private final ScopeCache cache;
    
    protected ContextImpl(S scope) throws IOException
    {
        this(scope, null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private ContextImpl(S scope, IocContext<?> parent) throws IOException
    {
        this.scope = scope;
        this.cache = GlobalCache.instance().getScope(getScopeClass());
        this.parent = parent;
        classSet = ClassSet.findByScope(getScopeClass());
        serviceMap = ServiceMap.findByScope(getScopeClass());
        Instanciator creator = new Instanciator(this, serviceMap);
        container = new Container(creator, scope, this);
        //Inject dependencies on the scope component.
        creator.injectDependencies(scope.getClass(), scope);
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
    public <T extends Scope> IocContext<T> createChild(T scope)
    {
        if(scope == null || scope.getClass().equals(Object.class) || scope.getClass().equals(Application.class))
        {
            throw new IllegalArgumentException("scope");
        }
        try
        {
            return new ContextImpl(scope, this);
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
            List<T> resultList = components.stream()
                    .map((component) -> (T)container.create(component))
                    .filter((compInstance) -> (compInstance != null))
                    .collect(Collectors.toList());
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
        Object[] result;
        List<Class<?>> components = serviceMap.findAll(service);
        if(components != null)
        {
            List resultList = components.stream()
                    .map((component) -> (Object)container.create(component))
                    .filter((compInstance) -> (compInstance != null))
                    .collect(Collectors.toList());
            result = (Object[])Array.newInstance(resultClass, resultList.size());
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
    public Class<S> getScopeClass()
    {
        return (Class<S>)scope.getClass();
    }

    @Override
    public S getScope()
    {
        return scope;
    }

    @Override
    public String toString()
    {
        return "IocContext: " + scope;
    }

    public ClassCache findCache(Class<?> cls)
    {
        return cache.getCache(cls);
    }
}
