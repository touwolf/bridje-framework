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

package org.bridje.core.impl.sql;

import java.io.StringWriter;
import static java.lang.Thread.State.values;
import java.util.List;
import org.bridje.core.sql.Literal;
import org.bridje.core.sql.SQLExpression;

/**
 *
 * @author Gilberto
 */
public class Utils
{
    public static void joinExpressions(StringWriter sw, List<? extends SQLExpression> expressions)
    {
        boolean isFirst = true;
        for (SQLExpression literal : expressions)
        {
            if(!isFirst)
            {
                sw.append(", ");
            }
            literal.writeSQL(sw);
            isFirst = false;
        }
    }
}
