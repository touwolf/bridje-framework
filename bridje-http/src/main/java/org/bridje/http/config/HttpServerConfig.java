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

package org.bridje.http.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.adapter.XmlConfigAdapter;

/**
 * Http server configuration.
 */
@Configuration(XmlConfigAdapter.class)
@XmlRootElement(name = "http")
public class HttpServerConfig
{
    private String listen = "0.0.0.0";

    private String name = "Bridje HTTP Server";
    
    private int port = 8080;

    private boolean ssl = false;

    private String keyStoreFile = "keyStoreFile.keystore";

    private String keyStorePass = "somepass";

    private String keyStoreType = "JKS";

    private String keyStoreAlgo = KeyManagerFactory.getDefaultAlgorithm();

    private String sslAlgo = "TLS";

    /**
     * The listen ip on witch to start the http server, can be null witch means all ips will be allowed.
     * Especify this only if you plan to restrict the ip on witch the server will accept new connections.
     * @return The listen for the HTTP server.
     */
    public String getListen()
    {
        return listen;
    }
    
    /**
     * The host on witch to start the http server, can be null witch means all ips will be allowed.
     * Especify this only if you plan to restrict the ip on witch the server will accept new connections.
     * @param listen The host for the HTTP server.
     */
    public void setListen(String listen)
    {
        this.listen = listen;
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

    public boolean isSsl()
    {
        return ssl;
    }

    public void setSsl(boolean ssl)
    {
        this.ssl = ssl;
    }

    public String getSslAlgo()
    {
        return sslAlgo;
    }

    public void setSslAlgo(String sslAlgo)
    {
        this.sslAlgo = sslAlgo;
    }

    public String getKeyStoreFile()
    {
        return keyStoreFile;
    }

    public void setKeyStoreFile(String keyStoreFile)
    {
        this.keyStoreFile = keyStoreFile;
    }

    public String getKeyStorePass()
    {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass)
    {
        this.keyStorePass = keyStorePass;
    }

    public String getKeyStoreType()
    {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType)
    {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStoreAlgo()
    {
        return keyStoreAlgo;
    }

    public void setKeyStoreAlgo(String keyStoreAlgo)
    {
        this.keyStoreAlgo = keyStoreAlgo;
    }

    /**
     * Creates the InetSocketAddress to be user by the server.
     * @return A new InetSocketAddress instance 
     */
    public InetSocketAddress createInetSocketAddress()
    {
        if(listen == null || listen.trim().isEmpty())
        {
            return new InetSocketAddress(getPort());
        }
        else
        {
            return new InetSocketAddress(listen, getPort());
        }
    }

    public SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, CertificateException, KeyManagementException
    {
        SSLContext serverContext = SSLContext.getInstance(sslAlgo);
        final KeyStore ks = KeyStore.getInstance(keyStoreType);
        ks.load(readKeyStoreData(), keyStorePass.toCharArray());
        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(keyStoreAlgo);
        kmf.init(ks, keyStorePass.toCharArray());
        serverContext.init(kmf.getKeyManagers(), null, null);
        return serverContext;
    }
    
    private InputStream readKeyStoreData() throws FileNotFoundException
    {
        return new FileInputStream(new File(keyStoreFile));
    }
}
