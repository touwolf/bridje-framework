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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.el.ElAdvanceConverter;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

@Component
@Priority(5015)
class DoubleConverter implements ElAdvanceConverter
{
    private static final Logger LOG = Logger.getLogger(DoubleConverter.class.getName());

    private static final DecimalFormat FORMAT;

    static
    {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        FORMAT = new DecimalFormat();
        FORMAT.setDecimalFormatSymbols(formatSymbols);
    }

    @Override
    public <F, T> boolean canConvert(Class<F> from, Class<T> to)
    {
        return from == String.class && hasFromDoubleMethod(to);
    }

    @Override
    public <T> T convert(Object value, Class<T> type)
    {
        if (type == Double.class)
        {
            return type.cast(toDouble(value));
        }
        return null;
    }

    private boolean hasFromDoubleMethod(Class<?> cls)
    {
        if (Double.class.isAssignableFrom(cls))
            return true;
        try
        {
            Method method = cls.getDeclaredMethod("fromDouble", Double.class);
            return (method != null && method.getReturnType() == cls) && Modifier.isStatic(method.getModifiers());
        }
        catch (NoSuchMethodException | SecurityException ex)
        {
            return false;
        }
    }

    private Double toDouble(Object value)
    {
        if (value != null)
            try
            {
                String string = value.toString().trim();
                if (!string.isEmpty())
                {
                    int lastComma = string.lastIndexOf(",");
                    if (lastComma > 0)
                        string = value.toString().substring(0, lastComma) + "." + value.toString().substring(lastComma + 1);
                    Number number = FORMAT.parse(string);
                    return number.doubleValue();
                }
            }
            catch (Exception ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        return null;
    }
}
