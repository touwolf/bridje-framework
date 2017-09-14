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
import org.bridje.sql.expr.SelectExpr;
import org.bridje.sql.expr.TableExpr;
import org.bridje.sql.flow.FromStep;
import org.bridje.sql.flow.GroupByStep;
import org.bridje.sql.flow.SelectStep;
import org.bridje.sql.flow.SelectWhereStep;
import org.bridje.sql.flow.OrderByStep;
import org.bridje.sql.flow.SelectLimitStep;
import org.bridje.sql.flow.SelectFinalStep;

class SelectBuilder implements SelectStep, FromStep, SelectWhereStep, OrderByStep, GroupByStep, SelectLimitStep
{
    private final Expression<?>[] select;

    private TableExpr from;

    private List<Join> joinsLst;

    private BooleanExpr<?> where;

    private OrderExpr[] orderBys;

    private OrderExpr[] groupBys;

    private BooleanExpr<?> having;

    private LimitExpr limit;

    public SelectBuilder(Expression<?>[] select)
    {
        this.select = select;
    }

    public Expression<?>[] getSelect()
    {
        return select;
    }

    @Override
    public FromStep from(TableExpr table)
    {
        this.from = table;
        return this;
    }

    @Override
    public FromStep innerJoin(TableExpr table, BooleanExpr<?> on)
    {
        if(this.joinsLst == null) this.joinsLst = new ArrayList<>();
        this.joinsLst.add(new Join(table, JoinType.INNER, on));
        return this;
    }

    @Override
    public FromStep leftJoin(TableExpr table, BooleanExpr<?> on)
    {
        this.joinsLst.add(new Join(table, JoinType.LEFT, on));
        return this;
    }

    @Override
    public FromStep rightJoin(TableExpr table, BooleanExpr<?> on)
    {
        this.joinsLst.add(new Join(table, JoinType.RIGHT, on));
        return this;
    }

    @Override
    public SelectWhereStep where(BooleanExpr<?> condition)
    {
        this.where = condition;
        return this;
    }

    @Override
    public OrderByStep orderBy(OrderExpr... orderBys)
    {
        this.orderBys = orderBys;
        return this;
    }

    @Override
    public GroupByStep groupBy(OrderExpr... groupBys)
    {
        this.groupBys = groupBys;
        return this;
    }

    @Override
    public SelectLimitStep having(BooleanExpr<?> condition)
    {
        this.having = condition;
        return this;
    }

    @Override
    public SelectExpr asTable()
    {
        return toSelectQuery();
    }

    @Override
    public SelectFinalStep limit(int offset)
    {
        this.limit = new Limit(offset);
        return this;
    }

    @Override
    public SelectFinalStep limit(int offset, int count)
    {
        this.limit = new Limit(offset, count);
        return this;
    }

    @Override
    public SQLStatement toSQL(SQLDialect dialect)
    {
        SQLBuilder builder = new SQLBuilder(dialect);
        toSelectQuery().writeSQL(builder);
        String query = builder.toString();
        return new SQLStatement(query, builder.getParameters().toArray());
    }

    private SelectQuery toSelectQuery()
    {
        Join[] joins = null;
        if(joinsLst != null)
        {
            joins = new Join[joinsLst.size()];
            joinsLst.toArray(joins);
        }
        return new SelectQuery(select, from, joins, where, orderBys, groupBys, having, limit);
    }
}
