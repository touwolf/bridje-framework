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

package org.bridje.el;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a map of simple converters that can be use to add/get the correct
 * converter for the source and the destiny class of the convertion.
 */
public class ElSimpleConvertMap
{
    private final Map<Class<?>, Map<Class<?>, ElSimpleConverter<?, ?>>> map;

    /**
     * Default constructor.
     */
    public ElSimpleConvertMap()
    {
        map = new HashMap<>();
    }

    /**
     * Gets the correct converter (if it exists) by the given source and destiny
     * clases for the convertion.
     *
     * @param <F>  The type of the from class.
     * @param <T>  The type of the to class.
     * @param from The source class for the convertion.
     * @param to   The destiny class for the convertion.
     *
     * @return The simple converter finded or null if it does not exists.
     */
    public <F, T> ElSimpleConverter<F, T> get(Class<F> from, Class<T> to)
    {
        Map<Class<?>, ElSimpleConverter<?, ?>> toMap = map.get(from);
        if (toMap != null)
        {
            return (ElSimpleConverter<F, T>) toMap.get(to);
        }
        return null;
    }

    /**
     * Specifies the correct converter by the given source and destiny clases
     * for the convertion.
     *
     * @param <F>  The type of the from class.
     * @param <T>  The type of the to class.
     * @param from The source class for the convertion.
     * @param to   The destiny class for the convertion.
     * @param conv
     */
    public <F, T> void add(Class<F> from, Class<T> to, ElSimpleConverter<F, T> conv)
    {
        addInternal(from, to, conv);
    }

    /**
     * Adds all the converters defined in the given simple converter map to this
     * object.
     *
     * @param convMap The converter map whos children will be added to this
     *                converter.
     */
    public void addAll(ElSimpleConvertMap convMap)
    {
        convMap.map.forEach((from, toMap) ->
        {
            toMap.forEach((to, conv) ->
            {
                addInternal(from, to, conv);
            });
        });
    }

    private void addInternal(Class<?> from, Class<?> to, ElSimpleConverter<?, ?> conv)
    {
        Map<Class<?>, ElSimpleConverter<?, ?>> toMap = map.get(from);
        if (toMap == null)
        {
            toMap = new HashMap<>();
            map.put(from, toMap);
        }
        toMap.put(to, conv);
    }

}
