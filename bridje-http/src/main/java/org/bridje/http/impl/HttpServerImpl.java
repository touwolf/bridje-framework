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
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.xml.bind.JAXBException;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpServer;
import org.bridje.http.WsServerHandler;
import org.bridje.http.config.HttpServerConfig;
import org.bridje.ioc.Application;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;

@Component
class HttpServerImpl implements HttpServer
{
    private static final Logger LOG = Logger.getLogger(HttpServerImpl.class.getName());

    private EventLoopGroup acceptor;
    
    private EventLoopGroup group;

    private HttpServerConfig config;

    @Inject
    private List<WsServerHandler> handlers;

    private SSLContext sslContext;

    private Thread serverThread;

    @Inject
    private IocContext<Application> appCtx;

    @PostConstruct
    public void init()
    {
        try
        {
            initConfig();
            if(config.isSsl())
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
        serverThread = new Thread(() ->
        {
            try
            {
                LOG.log(Level.INFO, "Starting {0}, Listen: {1} Port: {2} {3}", new Object[]{config.getName(), config.getListen(), String.valueOf(config.getPort()), (config.isSsl() ? "SSL: " + config.getSslAlgo() : "") });
                logBridlets();
                group = new NioEventLoopGroup();
                acceptor = new NioEventLoopGroup(3);
                try
                {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(acceptor, group)
                            .option(ChannelOption.SO_BACKLOG, 128)
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
                    shutdownGroups();
                }
            }
            catch (InterruptedException e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        });
        serverThread.start();
    }

    @Override
    public void stop()
    {
        try
        {
            shutdownGroups();
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

    private void initConfig() 
    {
        config = loadConfigFile();
        if(config == null) config = new HttpServerConfig();
    }
    
    private HttpServerConfig loadConfigFile() 
    {
        try
        {
            return HttpServerConfig.load(CONFIG_FILE);
        }
        catch (JAXBException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public void join()
    {
        try
        {
            if(serverThread != null)
            {
                serverThread.join();
            }
        }
        catch (InterruptedException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void printBridlets(PrintWriter writer)
    {
        appCtx.printPriorities(HttpBridlet.class, writer);
    }

    private void logBridlets()
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("HTTP Bridlets Chain:");
        printBridlets(pw);
        LOG.log(Level.INFO, sw.toString());
    }

    private void shutdownGroups() throws InterruptedException
    {
        boolean wereRunning = false;
        if(!acceptor.isShutdown())
        {
            wereRunning = true;
            LOG.log(Level.INFO, "Stoping HTTP server acceptor group.");
            acceptor.shutdownGracefully(0, 20, TimeUnit.SECONDS).sync();
        }
        if(!group.isShutdown())
        {
            wereRunning = true;
            LOG.log(Level.INFO, "Stoping HTTP server worker group.");
            group.shutdownGracefully(0, 20, TimeUnit.SECONDS).sync();
        }
        if(wereRunning && acceptor.isShutdown() && group.isShutdown())
        {
            LOG.log(Level.INFO, "HTTP server groups have been shutdown.");
        }
    }
}
