/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.el;

/**
 * Represents an environment that holds the variables that can be evaluated and
 * assigned in the expression language execution context.
 */
public interface ElEnvironment
{
    /**
     * Evaluates the given expression and cast the result of it to the given
     * result class.
     * 
     * @param <T> The type of the result of the expression.
     * @param expression The expression.
     * @param resultCls The result class for the expression.
     * @return An object of the given resultClss.
     */
    <T> T get(String expression, Class<T> resultCls);

    /**
     * Sets the given expression to the specified value.
     * 
     * @param <T> The type of the value.
     * @param expression The expression to assign.
     * @param value The value to assign.
     */
    <T> void set(String expression, T value);

    /**
     * Gets the value of the specified variable.
     * 
     * @param <T> The type of the variable.
     * @param name The name of the variable.
     * @param resultCls The result class for the variable.
     * @return The value of the variable.
     */
    <T> T getVar(String name, Class<T> resultCls);

    /**
     * Push the value into the stack of the specified variable.
     * 
     * @param <T> The type of the variable.
     * @param name The name of the variable.
     * @param value The value of the variable.
     */
    <T> void pushVar(String name, T value);

    /**
     * Pops the value out of the stack of the given variable.
     * 
     * @param name The name of the variable.
     */
    void popVar(String name);
    
    /**
     * Gets the var as the string.
     * 
     * @param name The name of the var.
     * @return The string value for this var.
     */
    String getVarAsString(String name);
}
