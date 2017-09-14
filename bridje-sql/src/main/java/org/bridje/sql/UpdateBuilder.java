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

package org.bridje.sql;

import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.dialect.SQLDialect;
import org.bridje.sql.expr.BooleanExpr;
import org.bridje.sql.expr.Expression;
import org.bridje.sql.expr.LimitExpr;
import org.bridje.sql.expr.OrderExpr;
import org.bridje.sql.expr.SQLStatement;
import org.bridje.sql.expr.TableExpr;
import org.bridje.sql.flow.FinalStep;
import org.bridje.sql.flow.SetsStep;
import org.bridje.sql.flow.UpdateLimitStep;
import org.bridje.sql.flow.UpdateStep;
import org.bridje.sql.flow.UpdateWhereStep;

public class UpdateBuilder implements UpdateStep
{
    private final Table table;

    private List<Join> joinsLst;

    private List<Assign<?>> setsLst;

    private BooleanExpr<?> where;

    private OrderExpr[] orderBys;

    private LimitExpr limit;

    public UpdateBuilder(Table table)
    {
        this.table = table;
    }

    @Override
    public UpdateStep innerJoin(TableExpr table, BooleanExpr<?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.INNER, on));
        return this;
    }

    @Override
    public UpdateStep leftJoin(TableExpr table, BooleanExpr<?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.LEFT, on));
        return this;
    }

    @Override
    public UpdateStep rightJoin(TableExpr table, BooleanExpr<?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.RIGHT, on));
        return this;
    }

    @Override
    public <T> SetsStep set(Column<T> column, T value)
    {
        if(setsLst == null) setsLst = new ArrayList<>();
        setsLst.add(new Assign<>(column, new Literal<>(value)));
        return this;
    }

    @Override
    public <T> SetsStep set(Column<T> column, Expression<T> value)
    {
        if(setsLst == null) setsLst = new ArrayList<>();
        setsLst.add(new Assign<>(column, value));
        return this;
    }

    @Override
    public UpdateWhereStep where(BooleanExpr<?> condition)
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
    public FinalStep limit(int offset)
    {
        this.limit = new Limit(offset);
        return this;
    }

    @Override
    public FinalStep limit(int offset, int count)
    {
        this.limit = new Limit(offset, count);
        return this;
    }

    @Override
    public SQLStatement toSQL(SQLDialect dialect)
    {
        SQLBuilder builder = new SQLBuilder(dialect);
        toSQL(builder);
        return new SQLStatement(builder.toString(), builder.getParameters().toArray());
    }

    public SQLStatement toSQL(SQLBuilder builder)
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
            Assign<?>[] sets = new Assign<?>[setsLst.size()];
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
        return new SQLStatement(builder.toString(), builder.getParameters().toArray());
    }
}
