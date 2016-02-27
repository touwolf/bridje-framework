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
 *
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

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPort()
    {
        if(port <= 0)
        {
            port = 8080;
        }
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }
    
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
