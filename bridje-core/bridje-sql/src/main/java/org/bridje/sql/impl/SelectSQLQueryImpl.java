/*
 * Copyright 2015 Bridje Framework.
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

import java.io.StringWriter;
import java.util.Arrays;
import org.bridje.sql.ColumnExpresion;
import org.bridje.sql.Condition;
import org.bridje.sql.FromStep;
import org.bridje.sql.GroupByStep;
import org.bridje.sql.HavingStep;
import org.bridje.sql.OrderExpression;
import org.bridje.sql.OrderStep;
import org.bridje.sql.SQLQuery;
import org.bridje.sql.TableExpression;
import org.bridje.sql.SelectWhereStep;

class SelectSQLQueryImpl implements FromStep, SelectWhereStep, GroupByStep, HavingStep, SQLQuery
{
    private ColumnExpresion[] select;

    private TableExpression from;

    private Condition where;
    
    private OrderExpression[] groupBy;
    
    private Condition having;
    
    private OrderExpression[] orderBy;

    public SelectSQLQueryImpl(ColumnExpresion[] select)
    {
        this.select = select;
    }
    
    @Override
    public String getSQL()
    {
        if(select != null)
        {
            StringWriter sw = new StringWriter();
            writeSelect(sw);
            if(from != null)
            {
                writeFrom(sw);
                if(where != null)
                {
                    writeWhere(sw);
                }
                if(groupBy != null)
                {
                    writeGroupBy(sw);
                    if(having != null)
                    {
                        writeHaving(sw);
                    }
                }
                if(orderBy != null)
                {
                    writeOrderBy(sw);
                }
            }
            return sw.toString();
        }
        return null;
    }
    
    @Override
    public SelectWhereStep from(TableExpression table)
    {
        this.from = table;
        return this;
    }

    @Override
    public GroupByStep where(Condition condition)
    {
        this.where = condition;
        return this;
    }

    @Override
    public HavingStep groupBy(OrderExpression... groupByExp)
    {
        this.groupBy = groupByExp;
        return this;
    }

    @Override
    public SQLQuery orderBy(OrderExpression... orderExp)
    {
        this.orderBy = orderExp;
        return this;
    }

    @Override
    public OrderStep having(Condition condition)
    {
        this.having = condition;
        return this;
    }

    private void writeSelect(StringWriter sw)
    {
        sw.append("SELECT ");
        Utils.joinExpressions(sw, Arrays.asList(select));
    }

    private void writeFrom(StringWriter sw)
    {
        sw.append("\nFROM ");
        from.writeSQL(sw);
    }

    private void writeWhere(StringWriter sw)
    {
        sw.append("\nWHERE ");
        where.writeSQL(sw);
    }

    private void writeGroupBy(StringWriter sw)
    {
        sw.append("\nGROUP BY ");
        Utils.joinExpressions(sw, Arrays.asList(groupBy));
    }

    private void writeHaving(StringWriter sw)
    {
        sw.append("\nHAVING ");
        having.writeSQL(sw);
    }

    private void writeOrderBy(StringWriter sw)
    {
        sw.append("\nORDER BY ");
        Utils.joinExpressions(sw, Arrays.asList(orderBy));
    }
}
