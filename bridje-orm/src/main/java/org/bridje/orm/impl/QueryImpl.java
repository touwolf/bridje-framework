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

import java.sql.SQLException;
import org.bridje.orm.impl.sql.SelectBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bridje.orm.Column;
import org.bridje.orm.Condition;
import org.bridje.orm.OrderBy;
import org.bridje.orm.Query;
import org.bridje.orm.TableColumn;

/**
 *
 * @param <T>
 */
class QueryImpl<T> implements Query<T>
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
    public List<T> fetchAll() throws SQLException
    {
        List<Object> params = new ArrayList<>();
        SelectBuilder qb = createQuery(
                    table.allFieldsCommaSep(table.getName() + "."), 
                    params);
        if(page > 0)
        {
            int index = ((page - 1) * pageSize);
            qb.limit(index, pageSize);
        }
        return ctx.doQuery(qb.toString(), 
                        (rs) -> table.parseAll(rs, ctx), 
                        params.toArray());
    }

    @Override
    public <C> List<C> fetchAll(Column<C> column) throws SQLException
    {
        List<Object> params = new ArrayList<>();
        SelectBuilder qb = createQuery(column.getExpression(), params);
        if(page > 0)
        {
            int index = ((page - 1) * pageSize);
            qb.limit(index, pageSize);
        }
        return ctx.doQuery(qb.toString(), 
                (rs) -> table.parseAll(1, column, rs, ctx), 
                params.toArray());
    }

    @Override
    public T fetchOne() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        SelectBuilder qb = createQuery(
                        table.allFieldsCommaSep(table.getName() + "."), 
                        parameters);
        qb.limit(0, 1);
        return ctx.doQuery(qb.toString(), 
                    (rs) -> table.parse(rs, ctx), 
                    parameters.toArray());
    }

    @Override
    public <C> C fetchOne(Column<C> column) throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        SelectBuilder qb = createQuery(column.getExpression(), parameters);
        qb.limit(0, 1);
        return ctx.doQuery(qb.toString(), 
                    (rs) -> table.parse(1, column, rs, ctx), 
                    parameters.toArray());
    }

    @Override
    public long count() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        SelectBuilder qb = createQuery("COUNT(*)", parameters);
        return ctx.doQuery(qb.toString(), (rs) -> table.parseCount(rs), parameters.toArray());
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
    public Query<T> orderBy(OrderBy... statements)
    {
        orderBy = statements;
        return this;
    }

    protected SelectBuilder createQuery(String fields, List<Object> parameters)
    {
        SelectBuilder qb = new SelectBuilder();
        qb.select(fields)
            .from(table.getName());
        if(condition != null)
        {
            qb.where(condition.writeString(parameters));
        }
        if(orderBy != null)
        {
            qb.orderBy(Arrays
                    .asList(orderBy).stream()
                    .map((ob) -> table.buildOrderBy(ob, table.getName() + "."))
                    .collect(Collectors.joining(", ")));
        }
        return qb;
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
