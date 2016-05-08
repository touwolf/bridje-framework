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

package org.bridje.web;

import java.util.Map;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerRequest;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Scope;

public class WebRequestScope implements Scope
{
    private final HttpServerRequest req;

    @Inject
    private IocContext<WebRequestScope> wrsCxt;
    
    public WebRequestScope(HttpServerContext ctx)
    {
        this.req = ctx.get(HttpServerRequest.class);
    }

    public IocContext<WebRequestScope> getIocContext()
    {
        return wrsCxt;
    }
    
    /**
     * The HTTP method used to made the request.
     *
     * @return An String representing the HTTP method used to made the request.
     */
    public String getMethod()
    {
        return req.getMethod();
    }

    /**
     * The protocol used to made the request
     *
     * @return An String representing the protocol used to made the request
     */
    public String getProtocol()
    {
        return req.getProtocol();
    }

    /**
     * The host of the server the client made the HTTP request to
     *
     * @return An String representing the host name of the server.
     */
    public String getHost()
    {
        return req.getHost();
    }

    /**
     * The UserAgent heather from the http request if any.
     *
     * @return An String representing the UserAgent information from the client
     * if is available.
     */
    public String getUserAgent()
    {
        return req.getUserAgent();
    }

    /**
     * The Accept header sended by the client.
     *
     * @return An String representing the value of the Accept header
     */
    public String getAccept()
    {
        return req.getAccept();
    }

    /**
     * The requested path asked by the client.
     *
     * @return An String representing the requested path asked by the client.
     */
    public String getPath()
    {
        return req.getPath();
    }

    /**
     * The mime/type sended by the client for this request, this method will get
     * the Content-Type http header.
     *
     * @return The mime/type sended by the client for this request.
     */
    public String getConentType()
    {
        return req.getConentType();
    }

    /**
     * When ever this request is http method is "GET".
     *
     * @return true the http method for this request is "GET", false otherwise.
     */
    public boolean isGet()
    {
        return req.isGet();
    }

    /**
     * When ever this request is http method is "POST".
     *
     * @return true the http method for this request is "GET", false otherwise.
     */
    public boolean isPost()
    {
        return req.isPost();
    }

    /**
     * When ever this request is http method is "DELETE".
     *
     * @return true the http method for this request is "DELETE", false
     * otherwise.
     */
    public boolean isDelete()
    {
        return req.isDelete();
    }

    /**
     * When ever this request is http method is "PUT".
     *
     * @return true the http method for this request is "PUT", false otherwise.
     */
    public boolean isPut()
    {
        return req.isPut();
    }

    /**
     * Gets a unmodificable map to the post parameters sended by the client. If
     * this request is not a "application/x-www-form-urlencoded" or a
     * multipart/form-data" the post parameters map will be empty.
     *
     * @return A map with all the post parameters sended by the client.
     */
    public Map<String, String> getPostParameters()
    {
        return req.getPostParameters();
    }

    /**
     * Gets the specific post parameter from the parameters map.
     *
     * @param parameter The post parameter name.
     * @return The post parameter value or null if it does not exists.
     */
    public String getPostParameter(String parameter)
    {
        return req.getPostParameter(parameter);
    }

    /**
     * Gets all the post parameters names for this request if any.
     *
     * @return An array of String representing all the post parameters for this
     * request.
     */
    public String[] getPostParametersNames()
    {
        return req.getPostParametersNames();
    }

    @Override
    public void preCreateComponent(Class<Object> clazz)
    {
        //Before creating a web request scoped component
    }

    @Override
    public void preInitComponent(Class<Object> clazz, Object instance)
    {
        //Before init a web request scoped component
    }

    @Override
    public void postInitComponent(Class<Object> clazz, Object instance)
    {
        //After init a web request scoped component
    }
}
