
package org.bridje.core.impl.dbc.fact;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.AbandonedConfig;
import org.apache.commons.dbcp.AbandonedObjectPool;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.bridje.core.dbc.DataSourceConfig;

public class DataSourceImpl implements DataSource, ConnectionFactory
{
    private static final Logger LOG = Logger.getLogger(DataSourceImpl.class.getName());

    private final DataSource ds;

    private final DataSourceConfig config;

    private AbandonedObjectPool connectionPool;

    public DataSourceImpl(DataSourceConfig config)
    {
        this.config = config;
        ds = createDataSource();
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        Connection cn = ds.getConnection();
        try
        {
            cn.setAutoCommit(true);
            cn.setReadOnly(false);
        } catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return cn;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException
    {
        Connection cn = ds.getConnection(username, password);
        try
        {
            cn.setAutoCommit(true);
            cn.setReadOnly(false);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return cn;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException
    {
        return ds.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException
    {
        ds.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException
    {
        ds.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException
    {
        return ds.getLoginTimeout();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        if (ds == null)
        {
            throw new SQLFeatureNotSupportedException();
        }
        return ds.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        return ds.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return ds.isWrapperFor(iface);
    }

    @Override
    public Connection createConnection() throws SQLException
    {
        return DriverManager.getConnection(config.getHost(), config.getUser(), config.getPass());
    }

    public Connection createRawConnection() throws SQLException
    {
        return DriverManager.getConnection(config.getHost(), config.getUser(), config.getPass());
    }

    private DataSource createDataSource()
    {
        AbandonedConfig abandonedConfig = new AbandonedConfig();
        abandonedConfig.setLogAbandoned(config.isLogAbandoned());
        abandonedConfig.setRemoveAbandoned(config.isRemoveAbandoned());
        abandonedConfig.setRemoveAbandonedTimeout(config.getRemoveAbandonedTimeout());

        connectionPool = new AbandonedObjectPool(null, abandonedConfig);

        // Numero max de conexiones del pool (default 8)
        connectionPool.setMaxActive(config.getMaxActive());
        // Tiempo max de espera para obtener una conexion (default ilimitado)
        connectionPool.setMaxWait(config.getMaxWait());
        // Tiempo min antes de eliminar una conexion inactiva
        connectionPool.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());
        // No de pruebas para eliminar conexiones por corridas
        connectionPool.setNumTestsPerEvictionRun(config.getNumTestsPerEvictionRun());
        // Tiempo entre corridas
        connectionPool.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
        // Probar al obtener una conexion
        connectionPool.setTestOnBorrow(config.isTestOnBorrow());
        // Probar al devolver una conexion
        connectionPool.setTestOnReturn(config.isTestOnReturn());
        // Probar durante que una conexion este inactiva
        connectionPool.setTestWhileIdle(config.isTestWhileIdle());
        // Tiempo min antes de eliminar una conexion inactiva
        connectionPool.setSoftMinEvictableIdleTimeMillis(config.getSoftMinEvictableIdleTimeMillis());

        KeyedObjectPoolFactory keyedObjectPoolFactory = new GenericKeyedObjectPoolFactory(null);

        PoolableConnectionFactory poolableConnectionFactory
                = new PoolableConnectionFactory(this, connectionPool,
                        keyedObjectPoolFactory, null, false, true, abandonedConfig);

        connectionPool.setFactory(poolableConnectionFactory);

        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

        return dataSource;
    }

    public DataSourceConfig getConfig()
    {
        return config;
    }
}
