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

import org.bridje.orm.impl.sql.SelectBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bridje.orm.ColumnNameFinder;
import org.bridje.orm.Query;

/**
 *
 * @param <T>
 */
class JoinQueryImpl<R, T> extends QueryImpl<R> implements Query<R>, ColumnNameFinder
{
    private final QueryImpl<T> baseQuery;
    
    private final RelationInf<T, R> relationInf;
    
    public JoinQueryImpl(QueryImpl<T> baseQuery, RelationInf<T, R> relationInf)
    {
        super(baseQuery.getCtx(), relationInf.getRelatedEntity());
        this.baseQuery = baseQuery;
        this.relationInf = relationInf;
    }
    
    @Override
    protected SelectBuilder createQuery(String fields, List<Object> parameters)
    {
        EntityInf baseEntityInf = findBaseEntityInf();
        SelectBuilder qb = new SelectBuilder();
        qb.select(fields)
            .from(baseEntityInf.getTableName());
        createJoins(qb);
        createWhere(qb, parameters);
        createOrderBy(qb);
        return qb;
    }

    private EntityInf findBaseEntityInf()
    {
        if(baseQuery instanceof JoinQueryImpl)
        {
            return ((JoinQueryImpl)baseQuery).findBaseEntityInf();
        }
        else
        {
            return baseQuery.getEntityInf();
        }
    }

    private void createJoins(SelectBuilder qb)
    {
        if(baseQuery instanceof JoinQueryImpl)
        {
            ((JoinQueryImpl)baseQuery).createJoins(qb);
        }
        StringBuilder joinCondition = new StringBuilder();
        joinCondition.append(getEntityInf().getTableName());
        joinCondition.append('.');
        joinCondition.append(getEntityInf().getKeyField().getColumnName());
        joinCondition.append(" = ");
        joinCondition.append(relationInf.getEntityInf().getTableName());
        joinCondition.append('.');
        joinCondition.append(relationInf.getColumnName());
        qb.innerJoin(getEntityInf().getTableName(), joinCondition.toString());
    }

    private void createWhere(SelectBuilder qb, List<Object> parameters)
    {
        if(baseQuery instanceof JoinQueryImpl)
        {
            ((JoinQueryImpl)baseQuery).createWhere(qb, parameters);
        }
        else
        {
            if(baseQuery.getCondition() != null)
            {
                qb.where(baseQuery.getCondition().writeString(parameters, this));
            }
        }
        if(getCondition() != null)
        {
            qb.where(getCondition().writeString(parameters, this));
        }
    }

    private void createOrderBy(SelectBuilder qb)
    {
        if(baseQuery instanceof JoinQueryImpl)
        {
            ((JoinQueryImpl)baseQuery).createOrderBy(qb);
        }
        else
        {
            if(baseQuery.getOrderBy() != null)
            {
                qb.orderBy(Arrays
                        .asList(baseQuery.getOrderBy()).stream()
                        .map((ob) -> baseQuery.getEntityInf().buildOrderBy(ob, 
                                baseQuery.getEntityInf().getTableName() + "."))
                        .collect(Collectors.joining(", ")));
            }
        }
        if(getOrderBy() != null)
        {
            qb.orderBy(Arrays
                    .asList(getOrderBy()).stream()
                    .map((ob) -> getEntityInf().buildOrderBy(ob, getEntityInf().getTableName() + "."))
                    .collect(Collectors.joining(", ")));
        }
    }

}
