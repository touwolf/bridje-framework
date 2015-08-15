
package org.bridje.ioc.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.IocContext;

public class IocContextImpl implements IocContext
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
        //TODO: see how to create components that receive other components in the constructor.
        try
        {
            Constructor<?> defConstructor = null;
            Constructor<?>[] constructors = cls.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors)
            {
                if (constructor.getParameterTypes().length == 0)
                {
                    constructor.setAccessible(true);
                    defConstructor = constructor;
                    break;
                }
                
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) 
                {
                    Object find = find(parameterType);
                }               
            }
            if (defConstructor == null)
            {
                LOG.log(Level.WARNING, "Couldn't find a default constructor for {0}", cls.getName());
                return null;
            }
            return (T)defConstructor.newInstance();
        }
        catch (InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex)
        {
            LOG.warning(ex.getMessage());
        }
        return null;
    }
}
