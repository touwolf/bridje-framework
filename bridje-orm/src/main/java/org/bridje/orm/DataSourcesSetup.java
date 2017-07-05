/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.orm;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.jdbc.JdbcService;

public class DataSourcesSetup
{
    private final Map<Class<? extends OrmModel>, DataSource> dataSources;

    public DataSourcesSetup()
    {
        dataSources = new HashMap<>();
    }
    
    public void setDataSource(Class<? extends OrmModel> modelClass, DataSource ds)
    {
        dataSources.put(modelClass, ds);
    }

    public void setDataSource(Class<? extends OrmModel> modelClass, String dsName)
    {
        DataSource ds = Ioc.context().find(JdbcService.class).getDataSource(dsName);
        dataSources.put(modelClass, ds);
    }

    public DataSource getDataSource(Class<? extends OrmModel> modelClass)
    {
        return dataSources.get(modelClass);
    }
}
