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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.jdbc.JdbcService;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLService;

public class ORMConfig
{
    private final Map<Class<?>, SQLEnvironment> sqlEnvironments;

    private final SQLService sqlServ;

    public ORMConfig()
    {
        sqlEnvironments = new HashMap<>();
        sqlServ = Ioc.context().find(SQLService.class);
    }

    public void put(Class<?> modelClass, SQLEnvironment env)
    {
        if(env == null) throw new IllegalArgumentException("The SQL environment must not be null.");
        sqlEnvironments.put(modelClass, env);
    }

    public void put(Class<?> modelClass, DataSource ds) throws SQLException
    {
        if(ds == null) throw new IllegalArgumentException("The DataSource must not be null.");
        sqlEnvironments.put(modelClass, createSQLEnvironment(ds));
    }

    public void put(Class<?> modelClass, Connection conn) throws SQLException
    {
        if(conn == null) throw new IllegalArgumentException("The Connection must not be null.");
        sqlEnvironments.put(modelClass, createSQLEnvironment(conn));
    }

    public void put(Class<?> modelClass, String dsName) throws SQLException
    {
        if(dsName == null) throw new IllegalArgumentException("The name of the DataSource must not be null.");
        sqlEnvironments.put(modelClass, createSQLEnvironment(dsName));
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
}
