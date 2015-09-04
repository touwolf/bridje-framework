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

package org.bridje.sql;

import java.io.StringWriter;
public class Column implements ColumnExpresion
{
    private final Table table;

    private final String name;

    public Column(Table table, String name)
    {
        this.table = table;
        this.name = name;
    }
    
    public Table getTable()
    {
        return table;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Condition isNull()
    {
        return new ConditionImpl(this, "IS", null);
    }

    public Condition isNotNull()
    {
        return new ConditionImpl(this, "IS", null).not();
    }
    
    public OrderExpression asc()
    {
        return new OrderExpressionImpl(this, "ASC");
    }

    public OrderExpression desc()
    {
        return new OrderExpressionImpl(this, "DESC");
    }

    @Override
    public Condition eq(ColumnExpresion exp)
    {
        return new ConditionImpl(this, "=", exp);
    }

    @Override
    public Condition gt(ColumnExpresion exp)
    {
        return new ConditionImpl(this, ">", exp);
    }

    @Override
    public Condition ge(ColumnExpresion exp)
    {
        return new ConditionImpl(this, ">=", exp);
    }

    @Override
    public Condition lt(ColumnExpresion exp)
    {
        return new ConditionImpl(this, "<", exp);
    }

    @Override
    public Condition le(ColumnExpresion exp)
    {
        return new ConditionImpl(this, "<=", exp);
    }

    @Override
    public void writeSQL(StringWriter sw)
    {
        table.writeSQL(sw);
        sw.append(".");
        sw.append(name);
    }
}
