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
import org.bridje.sql.dialect.SQLDialect;
import org.bridje.sql.expr.OrderExpr;
import org.bridje.sql.expr.SortType;
import org.bridje.sql.expr.StringExpr;

class BinaryExpr<T> implements BooleanExpr<T>, StringExpr<T>, ArithmeticExpr<T>
{
    private final Expression<?> operand1;
    
    private final Operators operator;
    
    private final Expression<?> operand2;

    public BinaryExpr(Expression<?> operand1, Operators operator, Expression<?> operand2)
    {
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
    public BooleanExpr<T> and(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.AND, operand);
    }

    @Override
    public BooleanExpr<T> or(BooleanExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.OR, operand);
    }

    @Override
    public BooleanExpr<T> not()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BooleanExpr<Boolean> eq(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.EQ, operand);
    }

    @Override
    public BooleanExpr<Boolean> ne(Expression<T> operand)
    {
        return new BinaryExpr<>(this, Operators.NE, operand);
    }

    @Override
    public StringExpr<T> trim()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArithmeticExpr<Integer> length()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArithmeticExpr<T> plus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.PLUS, operand);
    }

    @Override
    public ArithmeticExpr<T> minus(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MINUS, operand);
    }

    @Override
    public ArithmeticExpr<T> mul(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MULT, operand);
    }

    @Override
    public ArithmeticExpr<T> div(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.DIV, operand);
    }

    @Override
    public ArithmeticExpr<T> mod(ArithmeticExpr<T> operand)
    {
        return new BinaryExpr<>(this, Operators.MOD, operand);
    }

    @Override
    public void writeSQL(StringBuilder builder, SQLDialect dialect)
    {
        operand1.writeSQL(builder, dialect);
        builder.append(" ");
        builder.append(operator.toSQL());
        builder.append(" ");
        operand1.writeSQL(builder, dialect);
    }

    @Override
    public OrderExpr asc()
    {
        return new OrderBy(SortType.ASC, this);
    }

    @Override
    public OrderExpr desc()
    {
        return new OrderBy(SortType.DESC, this);
    }
}
