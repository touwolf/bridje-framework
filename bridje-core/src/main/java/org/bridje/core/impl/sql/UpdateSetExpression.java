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
import org.bridje.core.sql.Column;
import org.bridje.core.sql.ColumnExpresion;
import org.bridje.core.sql.SQLExpression;
import org.bridje.core.sql.SQLOperator;

/**
 *
 * @author Gilberto
 */
public class UpdateSetExpression implements SQLExpression
{
    private Column column;
    
    private ColumnExpresion value;

    public UpdateSetExpression(Column column, ColumnExpresion value)
    {
        this.column = column;
        this.value = value;
    }

    @Override
    public void writeSQL(StringWriter sw)
    {
        column.writeSQL(sw);
        sw.append(" ");
        sw.append(SQLOperator.EQ.toString());
        sw.append(" ");
        value.writeSQL(sw);
    }
}
