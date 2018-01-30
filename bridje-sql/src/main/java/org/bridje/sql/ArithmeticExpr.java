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
 * An arithmetic SQL expression.
 *
 * @param <T> The final java type of the expression.
 * @param <E> The result set read java type of the expression.
 */
public interface ArithmeticExpr<T, E> extends Expression<T, E>
{
    /**
     * Creates a new arithmetic expression that performs the addition of this
     * expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> plus(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new arithmetic expression that performs the substraction of
     * this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> minus(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new arithmetic expression that performs the multiplication of
     * this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> mul(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new arithmetic expression that performs the division of this
     * expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> div(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new arithmetic expression that performs the modulus of this
     * expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> mod(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new arithmetic expression that performs the addition of this
     * expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> plus(T operand);

    /**
     * Creates a new arithmetic expression that performs the substraction of
     * this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> minus(T operand);

    /**
     * Creates a new arithmetic expression that performs the multiplication of
     * this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> mul(T operand);

    /**
     * Creates a new arithmetic expression that performs the division of this
     * expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> div(T operand);

    /**
     * Creates a new arithmetic expression that performs the modulus of this
     * expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> mod(T operand);

    /**
     * Creates a new boolean expression that performs the grather than
     * comparation of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> gt(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new boolean expression that performs the grather or equals to
     * comparation of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> ge(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new boolean expression that performs the less than comparation
     * of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> lt(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new boolean expression that performs the less or equals to
     * comparation of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> le(ArithmeticExpr<T, E> operand);

    /**
     * Creates a new boolean expression that performs the grather than
     * comparation of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> gt(T operand);

    /**
     * Creates a new boolean expression that performs the grather or equals to
     * comparation of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> ge(T operand);

    /**
     * Creates a new boolean expression that performs the less than comparation
     * of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> lt(T operand);

    /**
     * Creates a new boolean expression that performs the less or equals to
     * comparation of this expresion with the given one.
     *
     * @param operand The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> le(T operand);

    /**
     * Creates a new arithmetic expression that performs the total sum of this
     * expression.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<Long, Long> sum();

    /**
     * Creates a new arithmetic expression that performs the average of this
     * expression.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<Long, Long> avg();

    /**
     * Creates a new arithmetic expression that performs the count of this
     * expression.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<Long, Long> count();

    /**
     * Creates a new arithmetic expression that returns the minimun value for
     * this expression.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> min();

    /**
     * Creates a new arithmetic expression that returns the maximun value for
     * this expression.
     *
     * @return The new arithmetic expression.
     */
    ArithmeticExpr<T, E> max();
}
