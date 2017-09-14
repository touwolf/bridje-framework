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

import java.sql.JDBCType;
import org.bridje.sql.expr.ArithmeticExpr;

public class NumberColumn<T> extends Column<T> implements ArithmeticExpr<T>
{
    public NumberColumn(Table table, String name, boolean allowNull, JDBCType jdbcType, Class<T> javaType)
    {
        super(table, name, allowNull, jdbcType, javaType);
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
}
