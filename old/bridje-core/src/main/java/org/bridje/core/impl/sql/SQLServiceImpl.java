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

import org.bridje.ioc.Component;
import org.bridje.core.sql.ColumnExpresion;
import org.bridje.core.sql.DeleteFromStep;
import org.bridje.core.sql.FromStep;
import org.bridje.core.sql.InsertSetStep;
import org.bridje.core.sql.SQLService;
import org.bridje.core.sql.Table;
import org.bridje.core.sql.TableExpression;
import org.bridje.core.sql.UpdateSetStep;

@Component
class SQLServiceImpl implements SQLService
{
    @Override
    public FromStep select(ColumnExpresion... selectExp)
    {
        return new SelectSQLQueryImpl(selectExp);
    }

    @Override
    public InsertSetStep insertInto(Table table)
    {
        return new InsertSQLQueryImpl(table);
    }

    @Override
    public UpdateSetStep update(TableExpression from)
    {
        return new UpdateSQLQueryImpl(from);
    }

    @Override
    public DeleteFromStep delete(Table table)
    {
        return new DeleteSQLQueryImpl(table);
    }

    @Override
    public DeleteFromStep delete()
    {
        return new DeleteSQLQueryImpl();
    }
}
