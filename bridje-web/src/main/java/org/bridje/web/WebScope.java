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
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.http.HttpCookie;
import org.bridje.http.HttpReqParam;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Scope;
import org.bridje.web.session.WebSession;

/**
 * Represents the IoC scope for the web request IocContext.
 */
public final class WebScope implements Scope
{
    private final HttpBridletRequest req;

    private final HttpBridletResponse resp;

    @Inject
    private IocContext<WebScope> iocCtx;

    private final HttpBridletContext srvCtx;

    private WebSession session;

    /**
     * The only constructor for this object, the HTTP bridlet context for the
     * request mus be provided.
     *
     * @param ctx The HTTP bridlet context for the current HTTP
     * request.
     */
    public WebScope(HttpBridletContext ctx)
    {
        this.srvCtx = ctx;
        this.req = ctx.get(HttpBridletRequest.class);
        this.resp = ctx.get(HttpBridletResponse.class);
    }

    /**
     * The IoC context for this web request.
     *
     * @return The instance of IocContext created for this HTTP request.
     */
    public IocContext<WebScope> getIocContext()
    {
        return iocCtx;
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
     * The UserAgent heather from the HTTP request if any.
     *
     * @return An String representing the UserAgent information from the client
     * if is available.
     */
    public String getUserAgent()
    {
        return req.getUserAgent();
    }

    /**
     * The Accept header sent by the client.
     *
     * @return An String representing the value of the Accept header
     */
    public String getAccept()
    {
        return req.getAccept();
    }

    /**
     * The current requested path, this method finds a ReqPathRef instance if
     * any and retrieve the path specified there, if no instance is available it
     * returns the requested path.
     *
     * @return An String representing the requested path asked by the client.
     */
    public String getPath()
    {
        return ReqPathRef.findCurrentPath(srvCtx);
    }

    /**
     * The requested path asked by the client.
     *
     * @return An String representing the requested path asked by the client.
     */
    public String getOrigPath()
    {
        return req.getPath();
    }

    /**
     * The mime/type sent by the client for this request, this method will get
     * the Content-Type HTTP header.
     *
     * @return The mime/type sent by the client for this request.
     */
    public String getContentType()
    {
        return req.getContentType();
    }

    /**
     * When ever this request is HTTP method is "GET".
     *
     * @return true the HTTP method for this request is "GET", false otherwise.
     */
    public boolean isGet()
    {
        return req.isGet();
    }

    /**
     * When ever this request is HTTP method is "POST".
     *
     * @return true the HTTP method for this request is "GET", false otherwise.
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
     * When ever this request is HTTP method is "PUT".
     *
     * @return true the HTTP method for this request is "PUT", false otherwise.
     */
    public boolean isPut()
    {
        return req.isPut();
    }

    /**
     * When ever this request is HTTP method is "PATCH".
     *
     * @return true the HTTP method for this request is "PATCH", false
     * otherwise.
     */
    public boolean isPatch()
    {
        return req.isPatch();
    }

    /**
     * Gets a unmodificable map to the post parameters sent by the client. If
     * this request is not a "application/x-www-form-urlencoded" or a
     * multipart/form-data" the post parameters map will be empty.
     *
     * @return A map with all the post parameters sent by the client.
     */
    public Map<String, HttpReqParam> getPostParameters()
    {
        return req.getPostParameters();
    }

    /**
     * Gets the specific post parameter from the parameters map.
     *
     * @param parameter The post parameter name.
     * @return The post parameter value or null if it does not exists.
     */
    public HttpReqParam getPostParameter(String parameter)
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

    /**
     * Gets the "GET" parameters that where sent by the client in the query
     * string of the request.
     *
     * @return A map with all the "GET" parameters for this request.
     */
    public Map<String, HttpReqParam> getGetParameters()
    {
        return req.getGetParameters();
    }

    /**
     * Gets the specific "GET" parameter from the parameters map.
     *
     * @param parameter The "GET" parameter name.
     * @return The "GET" parameter value or null if it does not exists.
     */
    public HttpReqParam getGetParameter(String parameter)
    {
        return req.getGetParameter(parameter);
    }

    /**
     * Gets all the "GET" parameters names for this request if any.
     *
     * @return An array of String representing all the "GET" parameters for this
     * request.
     */
    public String[] getGetParametersNames()
    {
        return req.getGetParametersNames();
    }

    /**
     * Gets a map with all the cookies sent to the server by the client.
     *
     * @return A map with the HTTP cookies for this request.
     */
    public Map<String, HttpCookie> getCookies()
    {
        return req.getCookies();
    }

    /**
     * Gets the specified HTTP cookie.
     *
     * @param name The name of the HTTP cookie.
     * @return the HttpCookie object representing the cookie or null if it does
     * not exists.
     */
    public HttpCookie getCookie(String name)
    {
        return req.getCookie(name);
    }

    /**
     * Adds a cookie to the HTTP response.
     *
     * @param name The name of the HTTP Cookie.
     * @param value The value of the HTTP Cookie.
     * @return The HTTP Cookie added.
     */
    public HttpCookie addCookie(String name, String value)
    {
        return resp.addCookie(name, value);
    }

    /**
     * Gets all the cookies names available in this request.
     *
     * @return An array of String representing the cookies names.
     */
    public String[] getCookiesNames()
    {
        return req.getCookiesNames();
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

    /**
     * Gets the current WebSession for this HTTP request.
     *
     * @return The current WebSession for this HTTP request.
     */
    public WebSession getSession()
    {
        if (session == null)
        {
            session = srvCtx.get(WebSession.class);
        }
        return session;
    }
}
