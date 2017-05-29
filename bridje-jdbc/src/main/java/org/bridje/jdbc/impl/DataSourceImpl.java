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

package org.bridje.jdbc.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.jdbc.config.DataSourceConfig;

class DataSourceImpl implements DataSource
{
    private static final Logger LOG = Logger.getLogger(DataSourceImpl.class.getName());

    private final Deque<ConnectionImpl> freeConnections = new ConcurrentLinkedDeque<>();

    private final Deque<ConnectionImpl> usedConnections = new ConcurrentLinkedDeque<>();

    private final DataSourceConfig config;
    
    private PrintWriter logWriter;
    
    private int loginTimeout;
    
    private boolean closed;
    
    private long lastCheck;
    
    public DataSourceImpl(DataSourceConfig config)
    {
        this.config = config;
        this.lastCheck = System.currentTimeMillis();
    }
    
    @Override
    public Connection getConnection() throws SQLException
    {
        if(closed)
        {
            throw new SQLException("The DataSource is close.");
        }
        Connection result = getFreeConnection();
        if(result != null)
        {
            checkIdleConnections();
            return result;
        }
        result = getNewConnection();
        if(result != null)
        {
            return result;
        }
        return waitFreeConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException
    {
        if(closed)
        {
            throw new SQLException("The DataSource is close.");
        }
        return createNewConnection(username, password);
    }

    private synchronized Connection getFreeConnection() throws SQLException
    {
        if(!freeConnections.isEmpty())
        {
            ConnectionImpl nextConnection = freeConnections.poll();
            if(needToReconnect(nextConnection))
            {
                if(!nextConnection.isValid(10))
                {
                    nextConnection.realClose();
                    nextConnection = createNewConnection();
                }
            }
            nextConnection.open();
            usedConnections.add(nextConnection);
            LOG.log(Level.FINE, "Current free connections in {0}: {1}", 
                        new Object[]{ config.getName(), freeConnections.size() });
            return nextConnection;
        }
        return null;
    }

    private synchronized Connection getNewConnection() throws SQLException
    {
        if(usedConnections.size() < config.getMaxConnections())
        {
            ConnectionImpl newConnection = createNewConnection();
            newConnection.open();
            usedConnections.add(newConnection);
            LOG.log(Level.FINE, "Current used connections in {0}: {1}", 
                        new Object[]{ config.getName(), usedConnections.size() });
            return newConnection;
        }
        return null;
    }

    private synchronized Connection waitFreeConnection() throws SQLException
    {
        Connection cnn = null;
        while(cnn == null)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
            cnn = getFreeConnection();
        }
        return cnn;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException
    {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException
    {
        logWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException
    {
        loginTimeout = seconds;
    }

    @Override
    public int getLoginTimeout() throws SQLException
    {
        return loginTimeout;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return false;
    }

    private ConnectionImpl createNewConnection() throws SQLException
    {
        LOG.log(Level.INFO, "Creating new connection for {0}.", config.getName());
        try
        {
            Class.forName(config.getDriver());
        }
        catch (ClassNotFoundException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        }
        Connection internalConnection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        return new ConnectionImpl(internalConnection, this);
    }

    private Connection createNewConnection(String user, String password) throws SQLException
    {
        try
        {
            Class.forName(config.getDriver());
        }
        catch (ClassNotFoundException ex)
        {
            throw new SQLException(ex.getMessage(), ex);
        }
        return DriverManager.getConnection(config.getUrl(), user, password);
    }

    protected synchronized void connectionClosed(ConnectionImpl closedConnection)
    {
        usedConnections.remove(closedConnection);
        freeConnections.add(closedConnection);
        LOG.log(Level.FINE, "Current free connections in {0}: {1}", 
                    new Object[]{ config.getName(), freeConnections.size() });
        notify();
    }

    protected synchronized void close() throws SQLException
    {
        closed = true;
        while(!usedConnections.isEmpty())
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                LOG.log(Level.FINE, e.getMessage(), e);
            }
        }
        for (ConnectionImpl freeConnection : freeConnections)
        {
            freeConnection.realClose();
        }
    }

    private void checkIdleConnections()
    {
        long idleTime = config.getIdleTime() * 1000;
        if( (System.currentTimeMillis() - lastCheck) > idleTime )
        {
            synchronized(this)
            {
                lastCheck = System.currentTimeMillis();
                List<ConnectionImpl> toRemove = new ArrayList<>();
                for (ConnectionImpl freeConnection : freeConnections)
                {
                    if( (System.currentTimeMillis() - freeConnection.getLastUse()) > idleTime )
                    {
                        try
                        {
                            int totalConnections = freeConnections.size() + usedConnections.size();
                            if(totalConnections - toRemove.size() <= config.getMinConnections())
                            {
                                break;
                            }
                            freeConnection.realClose();
                            toRemove.add(freeConnection);
                        }
                        catch (SQLException ex)
                        {
                            LOG.log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                }
                LOG.log(Level.FINE, "Removing {0} connections for {1}.", 
                            new Object[]{ toRemove.size(), config.getName() });
                freeConnections.removeAll(toRemove);
                LOG.log(Level.FINE, "Current total connections in {0}: {1}", 
                            new Object[]{ config.getName(), freeConnections.size() + usedConnections.size() });
            }
        }
    }

    private boolean needToReconnect(ConnectionImpl connection)
    {
        long reTime = config.getReconnectTime() * 1000;
        long timePass = System.currentTimeMillis() - connection.getLastUse();
        return reTime < timePass;
    }
}
