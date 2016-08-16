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

package org.bridje.el.impl;

import de.odysseus.el.misc.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.el.ELException;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.el.ElTypeConverter;

@Component
class TypeConverterImpl implements TypeConverter
{
    @Inject
    private ElTypeConverter[] typeConverter;
    
    private Map<Class,Map<Class,ElTypeConverter>> convertMap;
    
    @PostConstruct
    public void init()
    {
        convertMap = new HashMap<>();
        for (ElTypeConverter converter : typeConverter)
        {
            Class[] toClasses = converter.getToClasses();
            Class[] fromClasses = converter.getFromClasses();
            for (Class fromClasse : fromClasses)
            {
                Map<Class, ElTypeConverter> map = convertMap.get(fromClasse);
                if(map == null)
                {
                    map = new HashMap<>();
                    convertMap.put(fromClasse, map);
                }
                for (Class toClazz : toClasses)
                {
                    map.put(toClazz, converter);
                }
            }
        }
    }
    
    @Override
    public <T> T convert(Object value, Class<T> type) throws ELException
    {
        if(value == null)
        {
            return null;
        }
        
        if(type.getClass() == value.getClass())
        {
            return (T)value;
        }

        if(type.getClass().isAssignableFrom(value.getClass()))
        {
            return (T)value;
        }

        Map<Class, ElTypeConverter> map = convertMap.get(value.getClass());
        if(map != null)
        {
            ElTypeConverter converter = map.get(type);
            if(converter != null)
            {
                if(converter.canConvert(value, type))
                {
                    return converter.convert(value, type);
                }
            }
        }

        return TypeConverter.DEFAULT.convert(value, type);
    }
    
}
