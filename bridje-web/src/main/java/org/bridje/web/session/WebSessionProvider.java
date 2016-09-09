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

package org.bridje.web.session;

/**
 * This interface represents a web session provider, by implementing this
 * interface you can define the way in witch data will be stored and loaded from
 * the web sessions.
 */
public interface WebSessionProvider
{
    /**
     * Finds the given data in specified web session.
     *
     * @param sessionId The id of the web session.
     * @param name The name of the data that must be found.
     * @return The value of the specified data if it exists or null if it does
     * not.
     */
    String find(String sessionId, String name);

    /**
     * Stores the given data into the specified name for the given web session.
     * The data store with this method can be retreived with the find method
     * later.
     *
     * @param sessionId The id of the web session.
     * @param name The name of the data to be store.
     * @param value The data to be store.
     */
    void save(String sessionId, String name, String value);
}
