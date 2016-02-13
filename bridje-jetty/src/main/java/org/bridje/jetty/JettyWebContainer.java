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

package org.bridje.jetty;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.websocket.server.ServerEndpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.bridje.web.WebContainer;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 *
 */
class JettyWebContainer implements WebContainer
{
    private static final Logger LOG = Logger.getLogger(JettyWebContainer.class.getName());

    private final Server server;

    private ServletHandler servletHandler;

    private ServletHandler webSocketHandler;

    private HandlerList mainHandlerList;

    private SessionHandler sessionHandler;
    
    private final Map<Class<?>, WebSocketServlet> wsServlets;

    JettyWebContainer(int port)
    {
        wsServlets = new HashMap<>();
        server = new Server(port);
        server.setHandler(createSessionHandler());
    }

    @Override
    public void addServlet(Servlet servlet)
    {
        WebServlet annot = servlet.getClass().getAnnotation(WebServlet.class);
        ServletHolder holder = new ServletHolder(annot.name(), servlet);
        ServletMapping servletMapping = new ServletMapping();
        servletMapping.setPathSpecs(findUrlPatterns(annot));
        servletMapping.setServletName(annot.name());
        servletHandler.addServlet(holder);
        servletHandler.addServletMapping(servletMapping);
    }

    @Override
    public void removeServlet(Servlet servlet)
    {
        servletHandler.removeBean(servlet);
    }

    @Override
    public void registerWebSocket(final Class<?> cls)
    {
        WebSocketServlet servlet = new WebSocketServlet()
        {
            @Override
            public void configure(WebSocketServletFactory factory)
            {
                factory.setCreator(new WebSocketCreatorImpl(cls));
            }
        };
        ServerEndpoint annot = servlet.getClass().getAnnotation(ServerEndpoint.class);
        ServletHolder holder = new ServletHolder(annot.value(), servlet);
        ServletMapping servletMapping = new ServletMapping();
        servletMapping.setPathSpec(annot.value());
        servletMapping.setServletName(annot.value());
        webSocketHandler.addServlet(holder);
        webSocketHandler.addServletMapping(servletMapping);
        wsServlets.put(cls, servlet);
    }

    @Override
    public void unregisterWebSocket(Class<?> cls)
    {
        WebSocketServlet wsServlet = wsServlets.get(cls);
        webSocketHandler.removeBean(wsServlet);
        wsServlets.remove(cls);
    }

    @Override
    public void start()
    {
        try
        {
            server.start();
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stop()
    {
        try
        {
            server.stop();
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void join()
    {
        try
        {
            server.join();
        }
        catch (InterruptedException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private SessionHandler createSessionHandler()
    {
        sessionHandler = new SessionHandler();
        sessionHandler.setHandler(createHandlerCollection());
        return sessionHandler;
    }

    private HandlerCollection createHandlerCollection()
    {
        mainHandlerList = new HandlerList();
        mainHandlerList.addHandler(createWebSocketHandler());
        mainHandlerList.addHandler(createServletHandler());
        return mainHandlerList;
    }

    private ServletHandler createServletHandler()
    {
        servletHandler = new ServletHandler();
        return servletHandler;
    }

    private ServletHandler createWebSocketHandler()
    {
        webSocketHandler = new ServletHandler();
        return webSocketHandler;
    }

    private String[] findUrlPatterns(WebServlet annot)
    {
        if(annot.urlPatterns() != null && annot.urlPatterns().length > 0)
        {
            return annot.urlPatterns();
        }
        return annot.value();
    }
}
