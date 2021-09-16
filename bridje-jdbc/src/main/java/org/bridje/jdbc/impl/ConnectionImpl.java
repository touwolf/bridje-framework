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

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

class ConnectionImpl implements Connection
{
    private final Connection connection;

    private final DataSourceImpl parentDataSource;

    private long lastUse;

    private boolean closed = true;

    public ConnectionImpl(Connection connection, DataSourceImpl parentDataSource)
    {
        this.connection = connection;
        this.parentDataSource = parentDataSource;
    }

    protected void open()
    {
        closed = false;
        lastUse = System.currentTimeMillis();
    }

    public long getLastUse()
    {
        return lastUse;
    }

    @Override
    public Statement createStatement() throws SQLException
    {
        checkClosed();
        return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException
    {
        checkClosed();
        return connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException
    {
        checkClosed();
        return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException
    {
        checkClosed();
        return connection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException
    {
        checkClosed();
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException
    {
        checkClosed();
        return connection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException
    {
        checkClosed();
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException
    {
        checkClosed();
        connection.rollback();
    }

    @Override
    public void close() throws SQLException
    {
        closed = true;
        parentDataSource.connectionClosed(this);
    }

    @Override
    public boolean isClosed() throws SQLException
    {
        return closed || connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException
    {
        checkClosed();
        return connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException
    {
        checkClosed();
        connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException
    {
        checkClosed();
        return connection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException
    {
        checkClosed();
        connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException
    {
        checkClosed();
        return connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException
    {
        checkClosed();
        connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException
    {
        checkClosed();
        return connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException
    {
        checkClosed();
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException
    {
        checkClosed();
        connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
    {
        checkClosed();
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        checkClosed();
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        checkClosed();
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException
    {
        checkClosed();
        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException
    {
        checkClosed();
        connection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException
    {
        checkClosed();
        connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException
    {
        checkClosed();
        return connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException
    {
        checkClosed();
        return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException
    {
        checkClosed();
        return connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException
    {
        checkClosed();
        connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {
        checkClosed();
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkClosed();
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkClosed();
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkClosed();
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
        checkClosed();
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
    {
        checkClosed();
        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
    {
        checkClosed();
        return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException
    {
        checkClosed();
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException
    {
        checkClosed();
        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException
    {
        checkClosed();
        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException
    {
        checkClosed();
        return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException
    {
        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException
    {
        connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException
    {
        connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException
    {
        checkClosed();
        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException
    {
        checkClosed();
        return connection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException
    {
        checkClosed();
        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException
    {
        checkClosed();
        return connection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException
    {
        checkClosed();
        connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException
    {
        checkClosed();
        return connection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException
    {
        checkClosed();
        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
    {
        checkClosed();
        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException
    {
        checkClosed();
        return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        checkClosed();
        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        checkClosed();
        return connection.isWrapperFor(iface);
    }

    protected void realClose() throws SQLException
    {
        connection.close();
    }

    private void checkClosed() throws SQLException
    {
        if(closed)
        {
            throw new SQLException("The connection is closed.");
        }
    }
}
