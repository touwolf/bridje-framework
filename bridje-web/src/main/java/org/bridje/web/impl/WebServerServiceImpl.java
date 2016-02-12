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

import java.io.IOException;
import javax.servlet.Servlet;
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
    @Inject
    private WebContainerFactory fact;

    @Inject
    private ConfigService cfgServ;

    private WebContainer server;

    @Inject
    private Servlet[] servlets;

    @Override
    public void start()
    {
        try
        {
            if(server == null)
            {
                WebContainerConfig cfg = new WebContainerConfig();
                cfg.setPort(8080);
                cfg = cfgServ.findOrCreateConfig(WebContainerConfig.class, cfg);
                server = fact.createWebServer(cfg);
                for (Servlet servlet : servlets)
                {
                    server.addServlet(servlet);
                }
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
}
