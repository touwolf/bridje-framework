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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bridje.orm.Condition;
import org.bridje.orm.OrderBy;
import org.bridje.orm.Query;
import org.bridje.orm.TableRelationColumn;
import org.bridje.orm.impl.sql.SelectBuilder;

/**
 *
 * @param <T>
 */
class QueryImpl<T> extends AbstractQuery<T> implements Query<T>
{
    private final TableImpl<T> table;
    
    private final EntityContextImpl ctx;

    private Condition condition;
    
    private OrderBy[] orderBy;
    
    private int page;
    
    private int pageSize;
    
    public QueryImpl(EntityContextImpl ctx, TableImpl<T> table)
    {
        this.table = table;
        this.ctx = ctx;
    }
    
    @Override
    public void paging(int page, int size)
    {
        this.page = page;
        this.page = pageSize;
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

    @Override
    protected SelectBuilder createQuery(String fields, List<Object> parameters)
    {
        SelectBuilder qb = new SelectBuilder();
        qb.select(fields)
            .from(ctx.getDialect().identifier(table.getName()));
        if(condition != null)
        {
            qb.where(condition.writeSQL(parameters, ctx));
        }
        if(orderBy != null)
        {
            qb.orderBy(Arrays
                    .asList(orderBy).stream()
                    .map((ob) -> table.buildOrderBy(ob, parameters, ctx) )
                    .collect(Collectors.joining(", ")));
        }
        return qb;
    }

    @Override
    public Condition getCondition()
    {
        return condition;
    }

    @Override
    public OrderBy[] getOrderBy()
    {
        return orderBy;
    }

    @Override
    public int getPage()
    {
        return page;
    }

    @Override
    public int getPageSize()
    {
        return pageSize;
    }

    @Override
    public <R> Query<R> join(TableRelationColumn<T, R> relation)
    {
        return new JoinQueryImpl<>(this, (TableRelationColumnImpl<T, R>)relation);
    }

    @Override
    protected TableImpl<T> getTable()
    {
        return table;
    }

    @Override
    protected EntityContextImpl getCtx()
    {
        return ctx;
    }
}
