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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bridje.orm.Column;
import org.bridje.orm.Condition;
import org.bridje.orm.OrderBy;
import org.bridje.orm.Query;
import org.bridje.orm.TableColumn;
import org.bridje.orm.impl.sql.SelectBuilder;

/**
 *
 */
class JoinQueryImpl<T, R> extends AbstractQuery<R> implements Query<R>
{
    private TableRelationColumnImpl<T, R> relation;

    private TableImpl<R> related;

    private Condition condition;

    private final AbstractQuery<T> baseQuery;

    private final JoinType type;

    public JoinQueryImpl(JoinType type, AbstractQuery<T> baseQuery, 
            TableRelationColumnImpl<T, R> relation)
    {
        this.type = type;
        this.baseQuery = baseQuery;
        this.relation = relation;
    }

    public JoinQueryImpl(JoinType type, AbstractQuery<T> baseQuery, TableImpl<R> related, Condition condition)
    {
        this.related = related;
        this.condition = condition;
        this.baseQuery = baseQuery;
        this.type = type;
    }

    @Override
    public Query<R> paging(int page, int size)
    {
        baseQuery.paging(page, size);
        return this;
    }

    @Override
    public Query<R> where(Condition condition)
    {
        baseQuery.where(condition);
        return this;
    }

    @Override
    public Query<R> orderBy(OrderBy... statements)
    {
        baseQuery.orderBy(statements);
        return this;
    }

    @Override
    protected int getPage()
    {
        return baseQuery.getPage();
    }

    @Override
    protected int getPageSize()
    {
        return baseQuery.getPageSize();
    }

    @Override
    protected TableImpl<R> getTable()
    {
        if(relation == null)
        {
            return related;
        }
        return (TableImpl<R>)relation.getRelated();
    }

    @Override
    protected TableImpl<?> getBaseTable()
    {
        return baseQuery.getBaseTable();
    }

    @Override
    protected EntityContextImpl getCtx()
    {
        return baseQuery.getCtx();
    }

    @Override
    protected SelectBuilder createQuery(String fields, List<Object> parameters)
    {
        SelectBuilder qb = new SelectBuilder(getCtx().getDialect());
        qb.select(fields)
            .from(getCtx().getDialect().identifier(getBaseTable().getName()));
        createJoins(qb, parameters);
        if(getCondition() != null)
        {
            qb.where(getCondition().writeSQL(parameters, getCtx()));
        }
        if(getOrderBy() != null)
        {
            qb.orderBy(Arrays
                    .asList(getOrderBy()).stream()
                    .map((ob) -> getTable().buildOrderBy(ob, parameters, getCtx()))
                    .collect(Collectors.joining(", ")));
        }
        return qb;
    }

    private void createJoins(SelectBuilder qb, List<Object> parameters)
    {
        if(baseQuery instanceof JoinQueryImpl)
        {
            ((JoinQueryImpl)baseQuery).createJoins(qb, parameters);
        }
        StringBuilder joinCondition = new StringBuilder();
        if(relation != null)
        {
            joinCondition.append(getCtx().getDialect().identifier(relation.getTable().getName()));
            joinCondition.append('.');
            joinCondition.append(getCtx().getDialect().identifier(relation.getName()));
            joinCondition.append(" = ");
            joinCondition.append(getCtx().getDialect().identifier(relation.getRelated().getName()));
            joinCondition.append('.');
            joinCondition.append(getCtx().getDialect().identifier(relation.getRelated().getKey().getName()));
            qb.join(type.name(), getCtx().getDialect().identifier(getTable().getName()), joinCondition.toString());
        }
        else
        {
            qb.join(type.name(), getCtx().getDialect().identifier(getTable().getName()), condition.writeSQL(parameters, getCtx()));
        }
    }

    @Override
    protected Condition getCondition()
    {
        return baseQuery.getCondition();
    }

    @Override
    protected OrderBy[] getOrderBy()
    {
        return baseQuery.getOrderBy();
    }

    @Override
    public int delete() throws SQLException
    {
        throw new UnsupportedOperationException("DELETE JOIN is not supported yet.");
    }

    @Override
    protected Map<TableColumn<?, ?>, Object> getSets()
    {
        throw new UnsupportedOperationException("INSERT or UPDATE is not supported yet.");
    }

    @Override
    public int update() throws SQLException
    {
        throw new UnsupportedOperationException("INSERT or UPDATE is not supported yet.");
    }

    @Override
    public int insert() throws SQLException
    {
        throw new UnsupportedOperationException("INSERT or UPDATE is not supported yet.");
    }

    @Override
    public <D> Query<R> set(TableColumn<R, D> column, D value)
    {
        throw new UnsupportedOperationException("INSERT or UPDATE is not supported yet.");
    }

    @Override
    public <D> Query<R> set(TableColumn<R, D> column, Column<D> valueColumn)
    {
        throw new UnsupportedOperationException("INSERT or UPDATE is not supported yet.");
    }
}
