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

package org.bridje.orm.adpaters;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.orm.Column;
import org.bridje.orm.SQLAdapter;

/**
 * An SQLAdapter that will find and call the methods toSQL/fromSQL by
 * reflection in order to serialize/unserialize the values to/from the database.
 */
@Component
public class GenericSQLAdapter implements SQLAdapter
{
    private static final Logger LOG = Logger.getLogger(GenericSQLAdapter.class.getName());

    private final Map<Class, Method> fromSQLMap = new ConcurrentHashMap<>();

    private final Map<Class, Method> toSQLMap = new ConcurrentHashMap<>();

    @Override
    public Object serialize(Object value, Column column)
    {
        try
        {
            Method method = getToSQLMethod(column);
            if(method == null)
            {
                LOG.log(Level.WARNING, "{0} does not have a valid ToSQL method.", column.getType());
            }
            else
            {
                return method.invoke(value);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex)
        {
            LOG.log(Level.WARNING, "{0} does not have a valid ToSQL method: {1}", new Object[] { column.getType(), ex.getMessage() });
        }
        return null;
    }

    @Override
    public Object unserialize(Object value, Column column)
    {
        try
        {
            Method method = getFromSQLMethod(column);
            if(method == null)
            {
                LOG.log(Level.WARNING, "{0} does not have a valid FromSQL method.", column.getType());
            }
            else
            {
                return method.invoke(null, value);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex)
        {
            LOG.log(Level.WARNING, "{0} does not have a valid FromSQL method: {1}", new Object[] { column.getType(), ex.getMessage() });
        }
        return null;
    }

    private Method getToSQLMethod(Column column)
    {
        Method method = toSQLMap.get(column.getType());
        if(method != null)
        {
            return method;
        }
        else
        {
            method = findToSQLMethod(column);
            if(method != null)
            {
                toSQLMap.put(column.getType(), method);
            }
            return method;
        }
    }

    private Method getFromSQLMethod(Column column)
    {
        Method method = fromSQLMap.get(column.getType());
        if(method != null)
        {
            return method;
        }
        else
        {
            method = findFromSQLMethod(column);
            if(method != null)
            {
                fromSQLMap.put(column.getType(), method);
            }
            return method;
        }
    }

    private Method findToSQLMethod(Column column)
    {
        return findMethod(column.getType(), ToSQL.class);
    }

    private Method findFromSQLMethod(Column column)
    {
        return findMethod(column.getType(), FromSQL.class);
    }

    private Method findMethod(Class<?> type, Class<? extends Annotation> anot)
    {
        for(Method m : type.getDeclaredMethods())
        {
            if(m.getAnnotation(anot) != null)
            {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }
}
