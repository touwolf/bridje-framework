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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import static io.netty.handler.codec.http.HttpHeaders.Names.SERVER;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;

/**
 *
 */
class HttpServerChannelHandler extends SimpleChannelInboundHandler<Object>
{
    private static final Logger LOG = Logger.getLogger(HttpServerChannelHandler.class.getName());

    private HttpServerContext context;

    private HttpServerRequestImpl req;

    private HttpServerResponseImpl resp;
    
    private final HttpServerImpl server;

    public HttpServerChannelHandler(HttpServerImpl server)
    {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if(msg instanceof HttpRequest)
        {
            if(req != null || context != null)
            {
                sendBadRequest(ctx);
            }
            else
            {
                context = new HttpServerContextImpl();
                req = new HttpServerRequestImpl( (HttpRequest)msg );
            }
        }
        else if(msg instanceof HttpContent)
        {
            if(req == null)
            {
                sendBadRequest(ctx);
            }
            else
            {
                req.setContent(((HttpContent)msg).content());
                //if is the last http content
                if(msg instanceof LastHttpContent)
                {
                    if(resp == null)
                    {
                        resp = new HttpServerResponseImpl(ctx.alloc().buffer());
                    }
                    RootServerHandler rootHandler = Ioc.context().find(RootServerHandler.class);
                    context.set(HttpServerRequest.class, req);
                    context.set(HttpServerResponse.class, resp);
                    rootHandler.handle(context);
                    sendResponse(ctx);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LOG.log(Level.SEVERE, cause.getMessage(), cause);
        ctx.close();
        context = null;
        req = null;
        resp = null;
    }

    private void sendResponse(ChannelHandlerContext ctx) throws IOException
    {
        resp.close();
        int length = resp.getBuffer().readableBytes();
        DefaultHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(resp.getStatusCode()), resp.getBuffer());
        resp.setHeader(SERVER, server.getServerName());
        resp.setHeader(CONTENT_TYPE, resp.getContentType());
        resp.setHeader(CONTENT_LENGTH, length);
        resp.setHeader(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        Map<String, Object> headers = resp.getHeadersMap();
        for (Map.Entry<String, Object> entry : headers.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof Iterable)
            {
                response.headers().set(key, (Iterable<?>)value);
            }
            else
            {
                response.headers().set(key, value);
            }
        }
        ctx.write(response);
        ctx.flush();
        context = null;
        req = null;
        resp = null;
    }

    private void sendBadRequest(ChannelHandlerContext ctx)
    {
        DefaultHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
        response.headers().set(SERVER, server.getServerName());
        response.headers().set(CONTENT_TYPE, "text/html");
        response.headers().set(CONTENT_LENGTH, 0);
        response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
        context = null;
        req = null;
        resp = null;
    }
}