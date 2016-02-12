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

package org.bridje.web;

import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.XmlConfigAdapter;

/**
 * The web container configuration.
 */
@Configuration(XmlConfigAdapter.class)
@XmlRootElement(name = "web-container")
public class WebContainerConfig
{
    private int port;

    /**
     * Gets the port on wich the web server must be started.
     * 
     * @return The web server port.
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Sets the port on wich the web server must be started.
     * 
     * @param port The web server port.
     */
    public void setPort(int port)
    {
        this.port = port;
    }
}
