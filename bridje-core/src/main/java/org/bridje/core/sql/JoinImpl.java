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

package org.bridje.core.sql;

import java.io.StringWriter;

class JoinImpl implements Join, OnStep
{
    private TableExpression leftTable;
    
    private String joinType;
    
    private TableExpression rightTable;

    private Condition on;

    public JoinImpl(TableExpression leftTable, String joinType, TableExpression rightTable)
    {
        this.leftTable = leftTable;
        this.joinType = joinType;
        this.rightTable = rightTable;
    }
    
    public TableExpression getLeftTable()
    {
        return leftTable;
    }

    public void setLeftTable(TableExpression leftTable)
    {
        this.leftTable = leftTable;
    }

    public String getJoinType()
    {
        return joinType;
    }

    public void setJoinType(String joinType)
    {
        this.joinType = joinType;
    }

    public TableExpression getRightTable()
    {
        return rightTable;
    }

    public void setRightTable(TableExpression rightTable)
    {
        this.rightTable = rightTable;
    }

    public Condition getOn()
    {
        return on;
    }

    public void setOn(Condition on)
    {
        this.on = on;
    }
    
    @Override
    public OnStep innerJoin(TableExpression exp)
    {
        return new JoinImpl(this, "INNER", exp);
    }

    @Override
    public void writeSQL(StringWriter sw)
    {
        leftTable.writeSQL(sw);
        sw.write(" ");
        sw.write(joinType);
        sw.write(" JOIN ");
        rightTable.writeSQL(sw);
        sw.write(" ON ");
        on.writeSQL(sw);
    }

    @Override
    public Join on(Condition condition)
    {
        on = condition;
        return this;
    }
    
}
