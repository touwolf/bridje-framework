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

import java.io.InputStream;
import java.util.Map;

/**
 * Represents an HTTP request being made to the server.
 */
public interface HttpServerRequest
{
    /**
     * The HTTP method used to made the request.
     * @return An String representing the HTTP method used to made the request.
     */
    String getMethod();

    /**
     * The protocol used to made the request
     * @return An String representing the protocol used to made the request
     */
    String getProtocol();

    /**
     * The host of the server the client made the HTTP request to
     * @return An String representing the host name of the server.
     */
    String getHost();

    /**
     * The UserAgent heather from the http request if any.
     * @return An String representing the UserAgent information from the client if is available.
     */
    String getUserAgent();

    /**
     * The Accept header sended by the client.
     * @return An String representing the value of the Accept header
     */
    String getAccept();

    /**
     * The AcceptLanguaje header sended by the client.
     * @return An String representing the value of the AcceptLanguaje  header
     */
    String getAcceptLanguage();

    /**
     * The requested path asked by the client.
     * @return An String representing the requested path asked by the client.
     */
    String getPath();

    /**
     * 
     * @return 
     */
    String getConentType();

    /**
     * 
     * @return 
     */
    boolean isGet();

    /**
     * 
     * @return 
     */
    boolean isPost();

    /**
     * 
     * @return 
     */
    boolean isDelete();

    /**
     * 
     * @return 
     */
    boolean isPut();

    /**
     * 
     * @return 
     */
    boolean isForm();
    
    /**
     * 
     * @return 
     */
    boolean isWwwForm();
    
    /**
     * 
     * @return 
     */
    boolean isMultipartForm();

    /**
     * 
     * @return 
     */
    Map<String, String> getPostParameters();

    /**
     * 
     * @param parameter
     * @return 
     */
    String getPostParameter(String parameter);

    /**
     * 
     * @return 
     */
    String[] getPostParametersNames();

    /**
     * 
     * @return 
     */
    UploadedFile[] getUploadedFiles();
    
    /**
     * All the headers names in this request.
     * @return An String array with all the request headers names.
     */
    String[] getHeaders();

    /**
     * Gets the value of the especified header.
     * @param header The header name
     * @return The header value
     */
    String getHeader(String header);

    /**
     * An InputStream witch allows to read the request body for this request.
     * @return The InputStream to read the request body.
     */
    InputStream getInputStream();
}
