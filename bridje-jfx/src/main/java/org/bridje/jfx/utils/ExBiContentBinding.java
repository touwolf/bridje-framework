/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.jfx.utils;

import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;

/**
 *
 * @author gilbe
 */
public class ExBiContentBinding
{
    private static void checkParameters(Object property1, Object property2)
    {
        if ((property1 == null) || (property2 == null))
        {
            throw new NullPointerException("Both parameters must be specified.");
        }
        if (property1 == property2)
        {
            throw new IllegalArgumentException("Cannot bind object to itself");
        }
    }

    public static <E, T> Object bind(ObservableList<E> list1, ObservableList<T> list2, BiContentConverter<E, T> converter)
    {
        checkParameters(list1, list2);
        final BiListContentBinding<E, T> binding = new BiListContentBinding<>(list1, list2, converter);
        list1.setAll(convertTo(list2, converter));
        list1.addListener(binding);
        list2.addListener(binding);
        return binding;
    }

    public static void unbind(Object obj1, Object obj2)
    {
        checkParameters(obj1, obj2);
        if ((obj1 instanceof ObservableList) && (obj2 instanceof ObservableList))
        {
            final ObservableList list1 = (ObservableList) obj1;
            final ObservableList list2 = (ObservableList) obj2;
            final BiListContentBinding binding = new BiListContentBinding(list1, list2, null);
            list1.removeListener(binding);
            list2.removeListener(binding);
        }
        /*
        else if ((obj1 instanceof ObservableSet) && (obj2 instanceof ObservableSet))
        {
            final ObservableSet set1 = (ObservableSet) obj1;
            final ObservableSet set2 = (ObservableSet) obj2;
            final SetContentBinding binding = new SetContentBinding(set1, set2);
            set1.removeListener(binding);
            set2.removeListener(binding);
        }
        else if ((obj1 instanceof ObservableMap) && (obj2 instanceof ObservableMap))
        {
            final ObservableMap map1 = (ObservableMap) obj1;
            final ObservableMap map2 = (ObservableMap) obj2;
            final MapContentBinding binding = new MapContentBinding(map1, map2);
            map1.removeListener(binding);
            map2.removeListener(binding);
        }
        */
    }


    public static <E, T> List<T> convertFrom(List<E> subList, BiContentConverter<E, T> converter)
    {
        return subList.stream()
                        .map(e -> converter.convertFrom(e))
                        .collect(Collectors.toList());
    }

    public static <E, T> List<E> convertTo(List<T> subList, BiContentConverter<E, T> converter)
    {
        return subList.stream()
                        .map(e -> converter.convertTo(e))
                        .collect(Collectors.toList());
    }
}
