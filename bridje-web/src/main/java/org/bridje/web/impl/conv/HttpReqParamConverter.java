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

package org.bridje.web.impl.conv;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.el.ElTypeConverter;
import org.bridje.http.HttpReqParam;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

@Component
@Priority(5000)
public class HttpReqParamConverter implements ElTypeConverter
{
    private static final Logger LOG = Logger.getLogger(HttpReqParamConverter.class.getName());

    @Override
    public <F, T> boolean canConvert(Class<F> from, Class<T> to)
    {
        Class<?> resClass = to;
        if(resClass.isArray())
        {
            resClass = to.getComponentType();
        }
        return HttpReqParam.class.isAssignableFrom(from)
                && (Number.class.isAssignableFrom(resClass) 
                    || String.class == resClass
                    || Date.class.isAssignableFrom(resClass)
                    || Temporal.class.isAssignableFrom(resClass)
                );
    }

    @Override
    public <T> T convert(Object value, Class<T> type)
    {
        HttpReqParam param = (HttpReqParam)value;
        
        Class<?> resClass = type;
        if(resClass.isArray())
        {
            resClass = type.getComponentType();
            String[] values = param.getAllValues();
            Object[] result = (Object[])Array.newInstance(resClass, values.length);
            for (int i = 0; i < values.length; i++)
            {
                result[i] = convertParam(values[i], resClass);
            }
            return (T)result;
        }
        else
        {
            return (T)convertParam(param.getValue(), resClass);
        }
    }

    public <T> T convertParam(String param, Class<T> resClass)
    {
        if(String.class.isAssignableFrom(resClass))
        {
            return (T)param;
        }

        if(Number.class.isAssignableFrom(resClass))
        {
            try
            {
                Method method = resClass.getMethod("fromString", String.class);
                return (T)method.invoke(null, param);
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        if(Date.class.isAssignableFrom(resClass))
        {
        }

        if(Temporal.class.isAssignableFrom(resClass))
        {
            try
            {
                Method method = resClass.getMethod("parse", String.class);
                return (T)method.invoke(null, param);
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        
        return null;
    }
    
}
