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

package org.bridje.jfx.binding;

import java.util.List;
import javafx.collections.ObservableList;

/**
 * Utility functions for binding of the content of collections. This functions extend the 
 * functionality provided by the javafx Bindings class.
 */
public class ExBindings
{
    /**
     * Binds the content of two lists in both directions.
     * 
     * @param <E> The type for the first list.
     * @param <T> The type for the second list.
     * @param list1 The first list.
     * @param list2 The second list.
     * @param converter The bidirectional converter.
     * @return The binding object.
     */
    public static <E, T> Object bindContentBidirectional(ObservableList<E> list1, ObservableList<T> list2, BiContentConverter<E, T> converter)
    {
        return ExBiContentBinding.bind(list1, list2, converter);
    }

    /**
     * Unbinds a bidirectional binding.
     * 
     * @param obj1 The first object.
     * @param obj2 The second object.
     */
    public static void unbindContentBidirectional(Object obj1, Object obj2)
    {
        ExBiContentBinding.unbind(obj1, obj2);
    }

    /**
     * Binds the content of two lists.
     * 
     * @param <E> The type for the first list.
     * @param <T> The type for the second list.
     * @param list1 The first list.
     * @param list2 The second list.
     * @param converter The converter.
     * @return The binding object.
     */
    public static <E, T> Object bindContent(List<T> list1, ObservableList<? extends E> list2, ContentConverter<E, T> converter)
    {
        return ExContentBinding.bind(list1, list2, converter);
    }

    /**
     * Unbinds two bindable objects.
     * 
     * @param obj1 The first object.
     * @param obj2 The second object.
     */
    public static void unbindContent(Object obj1, Object obj2)
    {
        ExContentBinding.unbind(obj1, obj2);
    }
}
