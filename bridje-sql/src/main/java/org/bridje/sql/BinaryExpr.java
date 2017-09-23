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

import org.bridje.sql.expr.ArithmeticExpr;
import org.bridje.sql.expr.BooleanExpr;
import org.bridje.sql.expr.Expression;
import org.bridje.sql.expr.StringExpr;

class BinaryExpr<T> extends ExpressionBase<T> implements BooleanExpr<T>, StringExpr<T>, ArithmeticExpr<T>
{
    private final Expression<?> operand1;

    private final Operators operator;

    private final Expression<?> operand2;

    public BinaryExpr(Expression<?> operand1, Operators operator, Expression<?> operand2, Class<T> type)
    {
        super(type);
        this.operand1 = operand1;
        this.operator = operator;
        this.operand2 = operand2;
    }

    public Expression<?> getOperand1()
    {
        return operand1;
    }

    public Operators getOperator()
    {
        return operator;
    }

    public Expression<?> getOperand2()
    {
        return operand2;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.append('(');
        builder.append(operand1);
        builder.append(' ');
        builder.append(operator.toSQL());
        builder.append(' ');
        builder.append(operand1);
        builder.append(')');
    }
}
