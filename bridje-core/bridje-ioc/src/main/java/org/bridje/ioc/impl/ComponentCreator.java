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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.annotations.Construct;

/**
 * 
 * @author gilberto
 */
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
            Constructor<?> constructor = findConstructorWithAnnotation(cls);
            if(constructor == null)
            {
                constructor = findConstructorWithParameters(cls);
                if(constructor == null)
                {
                    constructor = findDefaultConstructor(cls);
                }
            }
            if(constructor == null)
            {
                LOG.log(Level.WARNING, "Couldn't find a default constructor for {}", cls.getName());
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
    
    private Constructor<?> findConstructorWithAnnotation(Class cls)
    {
        for (Constructor<?> constructor : cls.getDeclaredConstructors())
        {
            Construct constructAnnot = constructor.getAnnotation(Construct.class);
            if (constructAnnot != null)
            {                    
                return constructor;
            }
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
                Parameter[] parameters = constructor.getParameters();
                boolean allExists = true;
                for (Parameter parameter : parameters)
                {
                    if(parameter.getType().isArray())
                    {
                        Type arrType = ClassUtils.findTypeFromArray(parameter.getParameterizedType());
                        Class resultCls = ClassUtils.findClassFromType(arrType);
                        if(context.findAllGeneric(arrType, resultCls) == null)
                        {
                            allExists = false;
                            break;
                        }
                    }
                    else
                    {
                        Class resultCls = ClassUtils.findClassFromType(parameter.getParameterizedType());
                        if(context.findGeneric(parameter.getParameterizedType(), resultCls) == null)
                        {
                            allExists = false;
                            break;
                        }
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
        Parameter[] parameters = constructor.getParameters();
        for (Parameter parameter : parameters)
        {
            if(parameter.getType().isArray())
            {
                Type arrType = ClassUtils.findTypeFromArray(parameter.getParameterizedType());
                Class resultCls = ClassUtils.findClassFromType(arrType);
                instances.add(context.findAllGeneric(arrType, resultCls));
            }
            else
            {
                Class resultCls = ClassUtils.findClassFromType(parameter.getParameterizedType());
                instances.add(context.findGeneric(parameter.getParameterizedType(), resultCls));
            }
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
        Type service = field.getGenericType();
        Object componentObj = null;
        Class serviceCls = ClassUtils.findClassFromType(service);
        if(service instanceof Class && serviceCls.isArray())
        {
            service = serviceCls.getComponentType();
            serviceCls = ClassUtils.findClassFromType(service);
            componentObj = context.findAllGeneric(service, serviceCls);
        }
        else
        {
            componentObj = context.findGeneric(service, serviceCls);
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
