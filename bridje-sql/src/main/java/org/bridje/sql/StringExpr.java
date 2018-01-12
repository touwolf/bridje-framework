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
 * Represents an string SQL expression.
 *
 * @param <T> The final java type of the expression.
 * @param <E> The result set read java type of the expression.
 */
public interface StringExpr<T, E> extends Expression<T, E>
{
    /**
     * Create a new string expression that is the trim of this string
     * expression.
     *
     * @return A new string expression that is the trim of this string
     *         expression.
     */
    StringExpr<T, E> trim();

    /**
     * Gets the length of the string as a number expression.
     *
     * @return The length of the string as a number expression.
     */
    ArithmeticExpr<Integer, Integer> length();

    /**
     * Creates a new boolean expression that performs the LIKE comparison of
     * this expression with the given one.
     *
     * @param stringExpr The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> like(StringExpr<?, ?> stringExpr);

    /**
     * Creates a new boolean expression that performs the LIKE comparison of
     * this expression with the given one.
     *
     *
     * @param value The second operand.
     *
     * @return The new boolean expression.
     */
    BooleanExpr<Boolean, Boolean> like(String value);

}
