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

package org.bridje.core.impl.dbc;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.bridje.core.dbc.DataSourceConfig;
import org.bridje.core.dbc.DbcConfigProvider;
import org.bridje.core.dbc.DbcDataSourceFactory;
import org.bridje.core.dbc.DbcService;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Inject;

/**
 *
 */
@Component
class DbcServiceImpl implements DbcService
{
    @Inject
    private DbcConfigProvider configProv;
    
    @Inject
    private DbcDataSourceFactory fact;
    
    private Map<String, DataSource> dataSourcesMap;

    @Override
    public DataSource getDataSource(String dsName)
    {
        DataSource ds = getDataSourcesMap().get(dsName);
        if(ds == null)
        {
            DataSourceConfig dsCfg = findDataSourceConfig(dsName);
            if(dsCfg != null)
            {
                ds = createDataSource(dsCfg);
                if(ds != null)
                {
                    dataSourcesMap.put(dsName, ds);
                }
            }
        }
        return ds;
    }

    @Override
    public DataSourceConfig findDataSourceConfig(String dsName)
    {
        return configProv.findDataSourceConfig(dsName);
    }

    @Override
    public DataSource createDataSource(DataSourceConfig config)
    {
        return fact.createDataSource(config);
    }

    public Map<String, DataSource> getDataSourcesMap()
    {
        if(dataSourcesMap == null)
        {
            dataSourcesMap = new HashMap<>();
        }
        return dataSourcesMap;
    }
}
