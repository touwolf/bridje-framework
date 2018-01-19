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
 * Represents a web session, this interface can be used to save and retrieve
 * data for the current session, all session data will be store as string and
 * will be identified by a another string.
 */
public interface WebSession
{
    /**
     * The identifier for this session.
     *
     * @return An string by witch this session can be identified.
     */
    String getId();

    /**
     * Finds the given data in this session.
     *
     * @param name The name of the data that must be found.
     * @return The value of the specified data if it exists or null if it does
     * not.
     */
    String find(String name);

    /**
     * Stores the given data into the specified name in this session. The data
     * store with this method can be retrieved with the find method later.
     *
     * @param name The name of the data to be store.
     * @param value The data to be store.
     */
    void save(String name, String value);
}
