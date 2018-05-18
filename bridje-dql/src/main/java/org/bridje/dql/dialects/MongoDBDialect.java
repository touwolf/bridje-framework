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

package org.bridje.dql.dialects;

import org.bridje.dql.DQLDialect;
import org.bridje.dql.DQLFieldExpr;
import org.bridje.dql.impl.DQLOperators;
import org.bridje.ioc.Component;

@Component
public class MongoDBDialect implements DQLDialect
{
    @Override
    public void writeFieldArrFilter(StringBuilder sb, DQLOperators operator, DQLFieldExpr field, Object[] values)
    {
        sb.append("{ ");
        sb.append(field);
        sb.append(": { ");
        sb.append(findOperator(operator));
        sb.append(": [ ");
        boolean first = true;
        for (Object val : values)
        {
            if(!first) sb.append(", ");
            sb.append("\"");
            sb.append(val);
            sb.append("\"");
            first = false;
        }
        sb.append(" ]");
        sb.append(" }");
        sb.append(" }");
    }

    private String findOperator(DQLOperators operator)
    {
        switch(operator)
        {
            case IN:
                return "$in";
        }
        return "";
    }
}
