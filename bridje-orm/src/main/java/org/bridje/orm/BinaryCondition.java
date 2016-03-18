/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.orm;

import java.util.List;
/**
 *
 */
class BinaryCondition extends Condition
{
    private final Object firstOperand;

    private final Operator operator;

    private final Object secondOperand;

    public BinaryCondition(Object firstOperand, Operator operator, Object secondOperand)
    {
        this.firstOperand = firstOperand;
        this.operator = operator;
        this.secondOperand = secondOperand;
    }

    @Override
    public String writeString(List<Object> parameters)
    {
        StringBuilder sb = new StringBuilder();
        
        writeOperand(firstOperand, parameters, sb);
        sb.append(" ");
        sb.append(operator.toString());
        sb.append(" ");
        writeOperand(secondOperand, parameters, sb);
        
        return sb.toString();
    }

    private void writeOperand(Object operand, List<Object> parameters, StringBuilder sb)
    {
        if(operand instanceof EntityColumn)
        {
            sb.append(((EntityColumn)operand).getField());
        }
        else if(operand instanceof Condition)
        {
            sb.append("(");
            sb.append( ((Condition)operand).writeString(parameters) );
            sb.append(")");
        }
        else
        {
            parameters.add(operand);
            sb.append("?");
        }
    }
}
