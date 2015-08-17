/*
 * Copyright 2015 Bridje.org.
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.Inject;
import org.bridje.ioc.IocContext;

class ComponentCreator
{
    private static final Logger LOG = Logger.getLogger(ComponentCreator.class.getName());

    private final IocContext context;

    public ComponentCreator(IocContext context)
    {
        this.context = context;
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T> T instantiate(Class<T> cls)
    {
        try
        {
            Constructor<?> constructor = findConstructorWithParameters(cls);
            if(constructor == null)
            {
                constructor = findDefaultConstructor(cls);
            }
            if(constructor == null)
            {
                LOG.log(Level.WARNING, "Couldn't find a default constructor for {0}", cls.getName());
                return null;
            }
            constructor.setAccessible(true);
            Object[] parameters = findParameters(constructor);
            return parameters.length == 0 ? (T)constructor.newInstance() : (T)constructor.newInstance(parameters);
        }
        catch (InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex)
        {
            LOG.warning(ex.getMessage());
        }
        return null;
    }

    private Constructor<?> findConstructorWithParameters(Class cls)
    {
        for (Constructor<?> constructor : cls.getDeclaredConstructors())
        {
            //If it is not constructor by default we try to create components in the constructor how parameters.
            if (constructor.getParameterTypes().length != 0)
            {                    
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                boolean allExists = true;
                for (Class<?> parameterType : parameterTypes)
                {
                    if(!context.exists(parameterType))
                    {
                        allExists = false;
                        break;
                    }
                }
                if(allExists)
                {
                    return constructor;
                }
            }
        }
        return null;
    }

    private <T> Constructor<?> findDefaultConstructor(Class<T> cls)
    {
        for (Constructor<?> constructor : cls.getDeclaredConstructors())
        {
            if (constructor.getParameterTypes().length == 0)
            {
                return constructor;
            }
        }
        return null;
    }

    private Object[] findParameters(Constructor<?> constructor)
    {
        List<Object> instances = new LinkedList<>();
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (Class<?> parameterType : parameterTypes)
        {
            instances.add(context.find(parameterType));
        }
        return instances.toArray();
    }

    public void injectDependencies(Class cls, Object obj)
    {
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            Inject annotation = declaredField.getAnnotation(Inject.class);
            if(annotation == null)
            {
                continue;
            }
            injectDependency(cls, obj, declaredField);
        }

        Class supClass = cls.getSuperclass();
        if(supClass != null && supClass != Object.class)
        {
            injectDependencies(supClass, obj);
        }
    }
    
    public void injectDependency(Class cls, Object obj, Field field)
    {
        Class<?> service = field.getType();
        Object componentObj = null;
        if(service.isArray())
        {
            service = service.getComponentType();
            componentObj = context.findAll(service);
        }
        else
        {
            componentObj = context.find(service);
        }
        try
        {
            field.setAccessible(true);
            field.set(obj, componentObj);
        }
        catch(IllegalArgumentException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
