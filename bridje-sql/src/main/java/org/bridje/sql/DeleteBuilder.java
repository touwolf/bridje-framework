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
import org.bridje.sql.expr.LimitExpr;
import org.bridje.sql.expr.OrderExpr;
import org.bridje.sql.expr.SQLStatement;
import org.bridje.sql.expr.TableExpr;
import org.bridje.sql.flow.DeleteLimitStep;
import org.bridje.sql.flow.FinalStep;
import org.bridje.sql.flow.DeleteStep;
import org.bridje.sql.flow.DeleteWhereStep;

public class DeleteBuilder implements DeleteStep
{
    private final Table table;

    private List<Join> joinsLst;

    private BooleanExpr<?> where;

    private OrderExpr[] orderBys;

    private LimitExpr limit;

    public DeleteBuilder(Table table)
    {
        this.table = table;
    }

    @Override
    public DeleteStep innerJoin(TableExpr table, BooleanExpr<?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.INNER, on));
        return this;
    }

    @Override
    public DeleteStep leftJoin(TableExpr table, BooleanExpr<?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.LEFT, on));
        return this;
    }

    @Override
    public DeleteStep rightJoin(TableExpr table, BooleanExpr<?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.RIGHT, on));
        return this;
    }

    @Override
    public DeleteWhereStep where(BooleanExpr<?> condition)
    {
        this.where = condition;
        return this;
    }

    @Override
    public DeleteLimitStep orderBy(OrderExpr... orderBys)
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
        builder.append("DELTE ");
        builder.append(table);
        if(joinsLst != null)
        {
            Join[] joins = new Join[joinsLst.size()];
            joinsLst.toArray(joins);
            builder.append(' ');
            builder.appendAll(joins, " ");
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
