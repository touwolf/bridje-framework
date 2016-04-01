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

package org.bridje.orm.impl.core;

import java.sql.SQLException;
import org.bridje.orm.impl.sql.SelectBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bridje.orm.Column;
import org.bridje.orm.ColumnNameFinder;
import org.bridje.orm.Condition;
import org.bridje.orm.OrderBy;
import org.bridje.orm.Query;

/**
 *
 * @param <T>
 */
class QueryImpl<T> implements Query<T>, ColumnNameFinder
{
    private static final Logger LOG = Logger.getLogger(QueryImpl.class.getName());

    private final EntityInf<T> entityInf;
    
    private final EntityContextImpl entCtxImpl;

    private Condition condition;
    
    private OrderBy[] orderBy;
    
    private int page;
    
    private int pageSize;
    
    public QueryImpl(EntityContextImpl entCtxImpl, EntityInf<T> entityInf)
    {
        this.entityInf = entityInf;
        this.entCtxImpl = entCtxImpl;
    }
    
    @Override
    public void paging(int page, int size)
    {
        this.page = page;
        this.page = pageSize;
    }

    @Override
    public List<T> fetchAll()
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            SelectBuilder qb = createQuery(entityInf.allFieldsCommaSep(), parameters);
            if(page > 0)
            {
                int index = ((page - 1) * pageSize);
                qb.limit(index, pageSize);
            }
            return entCtxImpl.doQuery(qb.toString(), (rs) -> entityInf.parseAllEntitys(rs, entCtxImpl), parameters.toArray());
        }
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <C> List<C> fetchAll(Column<T, C> column)
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            if(column.getParameters() != null)
            {
                parameters.addAll(column.getParameters());
            }
            SelectBuilder qb = createQuery(findColumnName(column), parameters);
            if(page > 0)
            {
                int index = ((page - 1) * pageSize);
                qb.limit(index, pageSize);
            }
            return entCtxImpl.doQuery(qb.toString(), (rs) -> entityInf.parseAllColumns(1, column, rs, entCtxImpl), parameters.toArray());
        }
        catch (SQLException e)
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
            SelectBuilder qb = createQuery(entityInf.allFieldsCommaSep(), parameters);
            qb.limit(0, 1);
            return entCtxImpl.doQuery(qb.toString(), (rs) -> entityInf.parseEntity(rs, entCtxImpl), parameters.toArray());
        }
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public <C> C fetchOne(Column<T, C> column)
    {
        try
        {
            List<Object> parameters = new ArrayList<>();
            if(column.getParameters() != null)
            {
                parameters.addAll(column.getParameters());
            }
            SelectBuilder qb = createQuery(findColumnName(column), parameters);
            qb.limit(0, 1);
            return entCtxImpl.doQuery(qb.toString(), (rs) -> entityInf.parseColumn(1, column, rs, entCtxImpl),parameters.toArray());
        }
        catch (SQLException e)
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
            SelectBuilder qb = createQuery("COUNT(*)", parameters);
            return entCtxImpl.doQuery(qb.toString(), (rs) -> entityInf.parseCount(rs), parameters.toArray());
        }
        catch (SQLException e)
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
    public Query<T> where(Condition condition)
    {
        this.condition = condition;
        return this;
    }

    @Override
    public Query<T> orderBy(OrderBy... statements)
    {
        orderBy = statements;
        return this;
    }

    private SelectBuilder createQuery(String fields, List<Object> parameters)
    {
        SelectBuilder qb = new SelectBuilder();
        qb.select(fields)
            .from(entityInf.getTableName());
        if(condition != null)
        {
            qb.where(condition.writeString(parameters, this));
        }
        if(orderBy != null)
        {
            qb.orderBy(Arrays
                    .asList(orderBy).stream()
                    .map((ob) -> entityInf.buildOrderBy(ob))
                    .collect(Collectors.joining(", ")));
        }
        return qb;
    }

    @Override
    public String findColumnName(Column column)
    {
        EntityInf<Object> eInf = entCtxImpl.getMetainf().findEntityInf(column.getTable().getEntityClass());
        String columnName = eInf.findColumnName(column);
        if(column.getFunction() != null)
        {
            return String.format(column.getFunction(), columnName);
        }
        return columnName;
    }
}
