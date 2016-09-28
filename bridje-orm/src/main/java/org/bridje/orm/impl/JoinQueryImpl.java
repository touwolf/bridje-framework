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
 */
class JoinQueryImpl<T, R> extends AbstractQuery<R> implements Query<R>
{
    private final TableRelationColumnImpl<T, R> relation;

    private final AbstractQuery<T> baseQuery;

    public JoinQueryImpl(AbstractQuery<T> baseQuery, 
            TableRelationColumnImpl<T, R> relation)
    {
        this.baseQuery = baseQuery;
        this.relation = relation;
    }

    @Override
    public Query<R> paging(int page, int size)
    {
        baseQuery.paging(page, size);
        return this;
    }

    @Override
    public <C> Query<C> join(TableRelationColumn<R, C> relation)
    {
        return new JoinQueryImpl<>(this, (TableRelationColumnImpl<R, C>)relation);
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
        return (TableImpl<R>)relation.getRelated();
    }

    @Override
    protected EntityContextImpl getCtx()
    {
        return baseQuery.getCtx();
    }

    @Override
    protected SelectBuilder createQuery(String fields, List<Object> parameters)
    {
        SelectBuilder qb = new SelectBuilder();
        qb.select(fields)
            .from(getCtx().getDialect().identifier(getTable().getName()));
        createJoins(qb);
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

    private void createJoins(SelectBuilder qb)
    {
        if(baseQuery instanceof JoinQueryImpl)
        {
            ((JoinQueryImpl)baseQuery).createJoins(qb);
        }
        StringBuilder joinCondition = new StringBuilder();
        joinCondition.append(getCtx().getDialect().identifier(relation.getTable().getName()));
        joinCondition.append('.');
        joinCondition.append(getCtx().getDialect().identifier(relation.getName()));
        joinCondition.append(" = ");
        joinCondition.append(getCtx().getDialect().identifier(relation.getRelated().getName()));
        joinCondition.append('.');
        joinCondition.append(getCtx().getDialect().identifier(relation.getRelated().getKey().getName()));
        qb.innerJoin(getCtx().getDialect().identifier(relation.getTable().getName()), joinCondition.toString());
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
}
