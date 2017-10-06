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

package org.bridje.orm.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.orm.EnvironmentBuilder;
import org.bridje.orm.ORMEnvironment;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLService;

class EnvironmentBuilderImpl implements EnvironmentBuilder
{
    private final Map<Class<?>, SQLEnvironment> sqlEnvironments;

    private final SQLService sqlServ;

    public EnvironmentBuilderImpl()
    {
        sqlEnvironments = new HashMap<>();
        sqlServ = Ioc.context().find(SQLService.class);
    }

    @Override
    public EnvironmentBuilder model(Class<?> modelClass, SQLEnvironment env)
    {
        if(env == null) throw new IllegalArgumentException("The SQL environment must not be null.");
        sqlEnvironments.put(modelClass, env);
        return this;
    }

    @Override
    public EnvironmentBuilder model(Class<?> modelClass, DataSource ds) throws SQLException
    {
        if(ds == null) throw new IllegalArgumentException("The DataSource must not be null.");
        sqlEnvironments.put(modelClass, createSQLEnvironment(ds));
        return this;
    }

    @Override
    public EnvironmentBuilder model(Class<?> modelClass, Connection conn) throws SQLException
    {
        if(conn == null) throw new IllegalArgumentException("The Connection must not be null.");
        sqlEnvironments.put(modelClass, createSQLEnvironment(conn));
        return this;
    }

    @Override
    public EnvironmentBuilder model(Class<?> modelClass, String dsName) throws SQLException
    {
        if(dsName == null) throw new IllegalArgumentException("The name of the DataSource must not be null.");
        sqlEnvironments.put(modelClass, createSQLEnvironment(dsName));
        return this;
    }

    public SQLEnvironment get(Class<?> modelClass)
    {
        return sqlEnvironments.get(modelClass);
    }

    private SQLEnvironment createSQLEnvironment(DataSource ds) throws SQLException
    {
        return sqlServ.createEnvironment(ds);
    }

    private SQLEnvironment createSQLEnvironment(Connection conn) throws SQLException
    {
        return sqlServ.createEnvironment(conn);
    }

    private SQLEnvironment createSQLEnvironment(String dsName) throws SQLException
    {
        return sqlServ.createEnvironment(dsName);
    }

    public boolean contains(Class<?> type)
    {
        return sqlEnvironments.containsKey(type);
    }

    @Override
    public ORMEnvironment build()
    {
        return new ORMEnvironmentImpl(this);
    }
}
