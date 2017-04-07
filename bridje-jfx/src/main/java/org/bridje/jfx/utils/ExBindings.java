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

import com.sun.javafx.binding.BidirectionalContentBinding;
import javafx.collections.ObservableList;

/**
 *
 * @author gilbe
 */
public class ExBindings
{
    public static <E, T> void bindContentBidirectional(ObservableList<E> list1, ObservableList<T> list2, BiContentConverter<E, T> converter)
    {
        ExBiContentBinding.bind(list1, list2, converter);
    }

    public static void unbindContentBidirectional(Object obj1, Object obj2)
    {
        ExBiContentBinding.unbind(obj1, obj2);
    }
}
