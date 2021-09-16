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

package org.bridje.sql.impl;

import org.bridje.sql.*;

class UnaryExpr<T, E> extends ExpressionBase<T, E> implements BooleanExpr<T, E>, StringExpr<T, E>, ArithmeticExpr<T, E>, DateExpr<T, E>
{
    private final Expression<?, ?> operand;

    private final Operators operator;

    public UnaryExpr(Operators operator, Expression<?, ?> operand, SQLType<T, E> type)
    {
        super(type);
        this.operand = operand;
        this.operator = operator;
    }

    public Expression<?, ?> getOperand()
    {
        return operand;
    }

    public Operators getOperator()
    {
        return operator;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.append('(');
        builder.append(operator.toSQL());
        builder.append(' ');
        builder.append(operand);
        builder.append(')');
    }
}
