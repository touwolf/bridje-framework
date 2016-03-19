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
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http.HttpRequest;
import java.io.InputStream;
import java.util.Set;
import org.bridje.http.HttpServerRequest;

/**
 *
 */
class HttpServerRequestImpl implements HttpServerRequest
{
    private ByteBuf buffer;

    private final HttpRequest headers;
    
    private String[] headersArr;

    public HttpServerRequestImpl(HttpRequest headers)
    {
        this.headers = headers;
    }

    @Override
    public InputStream getInputStream()
    {
        return new ByteBufInputStream(buffer);
    }

    public ByteBuf getBuffer()
    {
        return buffer;
    }

    protected void setContent(ByteBuf content)
    {
        this.buffer = content;
    }

    @Override
    public String getMethod()
    {
        return this.headers.getMethod().name();
    }

    @Override
    public String getProtocol()
    {
        return this.headers.getProtocolVersion().text();
    }

    @Override
    public String getHost()
    {
        return this.headers.headers().get("Host");
    }

    @Override
    public String getUserAgent()
    {
        return this.headers.headers().get("User-Agent");
    }

    @Override
    public String getAccept()
    {
        return this.headers.headers().get("Accept");
    }

    @Override
    public String getAcceptLanguage()
    {
        return this.headers.headers().get("Accept-Language");
    }
    
    @Override
    public String getPath()
    {
        return this.headers.getUri();
    }

    @Override
    public String getHeader(String header)
    {
        return this.headers.headers().get(header);
    }

    @Override
    public String[] getHeaders()
    {
        if(headersArr == null)
        {
            Set<String> names = headers.headers().names();
            headersArr = new String[names.size()];
            names.toArray(headersArr);
        }
        return headersArr;
    }
}
