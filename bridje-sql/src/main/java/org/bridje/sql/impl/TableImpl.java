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
import org.bridje.sql.Column;
import org.bridje.sql.ForeignKey;
import org.bridje.sql.Index;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.Schema;
import org.bridje.sql.Table;
import org.bridje.sql.TableExpr;

class TableImpl implements Table
{
    private final String name;

    private Schema schema;

    private final Column<?, ?>[] keys;

    private final Column<?, ?>[] aiColumns;

    private final Column<?, ?>[] columns;

    private final ForeignKey[] foreignKeys;

    private final Index[] indexes;
    
    private final Map<String, Column<?, ?>> columnsMap;

    public TableImpl(String name, Column<?, ?>[] columns, Index[] indexes, ForeignKey[] foreignKeys)
    {
        this.name = name;
        columnsMap = new HashMap<>();
        List<Column<?, ?>> keysList = new ArrayList<>();
        List<Column<?, ?>> aiColumnsList = new ArrayList<>();
        this.columns = columns;
        for (Column<?, ?> column : columns)
        {
            ((ColumnImpl)column).setTable(this);
            if(column.isKey()) keysList.add(column);
            if(column.isAutoIncrement()) aiColumnsList.add(column);
            columnsMap.put(column.getName(), column);
        }
        this.keys = new Column[keysList.size()];
        keysList.toArray(keys);
        this.aiColumns = new Column[aiColumnsList.size()];
        keysList.toArray(aiColumns);
        this.indexes = indexes;
        for (Index index : indexes)
        {
            ((IndexImpl)index).setTable(this);
        }
        this.foreignKeys = foreignKeys;
        for (ForeignKey fk : foreignKeys)
        {
            ((ForeignKeyImpl)fk).setTable(this);
        }
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Schema getSchema()
    {
        return schema;
    }

    void setSchema(Schema schema)
    {
        this.schema = schema;
    }

    @Override
    public Column<?, ?>[] getPrimaryKey()
    {
        return keys;
    }
    
    @Override
    public Column<?, ?>[] getAutoIncrement()
    {
        return aiColumns;
    }

    @Override
    public Column<?, ?>[] getColumns()
    {
        return columns;
    }

    @Override
    public Column<?, ?> getColumn(String name)
    {
        return columnsMap.get(name);
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.appendObjectName(name);
    }

    @Override
    public Index[] getIndexes()
    {
        return indexes;
    }

    @Override
    public ForeignKey[] getForeignKeys()
    {
        return foreignKeys;
    }

    @Override
    public TableExpr as(String alias)
    {
        return new TableAliasImpl(this, alias);
    }
}
