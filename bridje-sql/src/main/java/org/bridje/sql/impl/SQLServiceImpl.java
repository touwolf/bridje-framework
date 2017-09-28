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

package org.bridje.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.jdbc.JdbcService;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLService;

@Component
class SQLServiceImpl implements SQLService
{
    @Inject
    private SQLDialect[] dialects;

    @Inject
    private JdbcService jdbcServ;

    @Override
    public SQLEnvironment createEnvironment(DataSource dataSource) throws SQLException
    {
        try(Connection conn = dataSource.getConnection())
        {
            SQLDialect dialect = findDialect(conn);
            return new EnvironmentDs(dataSource, dialect);
        }
    }

    @Override
    public SQLEnvironment createEnvironment(Connection connection) throws SQLException
    {
        SQLDialect dialect = findDialect(connection);
        return new EnvironmentConn(connection, dialect);
    }

    @Override
    public SQLEnvironment createEnvironment(String dataSourceName) throws SQLException
    {
        return createEnvironment(jdbcServ.getDataSource(dataSourceName));
    }

    private SQLDialect findDialect(Connection connection)
    {
        if(connection != null)
        {
            for (SQLDialect d : dialects)
            {
                if(d.canHandle(connection))
                {
                    return d;
                }
            }
        }
        return null;
    }
}
