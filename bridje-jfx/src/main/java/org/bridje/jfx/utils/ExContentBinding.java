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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;

/**
 *
 * @author gilbe
 */
public class ExContentBinding
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

    public static <E, T> Object bind(List<T> list1, ObservableList<? extends E> list2, ContentConverter<E, T> converter)
    {
        ExContentBinding.checkParameters(list1, list2);
        final ListContentBinding<E, T> contentBinding = new ListContentBinding<>(list1, converter);
        if (list1 instanceof ObservableList)
        {
            ((ObservableList) list1).setAll(list2);
        }
        else
        {
            list1.clear();
            list1.addAll(convert(list2, converter));
        }
        list2.removeListener(contentBinding);
        list2.addListener(contentBinding);
        return contentBinding;
    }

    public static void unbind(Object obj1, Object obj2)
    {
        checkParameters(obj1, obj2);
        if ((obj1 instanceof List) && (obj2 instanceof ObservableList))
        {
            ((ObservableList) obj2).removeListener(new ListContentBinding((List) obj1, null));
        }
        /*
        else if ((obj1 instanceof Set) && (obj2 instanceof ObservableSet))
        {
            ((ObservableSet) obj2).removeListener(new SetContentBinding((Set) obj1, null));
        }
        else if ((obj1 instanceof Map) && (obj2 instanceof ObservableMap))
        {
            ((ObservableMap) obj2).removeListener(new MapContentBinding((Map) obj1, null));
        }*/
    }

    private static <E, T> Collection<? extends T> convert(List<? extends E> subList, ContentConverter<E, T> converter)
    {
        return subList.stream()
                        .map(e -> converter.convert(e))
                        .collect(Collectors.toList());
    }
}
