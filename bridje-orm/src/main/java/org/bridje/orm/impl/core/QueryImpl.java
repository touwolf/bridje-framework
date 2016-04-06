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
import java.util.stream.Collectors;
import org.bridje.orm.Column;
import org.bridje.orm.ColumnNameFinder;
import org.bridje.orm.Condition;
import org.bridje.orm.FunctionColumn;
import org.bridje.orm.OrderBy;
import org.bridje.orm.Query;
import org.bridje.orm.RelationColumn;

/**
 *
 * @param <T>
 */
class QueryImpl<T> implements Query<T>, ColumnNameFinder
{
    private final EntityInf<T> entityInf;
    
    private final EntityContextImpl ctx;

    private Condition condition;
    
    private OrderBy[] orderBy;
    
    private int page;
    
    private int pageSize;
    
    public QueryImpl(EntityContextImpl ctx, EntityInf<T> entityInf)
    {
        this.entityInf = entityInf;
        this.ctx = ctx;
    }
    
    @Override
    public void paging(int page, int size)
    {
        this.page = page;
        this.page = pageSize;
    }

    @Override
    public List<T> fetchAll() throws SQLException
    {
        List<Object> params = new ArrayList<>();
        SelectBuilder qb = createQuery(
                    entityInf.allFieldsCommaSep(entityInf.getTableName() + "."), 
                    params);
        if(page > 0)
        {
            int index = ((page - 1) * pageSize);
            qb.limit(index, pageSize);
        }
        return ctx.doQuery(qb.toString(), 
                        (rs) -> entityInf.parseAll(rs, ctx), 
                        params.toArray());
    }

    @Override
    public <C> List<C> fetchAll(Column<T, C> column) throws SQLException
    {
        List<Object> params = new ArrayList<>();
        if(column instanceof FunctionColumn && ((FunctionColumn)column).getParameters() != null)
        {
            params.addAll(((FunctionColumn)column).getParameters());
        }
        SelectBuilder qb = createQuery(findColumnName(column), params);
        if(page > 0)
        {
            int index = ((page - 1) * pageSize);
            qb.limit(index, pageSize);
        }
        return ctx.doQuery(qb.toString(), 
                (rs) -> entityInf.parseAll(1, column, rs, ctx), 
                params.toArray());
    }

    @Override
    public T fetchOne() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        SelectBuilder qb = createQuery(
                        entityInf.allFieldsCommaSep(entityInf.getTableName() + "."), 
                        parameters);
        qb.limit(0, 1);
        return ctx.doQuery(qb.toString(), 
                    (rs) -> entityInf.parse(rs, ctx), 
                    parameters.toArray());
    }

    @Override
    public <C> C fetchOne(Column<T, C> column) throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        if(column instanceof FunctionColumn && ((FunctionColumn)column).getParameters() != null)
        {
            parameters.addAll(((FunctionColumn)column).getParameters());
        }
        SelectBuilder qb = createQuery(findColumnName(column), parameters);
        qb.limit(0, 1);
        return ctx.doQuery(qb.toString(), 
                    (rs) -> entityInf.parse(1, column, rs, ctx), 
                    parameters.toArray());
    }

    @Override
    public long count() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        SelectBuilder qb = createQuery("COUNT(*)", parameters);
        return ctx.doQuery(qb.toString(), (rs) -> entityInf.parseCount(rs), parameters.toArray());
    }

    @Override
    public boolean exists() throws SQLException
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
    public <R> Query<R> join(RelationColumn<T, R> relation)
    {
        RelationInf<T, R> relationInf = entityInf.findRelationInfo(relation);
        return new JoinQueryImpl<R, T>(this, relationInf);
    }
    
    @Override
    public Query<T> orderBy(OrderBy... statements)
    {
        orderBy = statements;
        return this;
    }

    protected SelectBuilder createQuery(String fields, List<Object> parameters)
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
                    .map((ob) -> entityInf.buildOrderBy(ob, entityInf.getTableName() + "."))
                    .collect(Collectors.joining(", ")));
        }
        return qb;
    }

    @Override
    public String findColumnName(Column column)
    {
        EntityInf<Object> eInf = ctx.getMetainf().findEntityInf(column.getTable().getEntityClass());
        String columnName = eInf.getTableName() + "." + eInf.findColumnName(column);
        if(column instanceof FunctionColumn && ((FunctionColumn)column).getFunction() != null)
        {
            return String.format(((FunctionColumn)column).getFunction(), columnName);
        }
        return columnName;
    }

    public EntityInf<T> getEntityInf()
    {
        return entityInf;
    }

    public EntityContextImpl getCtx()
    {
        return ctx;
    }

    public Condition getCondition()
    {
        return condition;
    }

    public OrderBy[] getOrderBy()
    {
        return orderBy;
    }

    public int getPage()
    {
        return page;
    }

    public int getPageSize()
    {
        return pageSize;
    }
}
