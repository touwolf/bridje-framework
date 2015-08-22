
package org.bridje.ioc.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import static javafx.scene.input.KeyCode.T;
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
        ClassList components = serviceMap.findAll(service);
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
        ClassList components = serviceMap.findAll(service);
        if(components != null)
        {
            List resultList = new LinkedList();
            for (Class component : components)
            {
                resultList.add(find(component));
            }
            T[] result = (T[])Array.newInstance(resultClass, components.size());
            return (T[])resultList.toArray(result);
        }
        return null;
    }
}
