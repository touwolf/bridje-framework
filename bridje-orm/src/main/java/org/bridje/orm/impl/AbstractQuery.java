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
import java.util.List;
import java.util.Map;
import org.bridje.orm.Column;
import org.bridje.orm.Condition;
import org.bridje.orm.OrderBy;
import org.bridje.orm.Query;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;
import org.bridje.orm.TableRelationColumn;
import org.bridje.orm.impl.sql.SelectBuilder;

abstract class AbstractQuery<T> implements Query<T>
{
    @Override
    public List<T> fetchAll() throws SQLException
    {
        List<Object> params = new ArrayList<>();
        TableImpl<T> table = getTable();
        
        EntityContextImpl ctx = getCtx();
        String columns = table.allFieldsCommaSep(ctx);
        SelectBuilder qb = createQuery(columns, params);
        if(getPage() > 0)
        {
            int index = ((getPage() - 1) * getPageSize());
            qb.limit(index, getPageSize());
        }
        return ctx.doQuery(qb.toString(), 
                        (rs) -> table.parseAll(rs, ctx), 
                        params.toArray());
    }

    @Override
    public <C> List<C> fetchAll(Column<C> column) throws SQLException
    {
        List<Object> params = new ArrayList<>();
        TableImpl<T> table = getTable();
        AbstractColumn<C> columnImpl = (AbstractColumn<C>)column;
        EntityContextImpl ctx = getCtx();
        SelectBuilder qb = createQuery(column.writeSQL(params, ctx), params);
        if(getPage() > 0)
        {
            int index = ((getPage() - 1) * getPageSize());
            qb.limit(index, getPageSize());
        }
        return ctx.doQuery(qb.toString(), 
                (rs) -> (List<C>)columnImpl.unserialize(table.parseAll(1, column, rs, ctx)), 
                params.toArray());
    }

    @Override
    public T fetchOne() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        TableImpl<T> table = getTable();
        EntityContextImpl ctx = getCtx();
        SelectBuilder qb = createQuery(
                        table.allFieldsCommaSep(ctx), 
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
        TableImpl<T> table = getTable();
        AbstractColumn<C> columnImpl = (AbstractColumn<C>)column;
        EntityContextImpl ctx = getCtx();
        SelectBuilder qb = createQuery(column.writeSQL(parameters, ctx), parameters);
        qb.limit(0, 1);
        return columnImpl.unserialize(ctx.doQuery(qb.toString(), 
                                rs -> table.parse(1, column, rs, ctx), 
                                parameters.toArray()));
    }
    
    @Override
    public <R> List<R> fetchAll(Table<R> table) throws SQLException
    {
        TableImpl<R> tableImpl = (TableImpl<R>)table;
        List<Object> params = new ArrayList<>();
        EntityContextImpl ctx = getCtx();
        String columns = tableImpl.allFieldsCommaSep(ctx);
        SelectBuilder qb = createQuery(columns, params);
        if(getPage() > 0)
        {
            int index = ((getPage() - 1) * getPageSize());
            qb.limit(index, getPageSize());
        }
        return ctx.doQuery(qb.toString(), 
                        rs -> tableImpl.parseAll(rs, ctx), 
                        params.toArray());
    }

    @Override
    public <R> R fetchOne(Table<R> table) throws SQLException
    {
        TableImpl<R> tableImpl = (TableImpl<R>)table;
        List<Object> parameters = new ArrayList<>();
        EntityContextImpl ctx = getCtx();
        SelectBuilder qb = createQuery(
                        tableImpl.allFieldsCommaSep(ctx), 
                        parameters);
        qb.limit(0, 1);
        return ctx.doQuery(qb.toString(), 
                        rs -> tableImpl.parse(rs, ctx), 
                        parameters.toArray());
    }

    @Override
    public long count() throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        TableImpl<T> table = getTable();
        EntityContextImpl ctx = getCtx();
        SelectBuilder qb = createQuery("COUNT(*)", parameters);
        return ctx.doQuery(qb.toString(), 
                        rs -> table.parseCount(rs), 
                        parameters.toArray());
    }

    @Override
    public boolean exists() throws SQLException
    {
        return count() > 0;
    }

    @Override
    public <R> Query<R> join(TableRelationColumn<T, R> relation)
    {
        return new JoinQueryImpl<>(JoinType.INNER, this, (TableRelationColumnImpl<T, R>)relation);
    }

    @Override
    public <R> Query<R> leftJoin(TableRelationColumn<T, R> relation)
    {
        return new JoinQueryImpl<>(JoinType.LEFT, this, (TableRelationColumnImpl<T, R>)relation);
    }

    @Override
    public <R> Query<R> rightJoin(TableRelationColumn<T, R> relation)
    {
        return new JoinQueryImpl<>(JoinType.RIGHT, this, (TableRelationColumnImpl<T, R>)relation);
    }

    @Override
    public <R> Query<R> join(Table<R> related, Condition on)
    {
        return new JoinQueryImpl<>(JoinType.INNER, this, (TableImpl<R>)related, on);
    }

    @Override
    public <R> Query<R> leftJoin(Table<R> related, Condition on)
    {
        return new JoinQueryImpl<>(JoinType.LEFT, this, (TableImpl<R>)related, on);
    }

    @Override
    public <R> Query<R> rightJoin(Table<R> related, Condition on)
    {
        return new JoinQueryImpl<>(JoinType.RIGHT, this, (TableImpl<R>)related, on);
    }

    protected abstract Map<TableColumn<?,?>, Object> getSets();

    protected abstract int getPage();

    protected abstract int getPageSize();

    protected abstract TableImpl<T> getTable();

    protected abstract TableImpl<?> getBaseTable();

    protected abstract EntityContextImpl getCtx();
    
    protected abstract SelectBuilder createQuery(String fields, List<Object> parameters);

    protected abstract Condition getCondition();

    protected abstract OrderBy[] getOrderBy();
}
