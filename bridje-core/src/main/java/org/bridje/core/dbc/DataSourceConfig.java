/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.core.dbc;

/**
 *
 */
public class DataSourceConfig
{
    private String driver;

    private String host;
    
    private int port;
    
    private String user;
    
    private String pass;

    private boolean logAbandoned;

    private boolean removeAbandoned;

    private int removeAbandonedTimeout;

    private int maxActive;

    private long maxWait;

    private long minEvictableIdleTimeMillis;

    private int numTestsPerEvictionRun;

    private long timeBetweenEvictionRunsMillis;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean testWhileIdle;

    private long softMinEvictableIdleTimeMillis;

    public String getDriver()
    {
        return driver;
    }

    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPass()
    {
        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
    }

    public boolean isLogAbandoned()
    {
        return logAbandoned;
    }

    public void setLogAbandoned(boolean logAbandoned)
    {
        this.logAbandoned = logAbandoned;
    }

    public boolean isRemoveAbandoned()
    {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(boolean removeAbandoned)
    {
        this.removeAbandoned = removeAbandoned;
    }

    public int getRemoveAbandonedTimeout()
    {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout)
    {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public int getMaxActive()
    {
        return maxActive;
    }

    public void setMaxActive(int maxActive)
    {
        this.maxActive = maxActive;
    }

    public long getMaxWait()
    {
        return maxWait;
    }

    public void setMaxWait(long maxWait)
    {
        this.maxWait = maxWait;
    }

    public long getMinEvictableIdleTimeMillis()
    {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis)
    {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public int getNumTestsPerEvictionRun()
    {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun)
    {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getTimeBetweenEvictionRunsMillis()
    {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis)
    {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public boolean isTestOnBorrow()
    {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow)
    {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn()
    {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn)
    {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle()
    {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle)
    {
        this.testWhileIdle = testWhileIdle;
    }

    public long getSoftMinEvictableIdleTimeMillis()
    {
        return softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis)
    {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }
}
