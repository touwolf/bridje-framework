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
 * A condition with two operators. This condition can be use with the binary
 * operators in the Operator enum like EQ("="), GT(">"), GE(">="), LT("&lt;"),
 * LE("&lt;="), NE("&lt;&gt;"), AND("and"), OR("or")
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
    public String writeString(List<Object> parameters, ColumnNameFinder cnf)
    {
        StringBuilder sb = new StringBuilder();

        writeOperand(firstOperand, parameters, cnf, sb);
        sb.append(" ");
        sb.append(operator.toString());
        sb.append(" ");
        writeOperand(secondOperand, parameters, cnf, sb);

        return sb.toString();
    }

    /**
     * This method writes and operant to the StringBuilder when the condition is
     * beign serialised in the writeString method.
     *
     * @param operand The operant to be writed
     * @param parameters The parameters list for the query.
     * @param cnf The ColumnNameFinder that will be use for writing columns
     * names in the condition.
     * @param sb THe StringBuilder to write the result.
     */
    private void writeOperand(Object operand, List<Object> parameters, ColumnNameFinder cnf, StringBuilder sb)
    {
        if (operand instanceof Column)
        {
            writeColumn((Column) operand, parameters, cnf, sb);
        }
        else if (operand instanceof Condition)
        {
            writeCondition((Condition) operand, parameters, cnf, sb);
        }
        else
        {
            writeLiteral(operand, parameters, sb);
        }
    }

    /**
     * This method writes a column as one of the operands of the current
     * condition when this object is beign serialised.
     *
     * @param column The column operand to write.
     * @param parameters The parameters list for the query.
     * @param cnf The ColumnNameFinder that will be use for writing columns
     * names in the condition.
     * @param sb THe StringBuilder to write the result.
     */
    private void writeColumn(Column column, List<Object> parameters, ColumnNameFinder cnf, StringBuilder sb)
    {
        sb.append(cnf.findColumnName(column));
        if (column.getParameters() != null)
        {
            parameters.addAll(column.getParameters());
        }
    }

    /**
     * This method writes a condition as one of the operands of the current
     * condition when this object is beign serialised.
     *
     * @param condition
     * @param parameters The parameters list for the query.
     * @param cnf The ColumnNameFinder that will be use for writing columns
     * names in the condition.
     * @param sb THe StringBuilder to write the result.
     */
    private void writeCondition(Condition condition, List<Object> parameters, ColumnNameFinder cnf, StringBuilder sb)
    {
        sb.append("(");
        sb.append(condition.writeString(parameters, cnf));
        sb.append(")");
    }

    /**
     * This method writes a literal param as one of the operands of the current
     * condition when this object is beign serialised.
     *
     * @param operand The operand literal to write.
     * @param parameters The parameters list for the query.
     * @param sb THe StringBuilder to write the result.
     */
    private void writeLiteral(Object operand, List<Object> parameters, StringBuilder sb)
    {
        parameters.add(operand);
        sb.append("?");
    }
}
