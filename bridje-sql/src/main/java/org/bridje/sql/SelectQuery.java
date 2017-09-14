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

import org.bridje.sql.expr.BooleanExpr;
import org.bridje.sql.expr.Expression;
import org.bridje.sql.dialect.SQLDialect;
import org.bridje.sql.expr.OrderExpr;
import org.bridje.sql.expr.TableExpr;

public class SelectQuery implements TableExpr
{
    private final Expression<?>[] columns;

    private final TableExpr from;

    private final Join[] joins;

    private final BooleanExpr<?> where;

    private final OrderExpr[] orderBys;

    private final OrderExpr[] groupBys;

    private final BooleanExpr<?> having;

    public SelectQuery(Expression<?>[] columns, TableExpr from, Join[] joins, BooleanExpr<?> where, OrderExpr[] orderBys, OrderExpr[] groupBys, BooleanExpr<?> having)
    {
        this.columns = columns;
        this.from = from;
        this.joins = joins;
        this.where = where;
        this.orderBys = orderBys;
        this.groupBys = groupBys;
        this.having = having;
    }

    public Expression<?>[] getColumns()
    {
        return columns;
    }

    public TableExpr getFrom()
    {
        return from;
    }

    public Join[] getJoins()
    {
        return joins;
    }

    public BooleanExpr<?> getWhere()
    {
        return where;
    }

    public OrderExpr[] getOrderBys()
    {
        return orderBys;
    }

    public OrderExpr[] getGroupBys()
    {
        return groupBys;
    }

    public BooleanExpr<?> getHaving()
    {
        return having;
    }

    public String toSQL(SQLDialect dialect)
    {
        StringBuilder sb = new StringBuilder();
        writeSQL(sb, dialect);
        return sb.toString();
    }

    @Override
    public void writeSQL(StringBuilder builder, SQLDialect dialect)
    {
        builder.append("SELECT ");
        SQLUtils.printCommaSep(builder, dialect, columns);
        if(from != null)
        {
            builder.append(" FROM ");
            from.writeSQL(builder, dialect);
        }
        if(joins != null)
        {
            for (Join join : joins)
            {
                builder.append(' ');
                join.writeSQL(builder, dialect);
            }
        }
        if(where != null)
        {
            builder.append(" WHERE ");
            where.writeSQL(builder, dialect);
        }
        if(orderBys != null)
        {
            builder.append(" ORDER BY ");
            SQLUtils.printCommaSep(builder, dialect, orderBys);
        }
        if(groupBys != null)
        {
            builder.append(" GROUP BY ");
            SQLUtils.printCommaSep(builder, dialect, groupBys);
        }
        if(having != null)
        {
            builder.append(" HAVING ");
            having.writeSQL(builder, dialect);
        }
    }
}
