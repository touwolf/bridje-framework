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
import java.util.Arrays;
import org.bridje.sql.Condition;
import org.bridje.sql.DeleteFromStep;
import org.bridje.sql.SQLQuery;
import org.bridje.sql.Table;
import org.bridje.sql.TableExpression;
import org.bridje.sql.UpdateWhereStep;

class DeleteSQLQueryImpl implements DeleteFromStep, UpdateWhereStep, SQLQuery
{
    private final Table[] tables;

    private TableExpression from;

    private Condition where;
    
    public DeleteSQLQueryImpl(Table... tables)
    {
        this.tables = tables;
    }
    
    @Override
    public String getSQL()
    {
        StringWriter sw = new StringWriter();
        writeDelete(sw);
        writeFrom(sw);
        writeWhere(sw);
        return sw.toString();
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

    @Override
    public UpdateWhereStep from(TableExpression from)
    {
        this.from = from;
        return this;
    }

    private void writeDelete(StringWriter sw)
    {
        sw.append("DELETE ");
        Utils.joinExpressions(sw, Arrays.asList(tables));
    }

    private void writeFrom(StringWriter sw)
    {
        sw.append("FROM ");
        from.writeSQL(sw);
    }
}
