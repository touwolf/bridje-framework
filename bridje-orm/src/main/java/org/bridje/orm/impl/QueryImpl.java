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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bridje.orm.Column;
import org.bridje.orm.Condition;
import org.bridje.orm.OrderBy;
import org.bridje.orm.Query;
import org.bridje.orm.TableColumn;
import org.bridje.orm.impl.sql.DeleteBuilder;
import org.bridje.orm.impl.sql.InsertBuilder;
import org.bridje.orm.impl.sql.SelectBuilder;
import org.bridje.orm.impl.sql.UpdateBuilder;

class QueryImpl<T> extends AbstractQuery<T> implements Query<T>
{
    private final TableImpl<T> table;

    private final EntityContextImpl ctx;

    private Condition condition;

    private OrderBy[] orderBy;

    private int page;

    private int pageSize;

    private Map<TableColumn<?,?>, Object> sets;

    public QueryImpl(EntityContextImpl ctx, TableImpl<T> table)
    {
        this.table = table;
        this.ctx = ctx;
    }

    @Override
    public Query<T> paging(int page, int size)
    {
        this.page = page;
        this.pageSize = size;
        return this;
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
        SelectBuilder qb = new SelectBuilder(ctx.getDialect());
        qb.select(fields)
            .from(ctx.getDialect().identifier(table.getName()));
        if(condition != null)
        {
            qb.where(condition.writeSQL(parameters, ctx));
        }
        if(orderBy != null)
        {
            qb.orderBy(Arrays
                .stream(orderBy)
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
    protected TableImpl<T> getTable()
    {
        return table;
    }

    @Override
    protected EntityContextImpl getCtx()
    {
        return ctx;
    }

    @Override
    protected TableImpl<?> getBaseTable()
    {
        return table;
    }

    @Override
    public int delete() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        DeleteBuilder qb = new DeleteBuilder();
        qb.delete(ctx.getDialect().identifier(table.getName()));
        if(condition != null)
        {
            qb.where(condition.writeSQL(parameters, ctx));
        }
        return ctx.doUpdate(qb.toString(), parameters.toArray());
    }

    @Override
    public T updateOne() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        UpdateBuilder qb = new UpdateBuilder(ctx.getDialect());
        qb.update(ctx.getDialect().identifier(table.getName()));
        getSets().forEach((column, value) ->
        {
            if(value instanceof Column)
            {
                qb.set(column.writeSQL(parameters, ctx),((Column)value).writeSQL(parameters, ctx));
            }
            else
            {
                qb.set(column.writeSQL(parameters, ctx));
                parameters.add(value);
            }
        });
        if(condition != null) qb.where(condition.writeSQL(parameters, ctx));
        return ctx.doUpdate(
                        qb.toString(),
                        rs -> table.parse(rs, ctx), 
                        parameters.toArray());
    }

    @Override
    public int update() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        UpdateBuilder qb = new UpdateBuilder(ctx.getDialect());
        qb.update(ctx.getDialect().identifier(table.getName()));
        getSets().forEach((column, value) ->
        {
            if(value instanceof Column)
            {
                qb.set(column.writeSQL(parameters, ctx),((Column)value).writeSQL(parameters, ctx));
            }
            else
            {
                qb.set(column.writeSQL(parameters, ctx));
                parameters.add(value);
            }
        });
        if(condition != null)
        {
            qb.where(condition.writeSQL(parameters, ctx));
        }
        return ctx.doUpdate(qb.toString(), parameters.toArray());
    }

    @Override
    public int insert() throws SQLException
    {
        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        InsertBuilder qb = new InsertBuilder();
        String tableName = ctx.getDialect().identifier(table.getName());
        qb.insertInto(tableName);
        getSets().forEach((column, value) ->
            fields.add(ctx.getDialect().identifier(column.getName())));
        getSets().forEach((column, value) ->
        {
            if(value instanceof Column)
            {
                values.add(((Column)value).writeSQL(parameters, ctx));
            }
            else
            {
                values.add("?");
                parameters.add(value);
            }
        });
        qb.fields(fields.stream().collect(Collectors.joining(", ")));
        qb.values(values.stream().collect(Collectors.joining(", ")));
        return ctx.doUpdate(qb.toString(), parameters.toArray());
    }

    @Override
    protected Map<TableColumn<?, ?>, Object> getSets()
    {
        if(sets == null)
        {
            sets = new HashMap<>();
        }
        return this.sets;
    }

    @Override
    public <D> Query<T> set(TableColumn<T, D> column, D value)
    {
        getSets().put(column, value);
        return this;
    }

    @Override
    public <D> Query<T> set(TableColumn<T, D> column, Column<D> valueColumn)
    {
        getSets().put(column, valueColumn);
        return this;
    }
}
