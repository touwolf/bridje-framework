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

/**
 * A bidirectional converter from/to two different types, for the content of two collections.
 * 
 * @param <E> The first type.
 * @param <T> The second type.
 */
public interface BiContentConverter<E, T>
{
    /**
     * Converts from the first type to the second.
     * 
     * @param value The value to convert.
     * @return The new converted value.
     */
    T convertFrom(E value);
    
    /**
     * Converts from the second type to the first.
     * 
     * @param value The value to convert.
     * @return The new converted value.
     */
    E convertTo(T value);
}
