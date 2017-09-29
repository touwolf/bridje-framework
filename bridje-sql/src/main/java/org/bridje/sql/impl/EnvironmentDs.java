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
import java.util.List;
import javax.sql.DataSource;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLResultParser;
import org.bridje.sql.SQLStatement;
import org.bridje.sql.Table;

class EnvironmentDs extends EnvironmentBase implements SQLEnvironment
{
    private final DataSource dataSource;

    private EnvironmentConn connEnv;

    public EnvironmentDs(DataSource dataSource, SQLDialect dialect)
    {
        super(dialect);
        this.dataSource = dataSource;
    }

    @Override
    public void fixTable(Table... tables) throws SQLException
    {
        if(connEnv != null)
        {
            connEnv.fixTable(tables);
        }
        else
        {
            try(Connection connection = dataSource.getConnection())
            {
                for (Table table : tables)
                {
                    fixTable(connection, table);
                }
            }
        }
    }

    @Override
    public void begin() throws SQLException
    {
        if(connEnv == null) connEnv = new EnvironmentConn(dataSource.getConnection(), getDialect());
    }

    @Override
    public void commit() throws SQLException
    {
        if(connEnv != null) connEnv.commit();
    }

    @Override
    public void rollback() throws SQLException
    {
        if(connEnv != null) connEnv.rollback();
    }

    @Override
    public void close() throws Exception
    {
        if(connEnv != null) connEnv.close();
    }

    @Override
    public int update(SQLStatement stmt) throws SQLException
    {
        if(connEnv != null)
        {
            return connEnv.update(stmt);
        }
        else
        {
            try(Connection connection = dataSource.getConnection())
            {
                return update(connection, stmt);
            }
        }
    }

    @Override
    public <T> List<T> fetchAll(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException
    {
        if(connEnv != null)
        {
            return connEnv.fetchAll(stmt, parser);
        }
        else
        {
            try(Connection connection = dataSource.getConnection())
            {
                return fetchAll(connection, stmt, parser);
            }
        }
    }

    @Override
    public <T> T fetchOne(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException
    {
        if(connEnv != null)
        {
            return connEnv.fetchOne(stmt, parser);
        }
        else
        {
            try(Connection connection = dataSource.getConnection())
            {
                return fetchOne(connection, stmt, parser);
            }
        }
    }
}
