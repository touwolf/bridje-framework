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

package org.bridje.orm.impl;

import java.sql.SQLException;
import org.bridje.orm.Entity;

/**
 * Utilities for casting values from to the database.
 */
public class CastUtils
{
    /**
     * Cast the given object to the specified class.
     *
     * @param <F> The type to cast to.
     * @param fieldType The class of the type to cast to.
     * @param value The value to be converted.
     * @param ctx The current entity context.
     * @return The converted value or null if not viable convertion can be
     * found.
     * @throws java.sql.SQLException if a related entity cannot be found.
     */
    static <F> F castValue(Class<F> fieldType, Object value, EntityContextImpl ctx) throws SQLException
    {
        if (value != null)
        {
            if (!fieldType.isAssignableFrom(value.getClass()))
            {
                if (Character.class.isAssignableFrom(fieldType))
                {
                    return (F) toCharacter(value);
                }
                if (Byte.class.isAssignableFrom(fieldType))
                {
                    return (F) toByte(value);
                }
                if (Short.class.isAssignableFrom(fieldType))
                {
                    return (F) toShort(value);
                }
                if (Integer.class.isAssignableFrom(fieldType))
                {
                    return (F) toInteger(value);
                }
                if (Long.class.isAssignableFrom(fieldType))
                {
                    return (F) toLong(value);
                }
                if (Float.class.isAssignableFrom(fieldType))
                {
                    return (F) toFloat(value);
                }
                if (Double.class.isAssignableFrom(fieldType))
                {
                    return (F) toDouble(value);
                }
                if(fieldType.getAnnotation(Entity.class) != null)
                {
                    TableImpl<F> tableImpl = (TableImpl<F>)ctx.findTable(fieldType);
                    return ctx.find(fieldType, tableImpl.getKey().unserialize(value) );
                }
            }
            return (F) value;
        }
        return null;
    }

    public static <F> F castValue(Class<F> fieldType, Object value)
    {
        if (value != null)
        {
            if (!fieldType.isAssignableFrom(value.getClass()))
            {
                if (Character.class.isAssignableFrom(fieldType))
                {
                    return (F) toCharacter(value);
                }
                if (Byte.class.isAssignableFrom(fieldType))
                {
                    return (F) toByte(value);
                }
                if (Short.class.isAssignableFrom(fieldType))
                {
                    return (F) toShort(value);
                }
                if (Integer.class.isAssignableFrom(fieldType))
                {
                    return (F) toInteger(value);
                }
                if (Long.class.isAssignableFrom(fieldType))
                {
                    return (F) toLong(value);
                }
                if (Float.class.isAssignableFrom(fieldType))
                {
                    return (F) toFloat(value);
                }
            }
            return (F) value;
        }
        return null;        
    }

    private static Character toCharacter(Object value)
    {
        if (value instanceof String && !((String) value).isEmpty())
        {
            return ((String) value).toCharArray()[0];
        }
        return null;
    }

    private static Object toByte(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number) value).byteValue();
        }
        if (value instanceof String && !((String) value).isEmpty())
        {
            return Byte.valueOf((String) value);
        }
        return null;
    }

    private static Object toShort(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number) value).shortValue();
        }
        if (value instanceof String && !((String) value).isEmpty())
        {
            return Short.valueOf((String) value);
        }
        return null;
    }

    private static Object toLong(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        if (value instanceof String && !((String) value).isEmpty())
        {
            return Long.valueOf((String) value);
        }
        return null;
    }

    private static Object toInteger(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number) value).intValue();
        }
        if (value instanceof String && !((String) value).isEmpty())
        {
            return Integer.valueOf((String) value);
        }
        return null;
    }

    private static Object toFloat(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number) value).floatValue();
        }
        if (value instanceof String && !((String) value).isEmpty())
        {
            return Float.valueOf((String) value);
        }
        return null;
    }
    
    private static Object toDouble(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number) value).floatValue();
        }
        if (value instanceof String && !((String) value).isEmpty())
        {
            return Double.valueOf((String) value);
        }
        return null;
    }
}
