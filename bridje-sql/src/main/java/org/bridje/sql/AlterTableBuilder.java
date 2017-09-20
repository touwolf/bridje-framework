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
import org.bridje.sql.flow.AlterTableStep;

class AlterTableBuilder implements AlterTableStep
{
    private final Table table;

    private List<AlterColumn> columns;

    public AlterTableBuilder(Table table)
    {
        this.table = table;
    }

    @Override
    public AlterTableStep addColumn(Column<?> column)
    {
        if(columns == null) columns = new ArrayList<>();
        columns.add(new AlterColumn(AlterColumnType.ADD, column));
        return this;
    }

    @Override
    public AlterTableStep dropColumn(Column<?> column)
    {
        if(columns == null) columns = new ArrayList<>();
        columns.add(new AlterColumn(AlterColumnType.DROP, column));
        return this;
    }

    @Override
    public AlterTableStep alterColumn(Column<?> column)
    {
        if(columns == null) columns = new ArrayList<>();
        columns.add(new AlterColumn(AlterColumnType.ALTER, column));
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
        builder.appendAlterTable(table);
        for (AlterColumn alterCol : columns)
        {
            switch(alterCol.getType())
            {
                case ADD:
                    builder.appendAddColumn(alterCol.getColumn());
                    break;
                case DROP:
                    builder.appendDropColumn(alterCol.getColumn());
                    break;
                case ALTER:
                    builder.appendAlterColumn(alterCol.getColumn());
                    break;
            }
        }
        return new SQLStatement(builder.toString(), builder.getParameters().toArray());
    }
}
