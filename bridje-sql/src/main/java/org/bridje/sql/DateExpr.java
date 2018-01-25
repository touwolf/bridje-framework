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
 * A date SQL expression.
 *
 * @param <T> The final java type of the expression.
 * @param <E> The result set read java type of the expression.
 */
public interface DateExpr<T, E> extends Expression<T, E>
{
    /**
     * Creates a new boolean expression that performs the grather than comparation
     * of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> gt(DateExpr<T, E> operand);

    /**
     * Creates a new boolean expression that performs the grather or equals to comparation
     * of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> ge(DateExpr<T, E> operand);

    /**
     * Creates a new boolean expression that performs the less than comparation
     * of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> lt(DateExpr<T, E> operand);

    /**
     * Creates a new boolean expression that performs the less or equals to comparation
     * of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> le(DateExpr<T, E> operand);

    /**
     * 
     * @return 
     */
    ArithmeticExpr<Integer, Integer> year();

    /**
     * 
     * @return 
     */
    ArithmeticExpr<Integer, Integer> month();

    /**
     * 
     * @return 
     */
    ArithmeticExpr<Integer, Integer> dayOfMonth();

    /**
     * 
     * @return 
     */
    ArithmeticExpr<Integer, Integer> dayOfWeek();

    /**
     * 
     * @return 
     */
    ArithmeticExpr<Integer, Integer> dayOfYear();
}
