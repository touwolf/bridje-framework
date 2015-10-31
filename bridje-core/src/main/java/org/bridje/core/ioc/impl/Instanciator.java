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

package org.bridje.core.ioc.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.core.ioc.ContextListener;
import org.bridje.core.ioc.annotations.Inject;
import org.bridje.core.ioc.IocContext;
import org.bridje.core.ioc.annotations.Construct;
import org.bridje.core.ioc.annotations.InjectNext;
import org.bridje.core.ioc.annotations.PostConstruct;

class Instanciator
{
    private static final Logger LOG = Logger.getLogger(Instanciator.class.getName());

    private final IocContext context;
    
    private final ServiceMap serviceMap;

    private ContextListener[] contextListeners;
    
    public Instanciator(IocContext context, ServiceMap serviceMap)
    {
        this.context = context;
        this.serviceMap = serviceMap;
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T> T instantiate(Class<T> cls)
    {
        try
        {
            Constructor<?> constructor = findConstructor(cls);
            if(constructor == null)
            {
                return null;
            }
            Object[] parameters = findParameters(constructor);
            return parameters.length == 0 ? (T)constructor.newInstance() : (T)constructor.newInstance(parameters);
        }
        catch (InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex)
        {
            LOG.warning(ex.getMessage());
        }
        return null;
    }
    
    public void callPostConstruct(Class cls, Object obj)
    {
        Method[] declaredMethods = cls.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods)
        {
            PostConstruct annotation = declaredMethod.getAnnotation(PostConstruct.class);
            if(annotation != null)
            {
                try
                {
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(obj);
                }
                catch(Exception ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    public void injectDependencies(Class cls, Object obj)
    {
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            Inject annotation = declaredField.getAnnotation(Inject.class);
            if(annotation != null)
            {
                injectDependency(cls, obj, declaredField, null);
            }
            InjectNext annotationNext = declaredField.getAnnotation(InjectNext.class);
            if(annotationNext != null)
            {
                injectDependency(cls, obj, declaredField, ClassUtils.findPriority(cls));
            }
        }

        Class supClass = cls.getSuperclass();
        if(supClass != null && supClass != Object.class)
        {
            injectDependencies(supClass, obj);
        }
    }
    
    public void injectDependency(Class cls, Object obj, Field field, Integer priority)
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
            if(priority == null)
            {
                componentObj = context.findGeneric(service, serviceCls);
            }
            else
            {
                componentObj = context.findNextGeneric(service, serviceCls, ClassUtils.findPriority(cls));
            }
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
    
    private <T> Constructor<?> findConstructor(Class<T> cls)
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
        return constructor;
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
            //If it is not the default constructor we find out if we can create de component by injecting all his parameters.
            if (constructor.getParameterTypes().length != 0)
            {
                if(allParametersExists(constructor.getParameters()))
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

    private boolean allParametersExists(Parameter[] parameters)
    {
        boolean allExists = true;
        for (Parameter parameter : parameters)
        {
            Type paramType = ClassUtils.findParameterType(parameter);
            if(!context.exists(paramType))
            {
                allExists = false;
                break;
            }
        }
        return allExists;
    }

    private void initContextListeners()
    {
        if(null == contextListeners)
        {
            contextListeners = context.findAll(ContextListener.class);
        }
    }

    protected <T> void invokePreCreateListener(Class<T> cls)
    {
        if(ContextListener.class.isAssignableFrom(cls))
        {
            return;
        }
       
        initContextListeners();
        if(null != contextListeners)
        {
            for (ContextListener contextListener : contextListeners)
            {
                //find the generic parameter type of ContextListener, 
                //example ContexListener<Integer> -> type = java.lang.Integer
                Type type = findGenericType(contextListener.getClass());
                if(type.equals(Object.class))
                {
                    contextListener.preCreateComponent(cls);
                }
                else
                {
                    List<Type> services = serviceMap.getServices(cls);
                    if(services != null)
                    {
                        for (Type service : services)
                        {
                            if(type.equals(service))
                            {
                                contextListener.preCreateComponent(cls);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public <T> void invokePreInitListener(Class<T> cls, Object instance)
    {
        if(ContextListener.class.isAssignableFrom(cls))
        {
            return;
        }
        
        initContextListeners();
        if(null != contextListeners)
        {
            for (ContextListener contextListener : contextListeners)
            {
                //find the generic parameter type of ContextListener, 
                //example ContexListener<Integer> -> type = java.lang.Integer
                Type type = findGenericType(contextListener.getClass());
                if(type.equals(Object.class))
                {
                    contextListener.preInitComponent(cls, instance);
                }
                else
                {
                    List<Type> services = serviceMap.getServices(cls);
                    if(services != null)
                    {
                        for (Type service : services)
                        {
                            if(type.equals(service))
                            {
                                contextListener.preInitComponent(cls, instance);
                                break;
                            }
                        }
                    }
                }      
            }
        }
    }
    
    public <T> void invokePostInitListener(Class<T> cls, Object instance)
    {
        if(ContextListener.class.isAssignableFrom(cls))
        {
            return;
        }
        
        initContextListeners();
        if(null != contextListeners)
        {
            for (ContextListener contextListener : contextListeners)
            {
                //find the generic parameter type of ContextListener, 
                //example ContexListener<Integer> -> type = java.lang.Integer
                Type type = findGenericType(contextListener.getClass());
                if(type.equals(Object.class))
                {
                    contextListener.postInitComponent(cls, instance);
                }
                else
                {
                    List<Type> services = serviceMap.getServices(cls);
                    if(services != null)
                    {
                        for (Type service : services)
                        {
                            if(type.equals(service))
                            {
                                contextListener.postInitComponent(cls, instance);
                                break;
                            }
                        }
                    }
                }      
            }
        }
    }

    private Type findGenericType(Class<? extends ContextListener> clazz)
    {
        Type type = (((ParameterizedType)(clazz
                        .getGenericInterfaces())[0])
                        .getActualTypeArguments())[0];
        return type;
    }
}
