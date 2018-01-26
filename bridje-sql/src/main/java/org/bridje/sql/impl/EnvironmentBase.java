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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLResultParser;
import org.bridje.sql.SQLResultSet;
import org.bridje.sql.SQLStatement;
import org.bridje.sql.Query;
import org.bridje.sql.Schema;

abstract class EnvironmentBase implements SQLEnvironment
{
    private static final Logger LOG = Logger.getLogger(EnvironmentBase.class.getName());

    private final SQLDialect dialect;

    public EnvironmentBase(SQLDialect dialect)
    {
        this.dialect = dialect;
    }

    @Override
    public SQLDialect getDialect()
    {
        return dialect;
    }

    protected void fixSchema(Connection connection, Schema schema) throws SQLException
    {
        new SchemaFixer(connection, getDialect(), schema).doFix();
    }
    
    @Override
    public int update(Query query, Object... parameters) throws SQLException
    {
        return update(query.toStatement(getDialect(), parameters));
    }

    @Override
    public <T> List<T> fetchAll(Query query, SQLResultParser<T> parser, Object... parameters) throws SQLException
    {
        return fetchAll(query.toStatement(getDialect(), parameters), parser);
    }

    @Override
    public <T> T fetchOne(Query query, SQLResultParser<T> parser, Object... parameters) throws SQLException
    {
        return fetchOne(query.toStatement(getDialect(), parameters), parser);
    }

    protected int update(Connection cnn, SQLStatement sqlStmt) throws SQLException
    {
        if(LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, sqlStmt.getSQL());
        try(PreparedStatement stmt = prepareStatement(cnn, sqlStmt))
        {
            return stmt.executeUpdate();
        }
    }

    protected <T> List<T> fetchAll(Connection cnn, SQLStatement sqlStmt, SQLResultParser<T> parser) throws SQLException
    {
        if(LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, sqlStmt.getSQL());
        try(PreparedStatement stmt = prepareStatement(cnn, sqlStmt))
        {
            SQLResultSet rs;
            if(sqlStmt.isWithGeneratedKeys())
            {
                int value = stmt.executeUpdate();
                rs = new SQLResultSetImpl(stmt.getGeneratedKeys(), sqlStmt.getResultFields());
            }
            else
            {
                rs = new SQLResultSetImpl(stmt.executeQuery(), sqlStmt.getResultFields());
            }
            return fetchAll(rs, parser);
        }
    }

    protected <T> T fetchOne(Connection cnn, SQLStatement sqlStmt, SQLResultParser<T> parser) throws SQLException
    {
        LOG.log(Level.FINE, sqlStmt.getSQL());
        try(PreparedStatement stmt = prepareStatement(cnn, sqlStmt))
        {
            SQLResultSet rs;
            if(sqlStmt.isWithGeneratedKeys())
            {
                int value = stmt.executeUpdate();
                rs = new SQLResultSetImpl(stmt.getGeneratedKeys(), sqlStmt.getResultFields());
            }
            else
            {
                rs = new SQLResultSetImpl(stmt.executeQuery(), sqlStmt.getResultFields());
            }
            return fetchOne(rs, parser);
        }
    }

    protected PreparedStatement prepareStatement(Connection cnn, SQLStatement sqlStmt) throws SQLException
    {
        PreparedStatement stmt;
        if(sqlStmt.isWithGeneratedKeys())
        {
            stmt = cnn.prepareStatement(sqlStmt.getSQL(), Statement.RETURN_GENERATED_KEYS);
        }
        else
        {
            stmt = cnn.prepareStatement(sqlStmt.getSQL());
        }

        Object[] params = sqlStmt.getParameters();
        for (int i = 0; i < params.length; i++)
        {
            stmt.setObject(i+1, params[i]);
        }
        return stmt;
    }

    private <T> T fetchOne(SQLResultSet rs, SQLResultParser<T> parser) throws SQLException
    {
        if(rs.next())
        {
            return parser.parse(rs);
        }
        return null;
    }

    private <T> List<T> fetchAll(SQLResultSet rs, SQLResultParser<T> parser) throws SQLException
    {
        List<T> result = new ArrayList<>();
        while(rs.next())
        {
            result.add(parser.parse(rs));
        }
        return result;
    }
}
