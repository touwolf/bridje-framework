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

package org.bridje.core.web;

import java.io.OutputStream;

/**
 * An object witch holds the data of the current request's response.
 */
public interface WebResponse
{
    /**
     * The output stream for the current request.
     *
     * @return An OutputStream object to outputs the result of the current
     * request.
     */
    public OutputStream getOutputStream();

    /**
     * Mark the response as processed, this method should be called only onece
     * per request, if no handler calls this method the web api will return a
     * 404 not found to the client.
     */
    public void processed();

    /**
     *
     * @return
     */
    public boolean isProcessed();

    /**
     * 
     * @param code 
     */
    public void setStatusCode(int code);
}
