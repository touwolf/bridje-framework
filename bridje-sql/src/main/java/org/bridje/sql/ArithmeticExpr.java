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
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> plus(ArithmeticExpr<T, E> operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> minus(ArithmeticExpr<T, E> operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> mul(ArithmeticExpr<T, E> operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> div(ArithmeticExpr<T, E> operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> mod(ArithmeticExpr<T, E> operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> plus(T operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> minus(T operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> mul(T operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> div(T operand);

    /**
     * 
     * @param operand
     * @return 
     */
    ArithmeticExpr<T, E> mod(T operand);
}
