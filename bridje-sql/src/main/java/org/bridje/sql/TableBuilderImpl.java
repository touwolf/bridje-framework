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
import org.bridje.sql.expr.SQLType;
import org.bridje.sql.expr.TableBuilder;

class TableBuilderImpl implements TableBuilder
{
    private Table table;

    private List<Column<?>> columns;

    public TableBuilderImpl(String name)
    {
        this.table = new Table(name);
        columns = new ArrayList<>();
    }
    
    @Override
    public <T> TableBuilder number(String name, SQLType<T> type, boolean allowNull, boolean autoIncrement, T defValue)
    {
        columns.add(new NumberColumn(table, name, type, allowNull, autoIncrement, defValue));
        return this;
    }

    @Override
    public <T> TableBuilder string(String name, SQLType<T> type, boolean allowNull, T defValue)
    {
        columns.add(new StringColumn(table, name, type, allowNull, false, defValue));
        return this;
    }

    @Override
    public <T> TableBuilder bool(String name, SQLType<T> type, boolean allowNull, T defValue)
    {
        columns.add(new BooleanColumn(table, name, type, allowNull, false, defValue));
        return this;
    }

    @Override
    public Table build()
    {
        Column<?>[] cols = new Column<?>[columns.size()];
        columns.toArray(cols);
        table.setColumns(cols);
        table = null;
        columns = null;
        return table;
    }
}
