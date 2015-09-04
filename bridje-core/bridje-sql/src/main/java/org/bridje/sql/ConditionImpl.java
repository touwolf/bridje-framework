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

class ConditionImpl implements Condition
{
    private Operand rightOperand;
    
    private String operator;
    
    private Operand leftOperand;

    public ConditionImpl(Operand leftOperand, String operator, Operand rightOperand)
    {
        this.rightOperand = rightOperand;
        this.operator = operator;
        this.leftOperand = leftOperand;
    }
    
    public Operand getRightOperand()
    {
        return rightOperand;
    }

    public void setRightOperand(Operand rightOperand)
    {
        this.rightOperand = rightOperand;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public Operand getLeftOperand()
    {
        return leftOperand;
    }

    public void setLeftOperand(Operand leftOperand)
    {
        this.leftOperand = leftOperand;
    }
    
    @Override
    public Condition and(Condition condition)
    {
        return new ConditionImpl(this, "AND", condition);
    }

    @Override
    public Condition or(Condition condition)
    {
        return new ConditionImpl(this, "OR", condition);
    }

    @Override
    public Condition not()
    {
        return new ConditionImpl(null, "NOT", this);
    }

    @Override
    public void writeSQL(StringWriter sw)
    {
        sw.append("(");
        if(operator.equalsIgnoreCase("NOT"))
        {
            sw.append(" ");
            sw.append(operator);
            sw.append(" ");
            writeOperand(sw, rightOperand);
        }
        else
        {
            writeOperand(sw, leftOperand);
            sw.append(" ");
            sw.append(operator);
            sw.append(" ");
            writeOperand(sw, rightOperand);
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
