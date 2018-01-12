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

import java.io.OutputStream;

/**
 * Represents the response made by the server to the client in the HTTP request.
 * This object allows you to control the response of the HTTP request.
 */
public interface HttpBridletResponse
{
    /**
     * The OutputStream to print the response body.
     *
     * @return The OutputStream to print the response body.
     */
    OutputStream getOutputStream();

    /**
     * The content mime type of the response.
     *
     * @return The content mime type of the response.
     */
    String getContentType();

    /**
     * The content mime type of the response.
     *
     * @param contentType The content mime type of the response.
     */
    void setContentType(String contentType);

    /**
     * The status code of the response.
     *
     * @return The status code of the response.
     */
    int getStatusCode();

    /**
     * The status code of the response.
     *
     * @param statusCode The status code of the response.
     */
    void setStatusCode(int statusCode);

    /**
     * Sets a header for the response.
     *
     * @param name  The header name
     * @param value The header value
     */
    void setHeader(String name, Object value);

    /**
     * Adds a new HttpCookie to the response of this request.
     *
     * @param name  The name of the cookie.
     * @param value The value for the cookie.
     *
     * @return The new created HttpCookie object.
     */
    HttpCookie addCookie(String name, String value);
}
