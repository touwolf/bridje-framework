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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.orm.Condition;
import org.bridje.orm.Query;

/**
 *
 * @param <T>
 */
class QueryImpl<T> implements Query<T>
{
    private static final Logger LOG = Logger.getLogger(QueryImpl.class.getName());

    private final EntityInf<T> entityInf;
    
    private final EntityContextImpl entCtxImpl;

    private Condition condition;
    
    public QueryImpl(EntityContextImpl entCtxImpl, EntityInf<T> entityInf)
    {
        this.entityInf = entityInf;
        this.entCtxImpl = entCtxImpl;
    }
    
    @Override
    public List<T> fetchAll()
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            return entCtxImpl.doQuery(
                    entityInf.buildSelectQuery(condition.writeString(parameters)), 
                    (rs) -> entityInf.parseAllEntitys(rs, entCtxImpl), 
                    parameters.toArray());
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<T> fetchAll(int page, int size)
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            int index = ((page - 1) * size);
            return entCtxImpl.doQuery(
                    entityInf.buildSelectQuery(condition.writeString(parameters), index, size),
                    (rs) -> entityInf.parseAllEntitys(rs, entCtxImpl),
                    parameters);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public T fetchOne()
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            return entCtxImpl.doQuery(
                    entityInf.buildSelectQuery(condition.writeString(parameters), 1, 1), 
                    (rs) -> entityInf.parseEntity(rs, entCtxImpl),
                    parameters);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public long count()
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            return entCtxImpl.doQuery(
                    entityInf.buildCountQuery(condition.writeString(parameters)), 
                    (rs) -> entityInf.parseCount(rs), 
                    parameters);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public boolean exists()
    {
        return count() > 0;
    }

    @Override
    public Query<T> by(Condition condition)
    {
        this.condition = condition;
        return this;
    }
}
