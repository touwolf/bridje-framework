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

package org.bridje.http.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bridje.http.HttpCookie;
import org.bridje.http.HttpBridletResponse;

/**
 *
 */
class HttpBridletResponseImpl implements HttpBridletResponse
{
    private final ByteBuf buffer;

    private final OutputStream out;

    private String contentType = "text/html; charset=UTF-8";

    private int statusCode = 200;

    private final Map<String, Object> headers;

    private Map<String, HttpCookieImpl> cookies;

    public HttpBridletResponseImpl(ByteBuf buffer)
    {
        this.buffer = buffer;
        this.buffer.retain();
        out = new ByteBufOutputStream(buffer);
        this.headers = new LinkedHashMap<>();
    }

    @Override
    public OutputStream getOutputStream()
    {
        return out;
    }

    public ByteBuf getBuffer()
    {
        return buffer;
    }

    protected void close() throws IOException
    {
        out.flush();
        out.close();
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }

    @Override
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    @Override
    public int getStatusCode()
    {
        return statusCode;
    }

    @Override
    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    @Override
    public void setHeader(String name, Object value)
    {
        this.headers.put(name, value);
    }

    public Map<String, Object> getHeadersMap()
    {
        return headers;
    }
    
    protected void release()
    {
        this.buffer.release();
    }
    
    protected Map<String, HttpCookieImpl> getCookies()
    {
        return cookies;
    }

    @Override
    public HttpCookie addCookie(String name, String value)
    {
        if(cookies == null)
        {
            cookies = new HashMap<>();
        }
        HttpCookieImpl c = new HttpCookieImpl(new DefaultCookie(name, value));
        cookies.put(c.getName(), c);
        return c;
    }

    protected boolean hasCookie(String name)
    {
        if(cookies != null)
        {
            return cookies.containsKey(name);
        }
        return false;
    }
}
