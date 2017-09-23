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

package org.bridje.sql;

import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.dialect.SQLDialect;
import org.bridje.sql.expr.SQLStatement;
import org.bridje.sql.flow.CreateIndexStep;
import org.bridje.sql.flow.FinalStep;

class CreateIndexBuilder implements CreateIndexStep, FinalStep
{
    private final String name;
    
    private final boolean unique;
    
    private final Table table;

    private List<Column<?>> columns;

    public CreateIndexBuilder(String name, Table table, boolean unique)
    {
        this.unique = unique;
        this.name = name;
        this.table = table;
    }

    @Override
    public CreateIndexStep column(Column<?> column)
    {
        if(columns == null) columns = new ArrayList<>();
        this.columns.add(column);
        return this;
    }

    @Override
    public SQLStatement toSQL(SQLDialect dialect)
    {
        SQLBuilder builder = new SQLBuilder(dialect);
        toSQL(builder);
        return new SQLStatement(builder.toString(), builder.getParameters().toArray());
    }

    public SQLStatement toSQL(SQLBuilder builder)
    {
        Column<?>[] columnsArr = new Column<?>[columns.size()];
        columns.toArray(columnsArr);
        if(unique)
        {
            builder.appendCreateUniqueIndex(name, table, columnsArr);
        }
        else
        {
            builder.appendCreateIndex(name, table, columnsArr);
        }
        return new SQLStatement(builder.toString(), builder.getParameters().toArray());
    }
}
