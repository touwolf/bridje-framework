/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      SIP://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.sip.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;

/**
 * SIP server configuration.
 */
@XmlRootElement(name = "http")
public class SipServerConfig
{
    private String listen = "0.0.0.0";

    private String name = "Bridje SIP Server";

    private int port = 5060;

    private boolean ssl;

    private String keyStoreFile = "keyStoreFile.keystore";

    private String keyStorePass = "somepass";

    private String keyStoreType = "JKS";

    private String keyStoreAlgo = KeyManagerFactory.getDefaultAlgorithm();

    private String sslAlgo = "TLS";

    /**
     * The listen IP on witch to start the SIP server, can be null witch means
     * all IPs will be allowed. Specify this only if you plan to restrict the
     * IP on witch the server will accept new connections.
     *
     * @return The listen for the SIP server.
     */
    public String getListen()
    {
        return listen;
    }

    /**
     * The host on witch to start the SIP server, can be null witch means all
     * IPs will be allowed. Specify this only if you plan to restrict the IP on
     * witch the server will accept new connections.
     *
     * @param listen The host for the SIP server.
     */
    public void setListen(String listen)
    {
        this.listen = listen;
    }

    /**
     * The SIP server name, by default it will be "Bridje SIP Server" but you
     * can change that setting this property.
     *
     * @return The name of the SIP server.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The SIP server name, by default it will be "Bridje SIP Server" but you
     * can change that setting this property.
     *
     * @param name The name of the SIP server.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The port on witch the SIP server will listen for new connections. By
     * default 8080
     *
     * @return The SIP server port
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
     * The port on witch the SIP server will listen for new connections. By
     * default 8080
     *
     * @param port The SIP server port
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Gets if the server must run with SSL connector for SIPs, if this
     * parameter is true, the SSL parameters must be configure properly for the
     * server to start.
     *
     * @return true SSL is enabled, false SSL is disabled.
     */
    public boolean isSsl()
    {
        return ssl;
    }

    /**
     * Gets if the server must run with SSL connector for SIPs, if this
     * parameter is true, the SSL parameters must be configure properly for the
     * server to start.
     *
     * @param ssl true SSL is enabled, false SSL is disabled.
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
     * The file that holds the key store for the SSLContext.
     *
     * @return An String with the path relative to the current folder for the
     * key store file.
     */
    public String getKeyStoreFile()
    {
        return keyStoreFile;
    }

    /**
     * The file that holds the key store for the SSLContext.
     *
     * @param keyStoreFile An String with the path relative to the current
     * folder for the key store file.
     */
    public void setKeyStoreFile(String keyStoreFile)
    {
        this.keyStoreFile = keyStoreFile;
    }

    /**
     * The password for the key store file.
     *
     * @return The password for the key store file.
     */
    public String getKeyStorePass()
    {
        return keyStorePass;
    }

    /**
     * The password for the key store file.
     *
     * @param keyStorePass The password for the key store file.
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
     * can be use for the SSL codec of the SIP server.
     *
     * @return The new created SSLContext for the SIP server SSL codec.
     * @throws NoSuchAlgorithmException If a wrong algorithm is
     * provided.
     * @throws KeyStoreException If any error occurs with the KeyStore.
     * @throws IOException If the key store file is not found or cannot be read.
     * @throws UnrecoverableKeyException This exception is thrown if a key in the key store cannot be recovered.
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

    /**
     * Loads a SipServerConfig from a file.
     * 
     * @param xmlFile The file to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     * @throws IOException If any IO Exception occurs.
     */
    public static SipServerConfig load(VFile xmlFile) throws JAXBException, IOException
    {
        if(!xmlFile.exists()) return null;
        try(InputStream is = new VFileInputStream(xmlFile))
        {
            return load(is);
        }
    }

    /**
     * Loads a SipServerConfig from an input stream.
     * 
     * @param is The input stream to load the object from.
     * @return The loaded object.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static SipServerConfig load(InputStream is) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(SipServerConfig.class);
        return (SipServerConfig)ctx.createUnmarshaller().unmarshal(is);
    }

    /**
     * Save a SipServerConfig to an output stream.
     * 
     * @param os The output stream to write the object to.
     * @param object The object to write.
     * @throws JAXBException If any JAXB Exception occurs.
     */
    public static void save(OutputStream os, SipServerConfig object) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(SipServerConfig.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, os);
    }

    private InputStream readKeyStoreData() throws FileNotFoundException
    {
        return new FileInputStream(new File(keyStoreFile));
    }
}
