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

package org.bridje.el.impl.convert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.el.ElAdvanceConverter;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

@Component
@Priority(5010)
class StringConverter implements ElAdvanceConverter
{
    private static final Logger LOG = Logger.getLogger(StringConverter.class.getName());

    @Override
    public <F, T> boolean canConvert(Class<F> from, Class<T> to)
    {
        return (from == String.class && hasFromStringMethod(to))
                || (to == String.class && hasFromStringMethod(from));
    }

    @Override
    public <T> T convert(Object value, Class<T> type)
    {
        if(type == String.class)
        {
            return (T)toString(value);
        }
        else
        {
            return fromString((String)value, type);
        }
    }

    private boolean hasFromStringMethod(Class<?> cls)
    {
        try
        {
            Method method = cls.getDeclaredMethod("fromString", String.class);
            return (method != null && method.getReturnType() == cls) && Modifier.isStatic(method.getModifiers());
        }
        catch (NoSuchMethodException | SecurityException ex)
        {
            return false;
        }
    }

    private String toString(Object value)
    {
        return value.toString();
    }

    private <T> T fromString(String value, Class<T> type)
    {
        try
        {
            Method method = type.getMethod("fromString", String.class);
            return (T)method.invoke(null, value);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return (T)null;
    }
}
