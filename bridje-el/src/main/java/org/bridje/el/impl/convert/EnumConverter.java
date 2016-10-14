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

import org.bridje.el.ElTypeConverter;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

@Component
@Priority(5020)
class EnumConverter implements ElTypeConverter
{
    @Override
    public <F, T> boolean canConvert(Class<F> from, Class<T> to)
    {
        return (from == String.class && isEnum(to))
                || (to == String.class && isEnum(from))
                || (isOrdinal(from) && isEnum(to))
                || (isOrdinal(to) && isEnum(from));

    }

    @Override
    public <T> T convert(Object value, Class<T> type)
    {
        if(type == String.class)
        {
            return (T)toString((Enum)value);
        }
        else if(isOrdinal(type))
        {
            return (T)toOrdinal((Enum)value);
        }
        else if(isEnum(type))
        {
            if(value instanceof String)
            {
                return (T)fromString((String)value, (Class<Enum>)type);
            }
            else
            {
                return (T)fromOrdinal((Integer)value, (Class<Enum>)type);
            }
        }
        return null;
    }

    private String toString(Enum enumValue)
    {
        return enumValue.name();
    }

    private Enum fromString(String string, Class<Enum> enumCls)
    {
        return Enum.valueOf(enumCls, string);
    }

    private Integer toOrdinal(Enum value)
    {
        return value.ordinal();
    }

    private Enum fromOrdinal(Integer ordinal, Class<Enum> enumCls)
    {
        Enum[] enumConst = enumCls.getEnumConstants();
        if(ordinal >= 0 && ordinal < enumConst.length)
        {
            return enumConst[ordinal];
        }
        return null;
    }

    private <T> boolean isEnum(Class<T> cls)
    {
        return cls.isEnum();
    }

    private <F> boolean isOrdinal(Class<F> cls)
    {
        return cls == Integer.class
                || cls == int.class;
    }
}
