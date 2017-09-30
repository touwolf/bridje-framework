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

package org.bridje.sql.impl;

import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.BuildTableStep;
import org.bridje.sql.Column;
import org.bridje.sql.SQLType;
import org.bridje.sql.Table;

class TableBuilder implements BuildTableStep
{
    private final TableImpl table;

    private final List<Column<?>> columns;

    public TableBuilder(String name)
    {
        this.table = new TableImpl(name);
        columns = new ArrayList<>();
    }

    @Override
    public <T> BuildTableStep autoIncrement(String name, SQLType<T> type, boolean key, boolean allowNull)
    {
        columns.add(new ColumnImpl<>(table, name, type, key, allowNull, true, null));
        return this;
    }

    @Override
    public <T> BuildTableStep number(String name, SQLType<T> type, boolean key, boolean allowNull, T defValue)
    {
        columns.add(new ColumnImpl<>(table, name, type, key, allowNull, false, defValue));
        return this;
    }

    @Override
    public <T> BuildTableStep string(String name, SQLType<T> type, boolean key, boolean allowNull, T defValue)
    {
        columns.add(new ColumnImpl<>(table, name, type, key, allowNull, false, defValue));
        return this;
    }

    @Override
    public <T> BuildTableStep bool(String name, SQLType<T> type, boolean key, boolean allowNull, T defValue)
    {
        columns.add(new ColumnImpl<>(table, name, type, key, allowNull, false, defValue));
        return this;
    }

    @Override
    public Table build()
    {
        Column<?>[] cols = new Column<?>[columns.size()];
        columns.toArray(cols);
        table.setColumns(cols);
        return table;
    }
}
