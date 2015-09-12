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

package org.bridje.sql.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.Column;
import org.bridje.sql.ColumnExpresion;
import org.bridje.sql.Condition;
import org.bridje.sql.SQLQuery;
import org.bridje.sql.Table;
import org.bridje.sql.TableExpression;
import org.bridje.sql.UpdateSetStep;
import static org.bridje.sql.impl.Utils.joinExpressions;

class UpdateSQLQueryImpl implements UpdateSetStep, SQLQuery
{
    private final List<UpdateSetExpression> values;

    private final TableExpression table;

    private Condition where;
    
    public UpdateSQLQueryImpl(TableExpression table)
    {
        this.table = table;
        this.values = new ArrayList<>();
    }
    
    @Override
    public String getSQL()
    {
        if(table != null)
        {
            StringWriter sw = new StringWriter();
            writeUpdate(sw);
            writeSets(sw);
            writeWhere(sw);
            return sw.toString();
        }
        return null;
    }

    @Override
    public UpdateSetStep set(Column column, ColumnExpresion value)
    {
        values.add(new UpdateSetExpression(column, value));
        return this;
    }

    private void writeUpdate(StringWriter sw)
    {
        sw.append("UPDATE ");
        table.writeSQL(sw);
    }

    private void writeSets(StringWriter sw)
    {
        sw.append("\nSET ");
        joinExpressions(sw, values);
    }    

    @Override
    public SQLQuery where(Condition condition)
    {
        this.where = condition;
        return this;
    }

    private void writeWhere(StringWriter sw)
    {
        sw.append("\nWHERE ");
        where.writeSQL(sw);
    }
}
