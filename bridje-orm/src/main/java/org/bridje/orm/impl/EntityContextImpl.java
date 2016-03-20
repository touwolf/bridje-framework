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

package org.bridje.orm.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.orm.EntityContext;
import org.bridje.orm.Table;
import org.bridje.orm.Query;

/**
 * 
 */
class EntityContextImpl implements EntityContext
{
    private static final Logger LOG = Logger.getLogger(EntityContextImpl.class.getName());

    private final DataSource ds;

    private final OrmMetaInfService metainf;

    public EntityContextImpl(DataSource ds)
    {
        this.ds = ds;
        this.metainf = Ioc.context().find(OrmMetaInfService.class);
    }

    @Override
    public <T> void fixTable(Class<T> entityClass)
    {
        try
        {
            EntityInf<T> entityInf = findEntityInf(entityClass);
            if(tableExists(entityInf))
            {
                fixColumns(entityInf);
            }
            else
            {
                createTable(entityInf);
            }
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public <T> T find(Class<T> entityClass, Object id)
    {
        try
        {
            EntityInf<T> entityInf = findEntityInf(entityClass);
            QueryBuilder qb = new QueryBuilder();
            qb.select(entityInf.allFieldsSelect())
                .from(entityInf.getTableName())
                .where(entityInf.buildIdCondition())
                .limit(0, 1);

            return doQuery(qb.toString(), (rs) -> entityInf.parseEntity(rs, this), id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> T refresh(T entity)
    {
        try
        {
            EntityInf<T> entityInf = findEntityInf(entity);
            QueryBuilder qb = new QueryBuilder();
            qb.select(entityInf.allFieldsSelect())
                .from(entityInf.getTableName())
                .where(entityInf.buildIdCondition())
                .limit(0, 1);
            Object id = entityInf.getKeyField().getValue(entity);
            return doQuery(qb.toString(), (rs) -> entityInf.parseEntity(entity, rs, this), id);
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> T insert(T entity)
    {
        try
        {
            EntityInf<T> entityInf = findEntityInf(entity);
            doUpdate(entityInf.buildInsertQuery(entity), entityInf.buildInsertParameters(entity));
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return entity;
    }

    @Override
    public <T> T delete(T entity)
    {
        try
        {
            EntityInf<T> entityInf = findEntityInf(entity);
            doUpdate(entityInf.buildDeleteQuery(entity), entityInf.findKeyValue(entity));
        }
        catch (SQLException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return entity;
    }

    public <T> T doQuery(String query, QueryConsumer<T> consumer, Object... parameters) throws SQLException
    {
        LOG.log(Level.INFO, query);
        T result = null;
        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            for (int i = 0; i < parameters.length; i++)
            {
                setParam(stmt, parameters[i], i + 1);
            }
            try (ResultSet resultSet = stmt.executeQuery())
            {
                result = consumer.parse(resultSet);
            }
        }
        return result;
    }
    
    public int doUpdate(String query, Object... parameters) throws SQLException
    {
        LOG.log(Level.INFO, query);
        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            for (int i = 0; i < parameters.length; i++)
            {
                setParam(stmt, parameters[i], i + 1);
            }
            return stmt.executeUpdate();
        }
    }

    private <T> EntityInf<T> findEntityInf(Class<T> entityClass)
    {
        return metainf.findEntityInf(entityClass);
    }
    
    private <T> EntityInf<T> findEntityInf(T entity)
    {
        return findEntityInf((Class<T>)entity.getClass());
    }

    private <T> boolean tableExists(EntityInf<T> entityInf) throws SQLException
    {
        try (Connection conn = ds.getConnection())
        {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, null, entityInf.getTableName(), null))
            {
                if(resultSet.next())
                {
                    return true;
                }
            }
        }
        return false;
    }

    private <T> void fixColumns(EntityInf<T> entityInf)
    {
        entityInf.buildCreateColumnQuery();
    }

    private <T> void createTable(EntityInf<T> entityInf) throws SQLException
    {
        String query = entityInf.buildCreateTableQuery();
        doUpdate(query);
    }

    @Override
    public <T> Query<T> query(Table<T> entityTable)
    {
        EntityInf<T> entityInf = findEntityInf(entityTable.getEntityClass());
        return new QueryImpl<>(this, entityInf);
    }

    private void setParam(PreparedStatement stmt, Object parameter, int index) throws SQLException
    {
        if(parameter != null && parameter instanceof Character) 
        {
            stmt.setString(index, parameter.toString());
        }
        else
        {
            stmt.setObject(index, parameter);
        }
    }
}
