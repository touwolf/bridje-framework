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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.bridje.sql.expr.BooleanExpr;
import org.bridje.sql.expr.Expression;
import org.bridje.sql.expr.OrderExpr;
import org.bridje.sql.expr.TableExpr;
import org.bridje.sql.flow.FetchStep;
import org.bridje.sql.flow.FromStep;
import org.bridje.sql.flow.GroupByStep;
import org.bridje.sql.flow.OrderByStep;
import org.bridje.sql.flow.SelectStep;
import org.bridje.sql.flow.WhereStep;

class SelectBuilder implements SelectStep, FromStep, WhereStep, OrderByStep, GroupByStep
{
    private final Expression<?>[] select;

    private TableExpr from;

    private List<Join> joinsLst;

    private BooleanExpr<?> where;

    private OrderExpr[] orderBys;

    private OrderExpr[] groupBys;

    private BooleanExpr<?> having;

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
    public WhereStep where(BooleanExpr<?> condition)
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
    public FetchStep having(BooleanExpr<?> condition)
    {
        this.having = condition;
        return this;
    }

    @Override
    public ResultSet fetchAll(DataSource ds) throws SQLException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet fetchOne(DataSource ds) throws SQLException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet fetchAll(Connection ds) throws SQLException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet fetchOne(Connection ds) throws SQLException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SelectQuery toQuery()
    {
        Join[] joins = null;
        if(joinsLst != null)
        {
            joins = new Join[joinsLst.size()];
            joinsLst.toArray(joins);
        }
        return new SelectQuery(select, from, joins, where, orderBys, groupBys, having);
    }
}
