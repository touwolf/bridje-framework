
package org.bridje.ioc.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.bridje.ioc.IocContext;

class IocContextImpl implements IocContext
{
    private static final Logger LOG = Logger.getLogger(IocContextImpl.class.getName());
    
    private final String scope;

    private final ClassList componensClasses;

    private final ComponentCreator componentCreator;
    
    private final ServiceMap serviceMap;
    
    private final IocContainer container;

    public IocContextImpl(String scope) throws IOException
    {
        this.scope = scope;
        componentCreator = new ComponentCreator(this);
        componensClasses = new ClassList(ClassList.loadFromClassPath(scope), new ClassList(this.getClass()));
        serviceMap = new ServiceMap(componensClasses);
        container = new IocContainer(componentCreator, Arrays.asList(new Object[]{this}));
    }

    @Override
    public <T> T find(Class<T> service)
    {
        Class<? extends T> component = serviceMap.findOne(new ServiceInfo(service, null));
        if(component != null)
        {
            return container.create(component);
        }
        return null;
    }

    @Override
    public <T> T[] findAll(Class<T> service)
    {
        ClassList components = serviceMap.findAll(new ServiceInfo(service, null));
        if(components != null)
        {
            List resultList = new LinkedList();
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
        return componensClasses.contains(cls);
    }

    @Override
    public boolean exists(Class service)
    {
        return serviceMap.exists(new ServiceInfo(service, null));
    }

    @Override
    public <T> T find(Class<T> service, Class[] params)
    {
        Class<? extends T> component = serviceMap.findOne(new ServiceInfo(service, params));
        if(component != null)
        {
            return container.create(component);
        }
        return null;
    }

    @Override
    public <T> T[] findAll(Class<T> service, Class[] params)
    {
        ClassList components = serviceMap.findAll(new ServiceInfo(service, null));
        if(components != null)
        {
            List resultList = new LinkedList();
            for (Class component : components)
            {
                resultList.add(find(component));
            }
            T[] result = (T[])Array.newInstance(service, components.size());
            return (T[])resultList.toArray(result);
        }
        return null;
    }
}
