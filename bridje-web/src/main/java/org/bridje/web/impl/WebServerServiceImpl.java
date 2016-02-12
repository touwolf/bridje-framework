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

package org.bridje.web.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.Servlet;
import javax.websocket.server.ServerEndpoint;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.Component;
import org.bridje.web.WebContainerConfig;
import org.bridje.ioc.Inject;
import org.bridje.web.WebContainer;
import org.bridje.web.WebContainerFactory;
import org.bridje.web.WebContainerService;

@Component
class WebServerServiceImpl implements WebContainerService
{
    private static final Logger LOG = Logger.getLogger(WebServerServiceImpl.class.getName());

    @Inject
    private WebContainerFactory fact;

    @Inject
    private ConfigService cfgServ;

    private WebContainer server;

    @Inject
    private Servlet[] servlets;
    
    private Set<Class<?>> webSockets;

    @Override
    public void start()
    {
        try
        {
            if(server == null)
            {
                WebContainerConfig cfg = findConfig();
                server = fact.createWebServer(cfg);
                addAllServlets();
                registerAllWebSockets();
            }
            server.start();
        }
        catch(IOException ex)
        {
            throw new IllegalStateException("Cannot read web server configuration.", ex);
        }
    }

    @Override
    public void stop()
    {
        if(server == null)
        {
            throw new IllegalStateException("Web server has not being started.");
        }
        server.stop();
        server = null;
    }

    @Override
    public void join()
    {
        if(server == null)
        {
            throw new IllegalStateException("Web server has not being started.");
        }
        server.join();
    }

    private void addAllServlets()
    {
        for (Servlet servlet : servlets)
        {
            server.addServlet(servlet);
        }
    }
    
    private void registerAllWebSockets()
    {
        for (Class<?> webSocket : getWebSockets())
        {
            server.registerWebSocket(webSocket);
        }
    }

    public Set<Class<?>> getWebSockets()
    {
        if(webSockets == null)
        {
            try
            {
                webSockets = findAllWebSockets();
            }
            catch (IOException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return webSockets;
    }
    
    private WebContainerConfig findConfig() throws IOException
    {
        WebContainerConfig cfg = new WebContainerConfig();
        cfg.setPort(8080);
        return cfgServ.findOrCreateConfig(WebContainerConfig.class, cfg);
    }

    private Set<Class<?>> findAllWebSockets() throws IOException
    {
        Set result = new HashSet();
        List<String> clsList = readAllEndpoints();
        for (String clsName : clsList)
        {
            try
            {
                Class<?> cls = Class.forName(clsName);
                if(cls.getAnnotation(ServerEndpoint.class) != null)
                {
                    result.add(cls);
                }
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return result;
    }

    private List<String> readAllEndpoints() throws IOException
    {
        List<String> result = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(WebSocketProcessor.RESOURCE_FILE);
        while(resources.hasMoreElements())
        {
            URL url = resources.nextElement();
            result.addAll(readEndpoints(url));
        }
        return result;
    }

    private List<String> readEndpoints(URL url) throws IOException
    {
        try (InputStream is = url.openStream())
        {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            return r.lines().collect(Collectors.toList());
        }
    }
}
