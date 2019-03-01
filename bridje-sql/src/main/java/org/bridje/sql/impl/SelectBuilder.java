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
import org.bridje.sql.Expression;
import org.bridje.sql.FinalStep;
import org.bridje.sql.FromStep;
import org.bridje.sql.GroupByStep;
import org.bridje.sql.OrderByStep;
import org.bridje.sql.OrderExpr;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLStatement;
import org.bridje.sql.SelectExpr;
import org.bridje.sql.SelectLimitStep;
import org.bridje.sql.SelectStep;
import org.bridje.sql.SelectWhereStep;
import org.bridje.sql.TableExpr;
import org.bridje.sql.Query;
import org.bridje.sql.Limit;

class SelectBuilder extends BuilderBase implements SelectStep, FromStep, SelectWhereStep, OrderByStep, GroupByStep, SelectLimitStep, SelectExpr, Query
{
    private final Expression<?, ?>[] select;

    private TableExpr from;

    private List<Join> joins;

    private BooleanExpr<?, ?> where;

    private GroupBy[] groupBys;

    private OrderExpr[] orderBys;

    private BooleanExpr<?, ?> having;

    private Limit limit;

    public SelectBuilder(Expression<?, ?>[] select)
    {
        this.select = select;
    }

    public Expression<?, ?>[] getSelect()
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
    public FromStep innerJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        if(this.joins == null) this.joins = new ArrayList<>();
        this.joins.add(new Join(table, JoinType.INNER, on));
        return this;
    }

    @Override
    public FromStep leftJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        this.joins.add(new Join(table, JoinType.LEFT, on));
        return this;
    }

    @Override
    public FromStep rightJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        this.joins.add(new Join(table, JoinType.RIGHT, on));
        return this;
    }

    @Override
    public SelectWhereStep where(BooleanExpr<?, ?> condition)
    {
        this.where = condition;
        return this;
    }

    @Override
    public GroupByStep groupBy(OrderExpr... orderBys)
    {
        this.groupBys = new GroupBy[orderBys.length];
        for (int i = 0; i < orderBys.length; i++)
        {
            this.groupBys[i] = new GroupBy(orderBys[i]);
        }
        return this;
    }

    @Override
    public OrderByStep orderBy(OrderExpr... orderBys)
    {
        this.orderBys = orderBys;
        return this;
    }

    @Override
    public SelectLimitStep having(BooleanExpr<?, ?> condition)
    {
        this.having = condition;
        return this;
    }

    @Override
    public SelectExpr as(String alias)
    {
        return new QueryAsTable(this, alias);
    }

    @Override
    public FinalStep limit(Limit limit)
    {
        this.limit = limit;
        return this;
    }

    @Override
    public Query toQuery()
    {
        return this;
    }

    @Override
    public Expression<?, ?>[] getResultFields()
    {
        return select;
    }

    @Override
    public SQLStatement toStatement(SQLDialect dialect, Object... parameters)
    {
        SQLBuilder builder = new SQLBuilderImpl(dialect);
        writeSQL(builder);
        String sql = builder.toString();
        return new SQLStatementImpl(select, sql, 
                        createParams(builder, parameters), false);
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.append("SELECT ");
        builder.appendAll(select, ", ");
        if(from != null)
        {
            builder.append(" FROM ");
            builder.append(from);
        }
        if(joins != null)
        {
            builder.append(' ');
            Join[] joinArr = new Join[joins.size()];
            joins.toArray(joinArr);
            builder.appendAll(joinArr, " ");
        }
        if(where != null)
        {
            builder.append(" WHERE ");
            builder.append(where);
        }
        if(groupBys != null)
        {
            builder.append(" GROUP BY ");
            builder.appendAll(groupBys, ", ");
        }
        if(orderBys != null)
        {
            builder.append(" ORDER BY ");
            builder.appendAll(orderBys, ", ");
        }
        if(having != null)
        {
            builder.append(" HAVING ");
            builder.append(having);
        }
        if(limit != null)
        {
            builder.append(limit);
        }
    }

    @Override
    public boolean isWithGeneratedKeys()
    {
        return false;
    }
}
