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

/**
 * 
 * @param <T> The final java type of the expression.
 * @param <E> The result set read java type of the expression.
 */
public interface Expression<T, E> extends SQLWritable
{
    /**
     * The SQL type for this expression.
     * 
     * @return The SQLType for this expression.
     */
    SQLType<T, E> getSQLType();

    /**
     * Creates a new boolean expression that performs the equals comparation of 
     * this expression with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> eq(Expression<T, E> operand);

    /**
     * Creates a new boolean expression that performs the non equals comparation of 
     * this expression with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> ne(Expression<T, E> operand);

    /**
     * Creates a new boolean expression that performs the equals comparation of 
     * this expression with the given operand.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> eq(T operand);

    /**
     * Creates a new boolean expression that performs the non equals comparation of 
     * this expression with the given operand.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> ne(T operand);

    /**
     * Creates a new order by expression that orders for this expression in an 
     * ascending manner.
     * 
     * @return The new order by expression .
     */
    OrderExpr asc();

    /**
     * Creates a new order by expression that orders for this expression in a 
     * descending manner.
     * 
     * @return The new order by expression .
     */
    OrderExpr desc();
}
