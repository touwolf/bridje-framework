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
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import org.bridje.http.HttpServer;
import org.bridje.http.WsServerHandler;
import org.bridje.http.config.HttpServerConfig;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFolder;

@Component
class HttpServerImpl implements HttpServer
{
    private static final Logger LOG = Logger.getLogger(HttpServerImpl.class.getName());

    private EventLoopGroup group;

    private HttpServerConfig config;

    @Inject
    private VfsService vfsServ;

    @Inject
    private List<WsServerHandler> handlers;

    private SSLContext sslContext;

    @PostConstruct
    public void init()
    {
        try
        {
            if(getConfig().isSsl())
            {
                sslContext = config.createSSLContext();
            }
        }
        catch (NoSuchAlgorithmException | KeyStoreException | IOException | UnrecoverableKeyException | CertificateException | KeyManagementException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void start()
    {
        try
        {
            LOG.log(Level.INFO, "Starting {0}, Listen: {1} Port: {2} {3}", new Object[]{config.getName(), config.getListen(), String.valueOf(config.getPort()), (config.isSsl() ? "SSL: " + config.getSslAlgo() : "") });
            group = new NioEventLoopGroup();
            try
            {
                ServerBootstrap b = new ServerBootstrap();
                b.group(group)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(this.config.createInetSocketAddress())
                        .childHandler(new ChannelInitializer<SocketChannel>()
                        {
                            @Override
                            public void initChannel(SocketChannel ch)
                            {
                                if(sslContext != null)
                                {
                                    SSLEngine engine = sslContext.createSSLEngine();
                                    engine.setUseClientMode(false);
                                    ch.pipeline().addLast("ssl", new SslHandler(engine));
                                }
                                ch.pipeline().addLast("codec", new HttpServerCodec());
                                ch.pipeline().addLast("switch", new HttpWsSwitch(handlers));
                                ch.pipeline().addLast("handler", new HttpServerChannelHandler(HttpServerImpl.this));
                                ch.pipeline().addLast("compressor", new HttpContentCompressor());
                            }
                        });
                ChannelFuture f = b.bind(this.config.getPort()).sync();
                f.channel().closeFuture().sync();
            }
            finally
            {
                group.shutdownGracefully().sync();
            }
        }
        catch (InterruptedException e)
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
        catch (InterruptedException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public String getServerName()
    {
        return config.getName();
    }

    public HttpServerConfig getConfig()
    {
        if(config == null)
        {
            try
            {
                config = vfsServ.readFile("/etc/http.xml", HttpServerConfig.class);
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
            if(config == null)
            {
                config = new HttpServerConfig();
                try
                {
                    if(vfsServ.canCreateNewFile(new Path("/etc/http.xml")))
                    {
                        VFile http = vfsServ.createNewFile(new Path("/etc/http.xml"));
                        if(http.canOpenForWrite())
                        {
                            vfsServ.writeFile("/etc/http.xml", config);
                        }
                    }

                }
                catch (Exception e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return config;
    }
}
