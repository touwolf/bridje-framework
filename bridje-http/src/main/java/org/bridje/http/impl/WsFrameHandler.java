/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bridje.http.impl;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.bridje.http.WsServerHandler;

class WsFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame>
{
    private final WsServerHandler handler;

    public WsFrameHandler(WsServerHandler handler)
    {
        this.handler = handler;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception
    {
        // ping and pong frames already handled
        if (frame instanceof TextWebSocketFrame)
        {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            handler.onText(new WsChannelImpl(ctx.channel()), request);
        }
        else
        {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
}
