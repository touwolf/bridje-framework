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
import org.bridje.cfg.ConfigService;
import org.bridje.web.WebServer;
import org.bridje.web.WebServerConfig;
import org.bridje.web.WebServerFactory;
import org.bridje.web.WebServerService;
import org.bridje.ioc.Inject;

class WebServerServiceImpl implements WebServerService
{
    @Inject
    private WebServerFactory fact;

    @Inject
    private ConfigService cfgServ;
    
    private WebServer server;
    
    @Override
    public void start()
    {
        try
        {
            if(server == null)
            {
                WebServerConfig cfg = new WebServerConfig();
                cfg.setPort(8080);
                cfg = cfgServ.findOrCreateConfig(WebServerConfig.class, cfg);
                server = fact.createWebServer(cfg);
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
