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
import org.bridje.sql.flow.CreateTableStep;
import org.bridje.sql.flow.FinalStep;
import org.bridje.sql.flow.ForeignKeyStep;
import org.bridje.sql.flow.ForeignKeysStep;

class CreateTableBuilder implements CreateTableStep, ForeignKeysStep, ForeignKeyStep, FinalStep
{
    private final Table table;

    private Column<?> primaryKey;
    
    private List<Column<?>> columns;
    
    private List<ForeignKey> foreignKeys;
    
    private ForeignKey foreignKey;

    public CreateTableBuilder(Table table)
    {
        this.table = table;
    }

    @Override
    public CreateTableStep column(Column<?> column)
    {
        if(columns == null) columns = new ArrayList<>();
        this.columns.add(column);
        return this;
    }

    @Override
    public ForeignKeysStep primaryKey(Column<?> column)
    {
        this.primaryKey = column;
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
        builder.appendCreateTable(table);
        for (Column<?> column : columns)
        {
            builder.appendCreateColumn(column, column.equals(primaryKey));
        }
        builder.appendPrimaryKey(primaryKey);
        return new SQLStatement(builder.toString(), builder.getParameters().toArray());
    }

    @Override
    public ForeignKeysStep foreignKey(String name, Column<?>... column)
    {
        foreignKey = new ForeignKey(name, table, column);
        return this;
    }

    @Override
    public ForeignKeysStep references(Table otherTable, Column<?>... column)
    {
        foreignKey.references(otherTable, column);
        if(foreignKeys == null) foreignKeys = new ArrayList<>();
        foreignKeys.add(foreignKey);
        foreignKey = null;
        return this;
    }
}
