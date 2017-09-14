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
import org.bridje.sql.expr.BooleanExpr;

public class BooleanColumn<T> extends Column<T> implements BooleanExpr<T>
{
    public BooleanColumn(Table table, String name, boolean allowNull, JDBCType jdbcType, Class<T> javaType)
    {
        super(table, name, allowNull, jdbcType, javaType);
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
}
