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
 * A cast service for converting values to specific classes.
 */
public interface CastService
{
    /**
     * Cast a value to the specified class.
     * <p>
     * @param <T> The type of the target class.
     * @param object The object to be converted.
     * @param cls The target class.
     * @return The converted value or {@literal null} if not conversion can be made.
     */
    <T> T cast(Object object, Class<T> cls);
}
