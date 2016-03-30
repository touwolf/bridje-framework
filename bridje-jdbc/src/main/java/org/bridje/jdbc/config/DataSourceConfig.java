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

package org.bridje.jdbc.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Represents the configuration parameters needed to initialize a 
 * DataSource object in the JdbcService. The changes to this object 
 * will no have any effects one the DataSource is created.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSourceConfig
{
    private String name;

    private String driver;

    private String url;

    private String user;

    private String password;
    
    private int maxConnections;

    private long idleTime;

    private int minConnections;

    /**
     * Gets the name of the DataSource object that will be found by the 
     * {@link org.bridje.jdbc.JdbcService#getDataSource} method.
     * 
     * @return The name of the DataSource.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the DataSource object that will be found by the 
     * {@link org.bridje.jdbc.JdbcService#getDataSource} method.
     * 
     * @param name The name of the DataSource.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the driver to be use for connecting the database. It must be a valid 
     * jdbc driver class name. (ex: com.mysql.jdbc.Driver for MySQL server).
     * 
     * @return The jdbc driver for this DataSource.
     */
    public String getDriver()
    {
        return driver;
    }

    /**
     * Sets the driver to be use for connecting the database. It must be a valid 
     * jdbc driver class name. (ex: com.mysql.jdbc.Driver for MySQL server).
     * 
     * @param driver The jdbc driver for this DataSource.
     */
    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    /**
     * Gets the jdbc url to connect to by this DataSource.
     * It must be a valid jdbc url connection string like (ex: jdbc:mysql://localhost:3306/books).
     * 
     * @return The jdbc connection string to be use by this DataSource.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Sets the jdbc url to connect to by this DataSource.
     * It must be a valid jdbc url connection string like (ex: jdbc:mysql://localhost:3306/books).
     * 
     * @param url The jdbc connection string to be use by this DataSource.
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * Gets the username to be use to authenticate to the database server.
     * 
     * @return The username for the database server authentication.
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Sets the username to be use to authenticate to the database server.
     * 
     * @param user The username for the database server authentication.
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * Gets the password to be use to authenticate to the database server.
     * 
     * @return The password for the database server authentication.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password to be use to authenticate to the database server.
     * 
     * @param password The password for the database server authentication.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * 
     * @return 
     */
    public int getMaxConnections()
    {
        if(maxConnections <= 0)
        {
            maxConnections = 8;
        }
        return maxConnections;
    }

    /**
     * 
     * @param maxConnections 
     */
    public void setMaxConnections(int maxConnections)
    {
        this.maxConnections = maxConnections;
    }

    public long getIdleTime()
    {
        if(idleTime <= 0)
        {
            idleTime = 600;
        }
        return idleTime;
    }

    public void setIdleTime(long idleTime)
    {
        this.idleTime = idleTime;
    }

    public int getMinConnections()
    {
        if(minConnections <= 0)
        {
            return 3;
        }
        return minConnections;
    }

    public void setMinConnections(int minConnections)
    {
        this.minConnections = minConnections;
    }
}
