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

import java.lang.reflect.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.ContextListener;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;

class Instanciator
{
    private static final Logger LOG = Logger.getLogger(Instanciator.class.getName());

    private final ContextImpl<?> context;

    private final ServiceMap serviceMap;

    private ContextListener[] contextListeners;

    public Instanciator(ContextImpl context, ServiceMap serviceMap)
    {
        this.context = context;
        this.serviceMap = serviceMap;
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T> T instantiate(Class<T> cls)
    {
        try
        {
            Constructor<?> constructor = context.findCache(cls).getConstructor();
            if (constructor == null)
            {
                return null;
            }
            return (T) constructor.newInstance();
        }
        catch (InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex)
        {
            LOG.warning(ex.getMessage());
        }
        return null;
    }

    public void callPostConstruct(Class cls, Object obj)
    {
        Class currentClass = cls;
        while (!currentClass.equals(Object.class))
        {
            List<Method> methods = context.findCache(currentClass).getPostConstructs();
            for (Method method : methods)
            {
                try
                {
                    method.invoke(obj);
                }
                catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    public void injectDependencies(Class cls, Object obj)
    {
        List<Field> fields = context.findCache(cls).getInjectFields();
        for (Field field : fields)
        {
            Inject annotation = field.getAnnotation(Inject.class);
            if (annotation != null)
            {
                injectDependency(cls, obj, field, null);
            }
            InjectNext annotationNext = field.getAnnotation(InjectNext.class);
            if (annotationNext != null)
            {
                injectDependency(cls, obj, field, ClassUtils.findPriority(cls));
            }
        }

        Class supClass = cls.getSuperclass();
        if (supClass != null && supClass != Object.class)
        {
            injectDependencies(supClass, obj);
        }
    }

    public void injectDependency(Class cls, Object obj, Field field, Integer priority)
    {
        try
        {
            Type service = field.getGenericType();
            Object componentObj;
            if (priority == null)
            {
                componentObj = context.findGeneric(service);
            }
            else
            {
                componentObj = context.findNextGeneric(service, priority);
            }

            field.set(obj, componentObj);
        }
        catch (IllegalArgumentException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void initContextListeners()
    {
        if (null == contextListeners)
        {
            contextListeners = context.findAll(ContextListener.class);
        }
    }

    protected <T> void invokePreCreateListener(Class<T> cls)
    {
        if (ContextListener.class.isAssignableFrom(cls))
        {
            return;
        }

        initContextListeners();
        if (null != contextListeners)
        {
            for (ContextListener contextListener : contextListeners)
            {
                //find the generic parameter type of ContextListener,
                //example ContexListener<Integer> -> type = java.lang.Integer
                Type type = findGenericType(contextListener.getClass());
                if (type.equals(Object.class))
                {
                    contextListener.preCreateComponent(cls);
                }
                else
                {
                    List<Type> services = serviceMap.getServices(cls);
                    if (services != null)
                    {
                        for (Type service : services)
                        {
                            if (type.equals(service))
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
        if (ContextListener.class.isAssignableFrom(cls))
        {
            return;
        }

        initContextListeners();
        if (null != contextListeners)
        {
            for (ContextListener contextListener : contextListeners)
            {
                //find the generic parameter type of ContextListener,
                //example ContexListener<Integer> -> type = java.lang.Integer
                Type type = findGenericType(contextListener.getClass());
                if (type.equals(Object.class))
                {
                    contextListener.preInitComponent(cls, instance);
                }
                else
                {
                    List<Type> services = serviceMap.getServices(cls);
                    if (services != null)
                    {
                        for (Type service : services)
                        {
                            if (type.equals(service))
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
        if (ContextListener.class.isAssignableFrom(cls))
        {
            return;
        }

        initContextListeners();
        if (null != contextListeners)
        {
            for (ContextListener contextListener : contextListeners)
            {
                //find the generic parameter type of ContextListener,
                //example ContexListener<Integer> -> type = java.lang.Integer
                Type type = findGenericType(contextListener.getClass());
                if (type.equals(Object.class))
                {
                    contextListener.postInitComponent(cls, instance);
                }
                else
                {
                    List<Type> services = serviceMap.getServices(cls);
                    if (services != null)
                    {
                        for (Type service : services)
                        {
                            if (type.equals(service))
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
        Type[] ifcs = clazz.getGenericInterfaces();
        for (Type ifc : ifcs)
        {
            if (ifc instanceof ParameterizedType
                    && ClassUtils.rawClass(ifc).equals(ContextListener.class))
            {
                return (((ParameterizedType) ifc).getActualTypeArguments())[0];
            }
        }
        return Object.class;
    }

}
