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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.http.HttpServer;
import org.bridje.ioc.Component;

/**
 *
 */
@Component
public class HttpServerImpl implements HttpServer
{
    private static final Logger LOG = Logger.getLogger(HttpServerImpl.class.getName());

    private final int port;
    
    private EventLoopGroup group;
    
    private final String serverName;

    public HttpServerImpl()
    {
        this.serverName = "Bridje HTTP Server";
        this.port = 8080;
    }

    @Override
    public void start()
    {
        try
        {
            group = new NioEventLoopGroup();
            try
            {
                ServerBootstrap b = new ServerBootstrap();
                b.group(group)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress(port))
                        .childHandler(new ChannelInitializer<SocketChannel>()
                        {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception
                            {
                                ch.pipeline().addLast("decoder", new HttpRequestDecoder());
                                ch.pipeline().addLast("encoder", new HttpResponseEncoder());
                                ch.pipeline().addLast("handler", new HttpServerChannelHandler(HttpServerImpl.this));
                                ch.pipeline().addLast("compressor", new HttpContentCompressor());
                            }
                        });
                ChannelFuture f = b.bind(port).sync();
                f.channel().closeFuture().sync();
            }
            finally
            {
                group.shutdownGracefully().sync();
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void stop()
    {
        try
        {
            group.shutdownGracefully().sync();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public String getServerName()
    {
        return serverName;
    }
}
