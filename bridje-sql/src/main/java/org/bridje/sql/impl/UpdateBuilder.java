/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.sql.impl;

import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.BooleanExpr;
import org.bridje.sql.Column;
import org.bridje.sql.Expression;
import org.bridje.sql.FinalStep;
import org.bridje.sql.OrderExpr;
import org.bridje.sql.Query;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLStatement;
import org.bridje.sql.SetsStep;
import org.bridje.sql.Table;
import org.bridje.sql.TableExpr;
import org.bridje.sql.UpdateLimitStep;
import org.bridje.sql.UpdateStep;
import org.bridje.sql.UpdateWhereStep;
import org.bridje.sql.Limit;

class UpdateBuilder extends BuilderBase implements UpdateStep, Query
{
    private final Table table;

    private List<Join> joinsLst;

    private List<Assign<?, ?>> setsLst;

    private BooleanExpr<?, ?> where;

    private OrderExpr[] orderBys;

    private Limit limit;

    public UpdateBuilder(Table table)
    {
        this.table = table;
    }

    @Override
    public UpdateStep innerJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.INNER, on));
        return this;
    }

    @Override
    public UpdateStep leftJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.LEFT, on));
        return this;
    }

    @Override
    public UpdateStep rightJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.RIGHT, on));
        return this;
    }

    @Override
    public <T, E> SetsStep set(Column<T, E> column, T value)
    {
        if(setsLst == null) setsLst = new ArrayList<>();
        setsLst.add(new Assign<>(column, new LiteralImpl<>(value)));
        return this;
    }

    @Override
    public <T, E> SetsStep set(Column<T, E> column, Expression<T, E> value)
    {
        if(setsLst == null) setsLst = new ArrayList<>();
        setsLst.add(new Assign<>(column, value));
        return this;
    }

    @Override
    public UpdateWhereStep where(BooleanExpr<?, ?> condition)
    {
        this.where = condition;
        return this;
    }

    @Override
    public UpdateLimitStep orderBy(OrderExpr... orderBys)
    {
        this.orderBys = orderBys;
        return this;
    }

    @Override
    public FinalStep limit(Limit limit)
    {
        this.limit = limit;
        return this;
    }

    @Override
    public SQLStatement toStatement(SQLDialect dialect, Object... parameters)
    {
        SQLBuilder builder = new SQLBuilderImpl(dialect);
        writeSQL(builder);
        String sql = builder.toString();
        return new SQLStatementImpl(null, sql, 
                    createParams(builder, parameters), false);
    }

    @Override
    public Query toQuery()
    {
        return this;
    }

    public void writeSQL(SQLBuilder builder)
    {
        builder.append("UPDATE ");
        builder.append(table);
        if(joinsLst != null)
        {
            Join[] joins = new Join[joinsLst.size()];
            joinsLst.toArray(joins);
            builder.append(' ');
            builder.appendAll(joins, " ");
        }
        if(setsLst != null)
        {
            Assign<?, ?>[] sets = new Assign<?, ?>[setsLst.size()];
            setsLst.toArray(sets);
            builder.append(" SET ");
            builder.appendAll(sets, " ,");
        }
        if(where != null)
        {
            builder.append(" WHERE ");
            builder.append(where);
        }
        if(orderBys != null)
        {
            builder.append(" ORDER BY ");
            builder.appendAll(orderBys, ", ");
        }
        if(limit != null)
        {
            builder.append(limit);
        }
    }

    @Override
    public Expression<?, ?>[] getResultFields()
    {
        return null;
    }

    @Override
    public boolean isWithGeneratedKeys()
    {
        return false;
    }
}
