/*
 * Copyright 2016 Bridje Framework.
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;

class ClassCache
{
    private final List<Field> injectFields;
    
    private final Constructor constructor;
    
    private final List<Method> postConstructs;

    public ClassCache(Class<?> cls)
    {
        injectFields = createInjectFields(cls);
        constructor = findConstructor(cls);
        postConstructs = findPostConstructs(cls);
    }

    public List<Field> getInjectFields()
    {
        return injectFields;
    }

    public Constructor getConstructor()
    {
        return constructor;
    }

    public List<Method> getPostConstructs()
    {
        return postConstructs;
    }

    private List<Field> createInjectFields(Class<?> cls)
    {
        List<Field> result = new ArrayList<>();
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields)
        {
            field.setAccessible(true);
            Inject annotation = field.getAnnotation(Inject.class);
            InjectNext annotationNext = field.getAnnotation(InjectNext.class);
            if(annotation != null)
            {
                result.add(field);
            }
            else if(annotationNext != null)
            {
                result.add(field);
            }
        }
        return result;
    }

    private Constructor findConstructor(Class<?> cls)
    {
        for (Constructor<?> constructor : cls.getDeclaredConstructors())
        {
            if (constructor.getParameterTypes().length == 0)
            {
                constructor.setAccessible(true);
                return constructor;
            }
        }
        return null;
    }

    private List<Method> findPostConstructs(Class<?> cls)
    {
        List<Method> result = new ArrayList<>();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods)
        {
            PostConstruct annotation = method.getAnnotation(PostConstruct.class);
            if(annotation != null)
            {
                method.setAccessible(true);
                result.add(method);
            }
        }
        return result;
    }
}
