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

import org.bridje.sql.BuildFkFinalStep;
import org.bridje.sql.BuildFkReferencesStep;
import org.bridje.sql.BuildForeignKeyStep;
import org.bridje.sql.Column;
import org.bridje.sql.ForeignKey;
import org.bridje.sql.ForeignKeyStrategy;
import org.bridje.sql.Table;

class ForeignKeyBuilder implements BuildForeignKeyStep, BuildFkReferencesStep, BuildFkFinalStep
{
    private final String name;

    private final Table table;

    private final Column<?>[] columns;

    private Table referencesTable;

    private Column<?>[] referencesColumns;

    private ForeignKeyStrategy onUpdate;

    private ForeignKeyStrategy onDelete;

    public ForeignKeyBuilder(String name, Table table, Column<?>[] columns)
    {
        this.name = name;
        this.table = table;
        this.columns = columns;
    }

    @Override
    public BuildFkReferencesStep references(Table table, Column<?>... columns)
    {
        this.referencesTable = table;
        this.referencesColumns = columns;
        return this;
    }

    @Override
    public BuildFkFinalStep strategy(ForeignKeyStrategy onUpdate, ForeignKeyStrategy onDelete)
    {
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
        return this;
    }
    
    @Override
    public BuildFkFinalStep strategy(ForeignKeyStrategy stategy)
    {
        this.onUpdate = stategy;
        this.onDelete = stategy;
        return this;
    }

    @Override
    public ForeignKey build()
    {
        return new ForeignKeyImpl(name, table, columns, referencesTable, referencesColumns, onUpdate, onDelete);
    }
}
