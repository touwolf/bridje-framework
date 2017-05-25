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

package org.bridje.web.impl.convert;

import de.odysseus.el.misc.TypeConverter;
import java.lang.reflect.Array;
import org.bridje.el.ElAdvanceConverter;
import org.bridje.http.HttpReqParam;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Priority;

/**
 * An EL type converter for HttpReqParam.
 */
@Component
@Priority(3000)
class HttpReqParamConverter implements ElAdvanceConverter
{
    @Inject
    private TypeConverter conv;
    
    @Override
    public <F, T> boolean canConvert(Class<F> from, Class<T> to)
    {
        return HttpReqParam.class.isAssignableFrom(from);
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
                result[i] = doConvert(values[i], resClass);
            }
            return (T)result;
        }
        else
        {
            return (T)doConvert(param.getValue(), resClass);
        }
    }

    private Object doConvert(String value, Class<?> resClass)
    {
        if(Enum.class.isAssignableFrom(resClass))
        {
            return Enum.valueOf((Class<Enum>)resClass, value);
        }
        if(Boolean.class.isAssignableFrom(resClass) 
                || boolean.class.isAssignableFrom(resClass))
        {
            return "true".equalsIgnoreCase(value) || 
                    "on".equalsIgnoreCase(value) || 
                    "1".equalsIgnoreCase(value);
        }
        return conv.convert(value, resClass);
    }
}
