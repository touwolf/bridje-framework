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
import org.bridje.sql.DeleteFromStep;
import org.bridje.sql.DeleteLimitStep;
import org.bridje.sql.DeleteStep;
import org.bridje.sql.DeleteWhereStep;
import org.bridje.sql.Expression;
import org.bridje.sql.FinalStep;
import org.bridje.sql.LimitExpr;
import org.bridje.sql.OrderExpr;
import org.bridje.sql.Query;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLStatement;
import org.bridje.sql.Table;
import org.bridje.sql.TableExpr;

class DeleteBuilder extends BuilderBase implements DeleteStep, DeleteFromStep, Query
{
    private final Table[] tables;
    
    private TableExpr from;

    private List<Join> joinsLst;

    private BooleanExpr<?, ?> where;

    private OrderExpr[] orderBys;

    private LimitExpr limit;

    public DeleteBuilder(Table[] tables)
    {
        this.tables = tables;
    }

    @Override
    public DeleteFromStep from(TableExpr table)
    {
        this.from = table;
        return this;
    }

    @Override
    public DeleteFromStep innerJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.INNER, on));
        return this;
    }

    @Override
    public DeleteFromStep leftJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.LEFT, on));
        return this;
    }

    @Override
    public DeleteFromStep rightJoin(TableExpr table, BooleanExpr<?, ?> on)
    {
        if(joinsLst == null) joinsLst = new ArrayList<>();
        joinsLst.add(new Join(table, JoinType.RIGHT, on));
        return this;
    }

    @Override
    public DeleteWhereStep where(BooleanExpr<?, ?> condition)
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
        builder.append("DELETE ");
        if(tables != null && tables.length > 0)
        {
            builder.appendAll(tables, ", ");
            builder.append(" FROM ");
        }
        else
        {
            builder.append("FROM ");
        }
        builder.append(from);
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
