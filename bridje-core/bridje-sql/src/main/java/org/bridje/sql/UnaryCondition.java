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

class UnaryCondition implements Condition
{
    private final SQLOperator operator;
    
    private final Operand operand;

    public UnaryCondition(SQLOperator operator, Operand operand)
    {
        this.operator = operator;
        this.operand = operand;
    }
    
    public SQLOperator getOperator()
    {
        return operator;
    }

    public Operand getOperand()
    {
        return operand;
    }
    
    @Override
    public Condition and(Condition condition)
    {
        return new BinaryCondition(this, SQLOperator.AND, condition);
    }

    @Override
    public Condition or(Condition condition)
    {
        return new BinaryCondition(this, SQLOperator.OR, condition);
    }

    @Override
    public Condition not()
    {
        return new UnaryCondition(SQLOperator.NOT, this);
    }

    @Override
    public void writeSQL(StringWriter sw)
    {
        sw.append("(");
        if(operator == SQLOperator.NOT)
        {
            sw.append(" ");
            sw.append(operator.toString());
            sw.append(" ");
            writeOperand(sw, operand);
        }
        sw.append(")");
    }

    private void writeOperand(StringWriter sw, Operand operand)
    {
        if(operand != null)
        {
            operand.writeSQL(sw);
        }
        else
        {
            sw.append("NULL");
        }
    }
    
}
