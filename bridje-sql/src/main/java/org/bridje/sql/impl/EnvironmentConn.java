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
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLResultParser;
import org.bridje.sql.SQLStatement;
import org.bridje.sql.Schema;

class EnvironmentConn extends EnvironmentBase implements SQLEnvironment
{
    private final Connection connection;

    public EnvironmentConn(Connection connection, SQLDialect dialect)
    {
        super(dialect);
        this.connection = connection;
    }

    @Override
    public void fixSchema(Schema schema) throws SQLException
    {
        fixSchema(connection, schema);
    }

    @Override
    public void clearSchema(Schema schema) throws SQLException
    {
        clearSchema(connection, schema);
    }

    @Override
    public void begin() throws SQLException
    {
        connection.setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException
    {
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException
    {
        connection.rollback();
    }

    @Override
    public void close() throws Exception
    {
        connection.close();
    }

    @Override
    public int update(SQLStatement stmt) throws SQLException
    {
        return update(connection, stmt);
    }

    @Override
    public <T> List<T> fetchAll(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException
    {
        return fetchAll(connection, stmt, parser);
    }

    @Override
    public <T> T fetchOne(SQLStatement stmt, SQLResultParser<T> parser) throws SQLException
    {
        return fetchOne(connection, stmt, parser);
    }
}
