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

package org.bridje.orm.impl;

import org.bridje.orm.Column;
import org.bridje.orm.Condition;

/**
 * Represents a condition that can be use in a query. Conditions can be build
 * from objects of {@link Column} class.
 */
abstract class AbstractCondition implements Condition
{
    @Override
    public Condition and(Condition otherCondition)
    {
        return new BinaryCondition(this, Operator.AND, otherCondition);
    }

    @Override
    public Condition or(Condition otherCondition)
    {
        return new BinaryCondition(this, Operator.OR, otherCondition);
    }
}
