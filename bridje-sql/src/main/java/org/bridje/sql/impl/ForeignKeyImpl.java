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

import org.bridje.sql.Column;
import org.bridje.sql.ForeignKey;
import org.bridje.sql.ForeignKeyStrategy;
import org.bridje.sql.Table;

public class ForeignKeyImpl implements ForeignKey
{
    private final String name;

    private Table table;

    private final Column<?>[] columns;

    private final Table referencesTable;

    private final Column<?>[] referencesColumns;

    private final ForeignKeyStrategy onUpdate;

    private final ForeignKeyStrategy onDelete;

    public ForeignKeyImpl(String name, Table table, Column<?>[] columns, Table referencesTable, Column<?>[] referencesColumns, ForeignKeyStrategy onUpdate, ForeignKeyStrategy onDelete)
    {
        this.name = name;
        this.table = table;
        this.columns = columns;
        this.referencesTable = referencesTable;
        this.referencesColumns = referencesColumns;
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Table getTable()
    {
        return table;
    }

    void setTable(Table table)
    {
        this.table = table;
    }

    @Override
    public Column<?>[] getColumns()
    {
        return columns;
    }

    @Override
    public Table getReferencesTable()
    {
        return referencesTable;
    }

    @Override
    public Column<?>[] getReferencesColumns()
    {
        return referencesColumns;
    }

    @Override
    public ForeignKeyStrategy getOnUpdate()
    {
        return onUpdate;
    }

    @Override
    public ForeignKeyStrategy getOnDelete()
    {
        return onDelete;
    }
}
