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

import org.bridje.ioc.Component;
import org.bridje.web.WebServer;
import org.bridje.web.WebServerConfig;
import org.bridje.web.WebServerFactory;

@Component
class JettyWebServerFactory implements WebServerFactory
{
    @Override
    public WebServer createWebServer(WebServerConfig config)
    {
        return new JettyWebServer(config.getPort());
    }
    
}
