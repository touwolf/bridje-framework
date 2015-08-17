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
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.IocContext;

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
                        if(!context.existsComponent(parameterType))
                        {
                            parameters = new LinkedList();
                            continue;
                        }
                        
                        parameters.add(context.find(parameterType));
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
