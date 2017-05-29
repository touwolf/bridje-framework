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

package org.bridje.http;

/**
 * Represents a context used in a HTTP request by the server handlers. This
 * interface allows to set and get data that is specific to the request being
 * made to the server. handlers can share information using this interface.
 */
public interface HttpBridletContext
{
    /**
     * Gets an instance of the specified class if an instance of it was added
     * previously to this context.
     *
     * @param <T> The type of the class to search for.
     * @param cls The class to search for.
     *
     * @return The previously set instance of the class or null if none can be
     *         found.
     */
    <T> T get(Class<T> cls);

    /**
     * Add the data object to the context so it can be retrieve by the get
     * method.
     *
     * @param <T>  The type of the class of data.
     * @param cls  The class of the data.
     * @param data The instance to store in the context.
     */
    <T> void set(Class<T> cls, T data);

    /**
     * 
     * @return 
     */
    HttpBridletRequest getRequest();

    /**
     * 
     * @return 
     */
    HttpBridletResponse getResponse();
}
