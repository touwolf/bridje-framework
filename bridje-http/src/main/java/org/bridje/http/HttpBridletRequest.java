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
public interface HttpBridletRequest
{
    /**
     * The HTTP method used to made the request.
     *
     * @return An String representing the HTTP method used to made the request.
     */
    String getMethod();

    /**
     * The protocol used to made the request
     *
     * @return An String representing the protocol used to made the request
     */
    String getProtocol();

    /**
     * The host of the server the client made the HTTP request to
     *
     * @return An String representing the host name of the server.
     */
    String getHost();

    /**
     * The UserAgent heather from the http request if any.
     *
     * @return An String representing the UserAgent information from the client
     * if is available.
     */
    String getUserAgent();

    /**
     * The Accept header sent by the client.
     *
     * @return An String representing the value of the Accept header
     */
    String getAccept();

    /**
     * The AcceptLanguaje header sent by the client.
     *
     * @return An String representing the value of the AcceptLanguaje header
     */
    String getAcceptLanguage();

    /**
     * The requested path asked by the client.
     *
     * @return An String representing the requested path asked by the client.
     */
    String getPath();

    /**
     * The mime/type sent by the client for this request, this method will get
     * the Content-Type http header.
     *
     * @return The mime/type sent by the client for this request.
     */
    String getContentType();

    /**
     * When ever this request is http method is "GET".
     *
     * @return true the http method for this request is "GET", false otherwise.
     */
    boolean isGet();

    /**
     * When ever this request is http method is "POST".
     *
     * @return true the http method for this request is "GET", false otherwise.
     */
    boolean isPost();

    /**
     * When ever this request is http method is "DELETE".
     *
     * @return true the http method for this request is "DELETE", false
     * otherwise.
     */
    boolean isDelete();

    /**
     * When ever this request is http method is "PUT".
     *
     * @return true the http method for this request is "PUT", false otherwise.
     */
    boolean isPut();

    /**
     * When ever this request is http method is "PATCH".
     *
     * @return true the http method for this request is "PATCH", false otherwise.
     */
    boolean isPatch();

    /**
     * When ever this request content type is a www-form or multipart-form.
     *
     * @return true this request content type is a www-form or multipart-form,
     * false otherwise.
     */
    boolean isForm();

    /**
     * When ever this request is a "application/x-www-form-urlencoded".
     *
     * @return true this request is a "application/x-www-form-urlencoded", false
     * otherwise.
     */
    boolean isWwwForm();

    /**
     * When ever this request is a "multipart/form-data".
     *
     * @return true this request is a "multipart/form-data", false otherwise.
     */
    boolean isMultipartForm();

    /**
     * Gets a unmodificable map to the post parameters sent by the client. If
     * this request is not a "application/x-www-form-urlencoded" or a
     * multipart/form-data" the post parameters map will be empty.
     *
     * @return A map with all the post parameters sent by the client.
     */
    Map<String, HttpReqParam> getPostParameters();

    /**
     * Gets the specific post parameter from the parameters map.
     *
     * @param parameter The post parameter name.
     * @return The post parameter value or null if it does not exists.
     */
    HttpReqParam getPostParameter(String parameter);

    /**
     * Gets all the post parameters names for this request if any.
     *
     * @return An array of String representing all the post parameters for this
     * request.
     */
    String[] getPostParametersNames();

    /**
     * Gets the "GET" parameters that where sent by the client in the query
     * string of the request.
     *
     * @return A map with all the "GET" parameters for this request.
     */
    public Map<String, HttpReqParam> getGetParameters();

    /**
     * Gets the specific "GET" parameter from the parameters map.
     *
     * @param parameter The "GET" parameter name.
     * @return The "GET" parameter value or null if it does not exists.
     */
    HttpReqParam getGetParameter(String parameter);
    
    /**
     * Gets all the "GET" parameters names for this request if any.
     *
     * @return An array of String representing all the "GET" parameters for this
     * request.
     */
    String[] getGetParametersNames();

    /**
     * Gets the uploaded files for a "multipart/form-data" request.
     *
     * @return The array of uploaded files if any.
     */
    UploadedFile[] getUploadedFiles();

    /**
     * All the headers names in this request.
     *
     * @return An String array with all the request headers names.
     */
    String[] getHeaders();

    /**
     * Gets the value of the especified header.
     *
     * @param header The header name
     * @return The header value
     */
    String getHeader(String header);

    /**
     * An InputStream witch allows to read the request body for this request.
     *
     * @return The InputStream to read the request body.
     */
    InputStream getInputStream();

    /**
     * Gets a map with all the cookies sent to the server by the client.
     *
     * @return A map with the HTTP cookies for this request.
     */
    Map<String, HttpCookie> getCookies();

    /**
     * Gets the specified HTTP cookie.
     *
     * @param name The name of the HTTP cookie.
     * @return the HttpCookie object representing the cookie or null if it does
     * not exists.
     */
    HttpCookie getCookie(String name);

    /**
     * Gets all the cookies names available in this request.
     *
     * @return An array of String representing the cookies names.
     */
    String[] getCookiesNames();
}
