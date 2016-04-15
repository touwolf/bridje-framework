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

/**
 * Http server configuration.
 */
@XmlRootElement(name = "http")
public class HttpServerConfig
{
    private String listen = "0.0.0.0";

    private String name = "Bridje HTTP Server";

    private int port = 8080;

    private boolean ssl;

    private String keyStoreFile = "keyStoreFile.keystore";

    private String keyStorePass = "somepass";

    private String keyStoreType = "JKS";

    private String keyStoreAlgo = KeyManagerFactory.getDefaultAlgorithm();

    private String sslAlgo = "TLS";

    /**
     * The listen ip on witch to start the http server, can be null witch means
     * all ips will be allowed. Especify this only if you plan to restrict the
     * ip on witch the server will accept new connections.
     *
     * @return The listen for the HTTP server.
     */
    public String getListen()
    {
        return listen;
    }

    /**
     * The host on witch to start the http server, can be null witch means all
     * ips will be allowed. Especify this only if you plan to restrict the ip on
     * witch the server will accept new connections.
     *
     * @param listen The host for the HTTP server.
     */
    public void setListen(String listen)
    {
        this.listen = listen;
    }

    /**
     * The HTTP server name, by default it will be "Bridje HTTP Server" but you
     * can change that setting this property.
     *
     * @return The name of the HTTP server.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The HTTP server name, by default it will be "Bridje HTTP Server" but you
     * can change that setting this property.
     *
     * @param name The name of the HTTP server.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The port on witch the HTTP server will listen for new connections. By
     * default 8080
     *
     * @return The HTTP server port
     */
    public int getPort()
    {
        if (port <= 0)
        {
            port = 8080;
        }
        return port;
    }

    /**
     * The port on witch the HTTP server will listen for new connections. By
     * default 8080
     *
     * @param port The HTTP server port
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Gets if the server must run with ssl connector for https, if this
     * parameter is true, the ssl parameters must be configure properly for the
     * server to start.
     *
     * @return true ssl is enabled, false ssl is disabled.
     */
    public boolean isSsl()
    {
        return ssl;
    }

    /**
     * Gets if the server must run with ssl connector for https, if this
     * parameter is true, the ssl parameters must be configure properly for the
     * server to start.
     *
     * @param ssl true ssl is enabled, false ssl is disabled.
     */
    public void setSsl(boolean ssl)
    {
        this.ssl = ssl;
    }

    /**
     * The algorithm to use to create the SSLContext.
     * <br>
     * Note that the list of registered providers may be retrieved via the
     * Security.getProviders() method.
     *
     * @return The algorithm that will be use to create the SSLContext.
     */
    public String getSslAlgo()
    {
        return sslAlgo;
    }

    /**
     * The algorithm to use to create the SSLContext.
     * <br>
     * Note that the list of registered providers may be retrieved via the
     * Security.getProviders() method.
     *
     * @param sslAlgo The algorithm that will be use to create the SSLContext.
     */
    public void setSslAlgo(String sslAlgo)
    {
        this.sslAlgo = sslAlgo;
    }

    /**
     * The file that holds the keystore for the SSLContext.
     *
     * @return An String with the path relative to the current folder for the
     * keystore file.
     */
    public String getKeyStoreFile()
    {
        return keyStoreFile;
    }

    /**
     * The file that holds the keystore for the SSLContext.
     *
     * @param keyStoreFile An String with the path relative to the current
     * folder for the keystore file.
     */
    public void setKeyStoreFile(String keyStoreFile)
    {
        this.keyStoreFile = keyStoreFile;
    }

    /**
     * The password for the keystore file.
     *
     * @return The password for the keystore file.
     */
    public String getKeyStorePass()
    {
        return keyStorePass;
    }

    /**
     * The password for the keystore file.
     *
     * @param keyStorePass The password for the keystore file.
     */
    public void setKeyStorePass(String keyStorePass)
    {
        this.keyStorePass = keyStorePass;
    }

    /**
     * The KeyStore type to create for the SSLContext.
     *
     * @return The KeyStore type to create for the SSLContext.
     */
    public String getKeyStoreType()
    {
        return keyStoreType;
    }

    /**
     * The KeyStore type to create for the SSLContext.
     *
     * @param keyStoreType The KeyStore type to create for the SSLContext.
     */
    public void setKeyStoreType(String keyStoreType)
    {
        this.keyStoreType = keyStoreType;
    }

    /**
     * The algorithm for the KeyManagerFactory.
     *
     * @return The algorithm for the KeyManagerFactory.
     */
    public String getKeyStoreAlgo()
    {
        return keyStoreAlgo;
    }

    /**
     * The algorithm for the KeyManagerFactory.
     *
     * @param keyStoreAlgo The algorithm for the KeyManagerFactory.
     */
    public void setKeyStoreAlgo(String keyStoreAlgo)
    {
        this.keyStoreAlgo = keyStoreAlgo;
    }

    /**
     * Creates the InetSocketAddress to be user by the server.
     *
     * @return A new InetSocketAddress instance
     */
    public InetSocketAddress createInetSocketAddress()
    {
        if (listen == null || listen.trim().isEmpty())
        {
            return new InetSocketAddress(getPort());
        }
        else
        {
            return new InetSocketAddress(listen, getPort());
        }
    }

    /**
     * Creates a new SSLContext from the parameters of this configuration, that
     * can be use for the ssl codec of the http server.
     *
     * @return The new created SSLContext for the http server ssl codec.
     * @throws NoSuchAlgorithmException If a wrong algorithm is
     * provided.
     * @throws KeyStoreException If any error occurrs with the KeyStore.
     * @throws IOException If the keystore file is not found or cannot be readed.
     * @throws UnrecoverableKeyException This exception is thrown if a key in the keystore cannot be recovered.
     * @throws CertificateException This exception indicates one of a variety of certificate problems.
     * @throws KeyManagementException This is the general key management exception for all operations dealing with key management.
     */
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
