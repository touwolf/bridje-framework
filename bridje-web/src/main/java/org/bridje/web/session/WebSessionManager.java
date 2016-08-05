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

package org.bridje.web.session;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.ioc.IocContext;
import org.bridje.web.WebRequestScope;

@Component
class WebSessionManager
{
    private static final Logger LOG = Logger.getLogger(WebSessionManager.class.getName());

    private Map<Class<?>, Map<Field, String>> sessionFields;

    public String findSessionFieldName(Class<Object> clazz, Field field)
    {
        return field.getAnnotation(WebSessionField.class).value();
    }
    
    public Set<Field> injectValue(IocContext<WebRequestScope> ctx, Class<?> clazz, Object instance, WebSession session)
    {
        if(sessionFields == null)
        {
            initSessionFields(ctx);
        }
        Map<Field, String> lst = sessionFields.get(clazz);
        if(lst != null)
        {
            Set<Field> result = new HashSet<>();
            lst.forEach((field, sessName) ->
            {
                try
                {
                    String value = session.find(sessName);
                    result.add(field);
                    if(value != null)
                    {
                        field.set(instance, unserialize(value, field));
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            });
            return result;
        }
        return null;
    }
    
    public Set<Field> storeValue(IocContext<WebRequestScope> ctx, Class<?> clazz, Object instance, WebSession session)
    {
        if(sessionFields == null)
        {
            initSessionFields(ctx);
        }
        Set<Field> result = new HashSet<>();
        Map<Field, String> lst = sessionFields.get(clazz);
        if(lst != null)
        {
            lst.forEach((field, sessName) ->
            {
                try
                {
                    Object value = field.get(instance);
                    session.save(sessName, serialize(value));
                    result.add(field);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            });
        }
        return result;
    }

    private synchronized void initSessionFields(IocContext<WebRequestScope> ctx)
    {
        if(sessionFields == null)
        {
            Map<Class<?>, Map<Field, String>> result = new HashMap<>();
            ctx.getClassRepository().forEachField(WebSessionField.class, (field, component, annot) ->
            {
                Map<Field, String> lst = result.get(component);
                if(lst == null)
                {
                    lst = new HashMap<>();
                    result.put(component, lst);
                }
                field.setAccessible(true);
                String name = findSessionFieldName(component, field);
                lst.put(field, name);
            });
            sessionFields = result;
        }
    }

    private String serialize(Object value)
    {
        if(value == null)
        {
            return null;
        }
        return value.toString();
    }

    private Object unserialize(String value, Field field)
    {
        if(value == null)
        {
            return null;
        }
        if(field.getType().equals(String.class))
        {
            return value;
        }
        try
        {
            Method method = field.getType().getMethod("valueOf", String.class);
            if(method != null)
            {
                return method.invoke(null, value);
            }
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}
