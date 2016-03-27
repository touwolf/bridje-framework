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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.jdbc.JdbcService;
import org.bridje.jdbc.config.DataSourceConfig;
import org.bridje.jdbc.config.JdbcConfig;

/**
 *
 */
@Component
class JdbcServiceImpl implements JdbcService
{
    private static final Logger LOG = Logger.getLogger(JdbcServiceImpl.class.getName());

    @Inject
    private ConfigService cfgServ;
    
    private Map<String, DataSourceImpl> dsMap;
    
    @PostConstruct
    public void init()
    {
        try
        {
            dsMap = new ConcurrentHashMap<>();
            JdbcConfig jdbcCfg = cfgServ.findOrCreateConfig(JdbcConfig.class, new JdbcConfig());
            jdbcCfg.getDataSources().stream()
                    .forEach((config) ->dsMap.put(config.getName(), new DataSourceImpl(config)) );
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    @Override
    public DataSource getDataSource(String name)
    {
        return dsMap.get(name);
    }

    @Override
    public DataSource createDataSource(DataSourceConfig config)
    {
        return new DataSourceImpl(config);
    }

    @Override
    public void closeDataSource(DataSource dataSource) throws SQLException
    {
        if(dataSource instanceof DataSourceImpl)
        {
            ((DataSourceImpl) dataSource).close();
        }
        else
        {
            throw new IllegalArgumentException("Invalid Data Source.");
        }
    }

    @Override
    public void closeAllDataSource()
    {
        for (Map.Entry<String, DataSourceImpl> entry : dsMap.entrySet())
        {
            try
            {
                DataSourceImpl dataSource = entry.getValue();
                dataSource.close();
            }
            catch (SQLException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        dsMap.clear();
    }
}
