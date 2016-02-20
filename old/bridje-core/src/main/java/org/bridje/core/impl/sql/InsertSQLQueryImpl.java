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
import java.util.ArrayList;
import java.util.List;
import org.bridje.core.sql.Column;
import org.bridje.core.sql.InsertSetStep;
import org.bridje.core.sql.Literal;
import org.bridje.core.sql.SQLQuery;
import org.bridje.core.sql.Table;
import org.bridje.core.sql.TableExpression;

class InsertSQLQueryImpl implements InsertSetStep, SQLQuery
{
    private final List<Column> columns;

    private final TableExpression into;
    
    private final List<Literal> values;

    public InsertSQLQueryImpl(Table into)
    {
        this.into = into;
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
    }
    
    @Override
    public String getSQL()
    {
        if(into != null)
        {
            StringWriter sw = new StringWriter();
            writeInsertInto(sw);
            writeColumns(sw);
            writeValues(sw);
            return sw.toString();
        }
        return null;
    }

    @Override
    public InsertSetStep set(Column column, Literal value)
    {
        columns.add(column);
        values.add(value);
        return this;
    }

    private void writeInsertInto(StringWriter sw)
    {
        sw.append("INSERT INTO ");
        into.writeSQL(sw);
    }

    private void writeColumns(StringWriter sw)
    {
        sw.append("\n (");
        Utils.joinExpressions(sw, columns);
        sw.append(")");
    }

    private void writeValues(StringWriter sw)
    {
        sw.append("\nVALUES (");
        Utils.joinExpressions(sw, values);
        sw.append(")");
    }
}
