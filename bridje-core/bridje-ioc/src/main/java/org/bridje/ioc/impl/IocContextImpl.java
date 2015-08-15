
package org.bridje.ioc.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.IocContext;

class IocContextImpl implements IocContext
{
    private static final Logger LOG = Logger.getLogger(IocContextImpl.class.getName());
    
    private final String scope;

    private final ClassList componensClasses;

    private final Map<Class, Object> components;

    public IocContextImpl(String scope) throws IOException
    {
        this.scope = scope;
        componensClasses = ClassList.loadFromClassPath(scope);
        components = new ConcurrentHashMap<>();
    }

    @Override
    public <T> T find(Class<T> cls)
    {
        if(!componensClasses.contains(cls))
        {
            return null;
        }
        
        if(components.containsKey(cls))
        {
            return (T)components.get(cls);
        }
        else
        {
            T obj = instantiate(cls);
            if(obj == null)
            {
                return null;
            }
            components.put(cls, obj);
            return obj;
        }
    }
    
    @SuppressWarnings("UseSpecificCatch")
    private <T> T instantiate(Class<T> cls)
    {
        try
        {
            Constructor<?> defConstructor = null;
            Constructor<?>[] constructors = cls.getDeclaredConstructors();
            List parameters = new LinkedList();
            for (Constructor<?> constructor : constructors)
            {
                //If it is not constructor by default we try to create components in the constructor how parameters.
                if (constructor.getParameterTypes().length != 0)
                {                    
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    parameters = new LinkedList();
                    for (Class<?> parameterType : parameterTypes) 
                    {
                        if(!componensClasses.contains(parameterType))
                        {
                            parameters = new LinkedList();
                            continue;
                        }
                        
                        parameters.add(find(parameterType));
                    }   
                }
                constructor.setAccessible(true);
                defConstructor = constructor;
                break;
            }
            if (defConstructor == null)
            {
                LOG.log(Level.WARNING, "Couldn't find a default constructor for {0}", cls.getName());
                return null;
            }
            return parameters.isEmpty() ? (T)defConstructor.newInstance() : (T)defConstructor.newInstance(parameters.toArray());
        }
        catch (InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex)
        {
            LOG.warning(ex.getMessage());
        }
        return null;
    }
}
