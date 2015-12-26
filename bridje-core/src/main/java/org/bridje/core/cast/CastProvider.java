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

package org.bridje.core.cast;

/**
 * Provider to cast any object to a specific class.
 *
 * @param <T> The handled type by the provider.
 */
public interface CastProvider<T>
{
    /**
     * Obtain the input types that can handle.
     *
     * @return The array of allowed input types.
     */
    Class<?>[] getSrcClasses();

    /**
     * Obtain the destiny class of casting.
     *
     * @return The destiny class of casting.
     */
    Class<T> getDestClass();

    /**
     * Cast an object.
     *
     * @param object The object to be converted.
     * @return The converted value or {@literal null} if not conversion can be made or
     * cannot be handled.
     */
    T cast(Object object);
}
