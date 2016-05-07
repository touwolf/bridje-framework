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
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import java.util.List;
import org.bridje.http.WsServerHandler;

class HttpWsSwitch extends MessageToMessageDecoder<HttpObject>
{
    private boolean added = false;
    
    private final List<WsServerHandler> handlers;

    public HttpWsSwitch(List<WsServerHandler> handlers)
    {
        this.handlers = handlers;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out)
    {
        if(!added && msg instanceof HttpRequest)
        {
            String path = ((HttpRequest)msg).getUri();
            WsServerHandler handler = findHandler(path);
            if(handler != null)
            {
                ctx.pipeline().addAfter("switch", "aggregator", new HttpObjectAggregator(65536));
                ctx.pipeline().addAfter("aggregator", "wsprotocol", new WebSocketServerProtocolHandler(path, null, true));
                ctx.pipeline().addAfter("wsprotocol", "wshandler", new WsFrameHandler(handler));
                added = true;
            }
        }
        out.add(msg);
    }

    private WsServerHandler findHandler(String path)
    {
        for (WsServerHandler handler : handlers)
        {
            if(handler.canHandle(path))
            {
                return handler;
            }
        }
        return null;
    }
    
}
