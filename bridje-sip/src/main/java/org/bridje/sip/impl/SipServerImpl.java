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

package org.bridje.sip.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.bridje.sip.config.SipServerConfig;
import org.bridje.ioc.Application;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.sip.SipBridlet;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.sip.SipServer;

@Component
class SipServerImpl implements SipServer
{
    private static final Logger LOG = Logger.getLogger(SipServerImpl.class.getName());

    private EventLoopGroup group;

    private SipServerConfig config;

    @Inject
    private VfsService vfsServ;
    
    private Thread serverThread;
    
    @Inject
    private IocContext<Application> appCtx;

    @PostConstruct
    public void init()
    {
        try
        {
            initConfig();
        }
        catch (IOException e)
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
                try
                {
                    Bootstrap b = new Bootstrap();
                    b.group(group)
                            .channel(NioDatagramChannel.class)
                            .option(ChannelOption.SO_BROADCAST, true)
                            .handler(new ChannelInitializer<NioDatagramChannel>()
                            {
                                @Override
                                public void initChannel(NioDatagramChannel ch)
                                {
                                    ChannelPipeline p = ch.pipeline();
                                    p.addLast("encoder", new SipResponseEncoder());
                                    p.addLast("decoder", new SipRequestDecoder());
                                    p.addLast("sip", new SipServerChannelHandler());
                                }
                            });
                    ChannelFuture f = b.bind(this.config.getListen(), this.config.getPort()).sync();
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
        });
        serverThread.start();
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

    private void initConfig() throws IOException
    {
        Path path = new Path("/etc/http.xml");
        config = vfsServ.readFile(path, SipServerConfig.class);
        if(config == null)
        {
            config = new SipServerConfig();
            if(vfsServ.canCreateNewFile(path))
            {
                vfsServ.createAndWriteNewFile(path, config);
            }
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
        appCtx.printPriorities(SipBridlet.class, writer);
    }

    private void logBridlets()
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("SIP Bridlets Chain:");
        printBridlets(pw);
        LOG.log(Level.INFO, sw.toString());
    }
}
