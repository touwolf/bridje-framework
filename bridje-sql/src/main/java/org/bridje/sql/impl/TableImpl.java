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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bridje.sql.BooleanColumn;
import org.bridje.sql.Column;
import org.bridje.sql.NumberColumn;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.StringColumn;
import org.bridje.sql.Table;

class TableImpl implements Table
{
    private final String name;

    private Column<?>[] keys;
    
    private Column<?>[] aiColumns;

    private Column<?>[] columns;

    private final Map<String, Column<?>> columnsMap;

    public TableImpl(String name)
    {
        this.name = name;
        columnsMap = new HashMap<>();
    }

    void setColumns(Column<?>[] columns)
    {
        List<Column<?>> keysList = new ArrayList<>();
        List<Column<?>> aiColumnsList = new ArrayList<>();
        this.columns = columns;
        for (Column<?> column : columns)
        {
            if(column.isKey()) keysList.add(column);
            if(column.isAutoIncrement()) aiColumnsList.add(column);
            columnsMap.put(column.getName(), column);
        }
        this.keys = new Column[keysList.size()];
        keysList.toArray(keys);
        this.aiColumns = new Column[aiColumnsList.size()];
        keysList.toArray(aiColumns);
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Column<?>[] getPrimaryKey()
    {
        return keys;
    }
    
    @Override
    public Column<?>[] getAutoIncrement()
    {
        return aiColumns;
    }

    @Override
    public Column<?>[] getColumns()
    {
        return columns;
    }

    @Override
    public <T> NumberColumn<T> getAsNumber(String name, Class<T> type)
    {
        return (NumberColumn<T>)columnsMap.get(name);
    }

    @Override
    public <T> StringColumn<T> getAsString(String name, Class<T> type)
    {
        return (StringColumn<T>)columnsMap.get(name);
    }

    @Override
    public <T> BooleanColumn<T> getAsBoolean(String name, Class<T> type)
    {
        return (BooleanColumn<T>)columnsMap.get(name);
    }

    @Override
    public <T> Column<T> getColumn(String name, Class<T> type)
    {
        return (Column<T>)columnsMap.get(name);
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.appendObjectName(name);
    }
}
