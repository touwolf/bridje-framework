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

import java.io.IOException;

/**
 * Represents a handler of HTTP request, this interface must be implemented by
 * components that will be call in chain to handle the request. Handlers must
 * provide a Priority greater than 0 and it is recommended that it is used with
 * the @InjectNext annotation to create a chain of handlers that can handler
 * properly all HTTP request for the application.
 */
public interface HttpBridlet
{
    /**
     * Handles the HTTP request, This method must call the same method from the
     * next handler if it exists an return it's result or it can handle the
     * request it self and return true in that case.
     *
     * @param context The data context for this request.
     * @return true the request was handler withing this call, false the request
     * was not handled by any handler whiting this call.
     * @throws IOException If any IOException occurs during this call.
     */
    boolean handle(HttpBridletContext context) throws IOException;
}
