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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class HttpServerChannelHandler extends SimpleChannelInboundHandler<Object>
{
    private static final Logger LOG = Logger.getLogger(HttpServerChannelHandler.class.getName());
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println(Thread.currentThread().getName());
        if(msg instanceof HttpRequest)
        {
            
        }
        else if(msg instanceof HttpContent)
        {
            //All http content
            if(msg instanceof LastHttpContent)
            {
                ByteBuf buffer = ctx.alloc().buffer();
                buffer.writeBytes("hola".getBytes("UTF-8"), 0, "hola".getBytes("UTF-8").length);
                DefaultHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
                response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
                response.headers().set(CONTENT_LENGTH, "hola".getBytes("UTF-8").length);
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.write(response);
            }
            else
            {
                //not the las http content
            }
        }
        System.out.println(msg);
        System.out.println("----------------------------------------------------");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LOG.log(Level.SEVERE, cause.getMessage(), cause);
        ctx.close();
    }
}