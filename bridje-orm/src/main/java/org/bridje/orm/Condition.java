/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.orm;

/**
 * Represents a condition that can be use in a query. Conditions can be build
 * from objects of {@link Column} class.
 */
public interface Condition
{
    /**
     * Creates a new condition that will be the logical and between this
     * condition and the given one.
     *
     * @param otherCondition The condition to join this condition to in a new
     * logical and condition.
     * @return The new created condition.
     */
    Condition and(Condition otherCondition);

    /**
     * Creates a new condition that will be the logical or between this
     * condition or the given one.
     *
     * @param otherCondition The condition to join this condition to in a new
     * logical or condition.
     * @return The new created condition.
     */
    Condition or(Condition otherCondition);
}
