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
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.jdbc.config.DataSourceConfig;

/**
 *
 */
class DataSourceImpl implements DataSource
{
    private static final Logger LOG = Logger.getLogger(DataSourceImpl.class.getName());

    private final Deque<ConnectionImpl> freeConnections = new ConcurrentLinkedDeque<>();

    private final Deque<ConnectionImpl> usedConnections = new ConcurrentLinkedDeque<>();

    private final DataSourceConfig config;
    
    private PrintWriter logWriter;
    
    private int loginTimeout;
    
    public DataSourceImpl(DataSourceConfig config)
    {
        this.config = config;
    }
    
    @Override
    public Connection getConnection() throws SQLException
    {
        if(freeConnections.isEmpty())
        {
            ConnectionImpl newConnection = createNewConnection();
            usedConnections.add(newConnection);
            return newConnection;
        }
        else
        {
            ConnectionImpl nextConnection = freeConnections.poll();
            usedConnections.add(nextConnection);
            return nextConnection;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException
    {
        throw new UnsupportedOperationException("Connect with diferent username and password it´s not supported");
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
        try
        {
            Class.forName(config.getDriver());
        }
        catch (ClassNotFoundException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        Connection internalConnection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        return new ConnectionImpl(internalConnection, this);
    }

    protected void connectionClosed(ConnectionImpl closedConnection)
    {
        usedConnections.remove(closedConnection);
        freeConnections.add(closedConnection);
    }

    protected void close() throws SQLException
    {
        while(!usedConnections.isEmpty())
        {
            try
            {
                Thread.sleep(1000l);
            }
            catch (InterruptedException e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        for (ConnectionImpl freeConnection : freeConnections)
        {
            freeConnection.realClose();
        }
    }
}
