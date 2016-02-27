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

package org.bridje.http;

import java.net.InetSocketAddress;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Http server configuration.
 */
@XmlRootElement(name = "http-server")
public class HttpServerConfig
{
    private String host;

    private String name;
    
    private int port;

    public HttpServerConfig()
    {
        this.port = 8080;
        this.name = "Bridje HTTP Server";
    }

    /**
     * The host on witch to start the http server, can be null witch means all ips will be allowed.
     * Especify this only if you plan to restrict the ip on witch the server will accept new connections.
     * @return The host for the HTTP server.
     */
    public String getHost()
    {
        return host;
    }
    
    /**
     * The host on witch to start the http server, can be null witch means all ips will be allowed.
     * Especify this only if you plan to restrict the ip on witch the server will accept new connections.
     * @param host The host for the HTTP server.
     */
    public void setHost(String host)
    {
        this.host = host;
    }
    
    /**
     * The HTTP server name, by default it will be "Bridje HTTP Server" but you can change that setting this property.
     * @return The name of the HTTP server.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The HTTP server name, by default it will be "Bridje HTTP Server" but you can change that setting this property.
     * @param name The name of the HTTP server.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The port on witch the HTTP server will listen for new connections. By default 8080
     * @return The HTTP server port
     */
    public int getPort()
    {
        if(port <= 0)
        {
            port = 8080;
        }
        return port;
    }

    /**
     * The port on witch the HTTP server will listen for new connections. By default 8080
     * @param port The HTTP server port
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Creates the InetSocketAddress to be user by the server.
     * @return A new InetSocketAddress instance 
     */
    public InetSocketAddress createInetSocketAddress()
    {
        if(host == null || host.trim().isEmpty())
        {
            return new InetSocketAddress(getPort());
        }
        else
        {
            return new InetSocketAddress(host, getPort());
        }
    }
}
