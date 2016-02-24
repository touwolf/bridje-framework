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
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;

/**
 *
 */
public class HttpServerChannelHandler extends SimpleChannelInboundHandler<Object>
{
    private static final Logger LOG = Logger.getLogger(HttpServerChannelHandler.class.getName());
    
    private HttpServerContext context;
    
    private HttpServerRequestImpl req;
    
    private HttpServerResponseImpl resp;
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if(msg instanceof HttpRequest)
        {
            
        }
        else if(msg instanceof HttpContent)
        {
            if(req != null)
            {
                req = new HttpServerRequestImpl( ((HttpContent)msg).content() );
            }
            if(msg instanceof LastHttpContent)
            {
                if(resp == null)
                {
                    resp = new HttpServerResponseImpl();
                }
                RootServerHandler rootHandler = Ioc.context().find(RootServerHandler.class);
                context.set(HttpServerRequest.class, req);
                context.set(HttpServerResponse.class, resp);
                rootHandler.handle(context);
            }
            else
            {
                //not the last http content
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LOG.log(Level.SEVERE, cause.getMessage(), cause);
        ctx.close();
    }

    private void send404(ChannelHandlerContext ctx) throws UnsupportedEncodingException
    {
        DefaultHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, 0);
        response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        ctx.write(response);
    }

    private void sendResponse(ChannelHandlerContext ctx) throws UnsupportedEncodingException
    {
        DefaultHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, 0);
        response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        ctx.write(response);
    }
}