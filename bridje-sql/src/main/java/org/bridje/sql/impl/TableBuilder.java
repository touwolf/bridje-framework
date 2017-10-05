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
import org.bridje.sql.BuildTableColumnsStep;
import org.bridje.sql.BuildTableFKsStep;
import org.bridje.sql.BuildTableIndexesStep;
import org.bridje.sql.BuildTableStep;
import org.bridje.sql.Column;
import org.bridje.sql.ForeignKey;
import org.bridje.sql.Index;
import org.bridje.sql.Table;

class TableBuilder implements BuildTableStep
{
    private final String name;

    private final List<Column<?, ?>> columns;
    
    private final List<Index> indexes;
    
    private final List<ForeignKey> foreignKeys;

    public TableBuilder(String name)
    {
        this.name = name;
        this.columns = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
    }
    
    @Override
    public BuildTableStep key(Column<?, ?> column)
    {
        ((ColumnImpl)column).setKey(true);
        columns.add(column);
        return this;
    }

    @Override
    public BuildTableColumnsStep column(Column<?, ?> column)
    {
        ((ColumnImpl)column).setKey(false);
        columns.add(column);
        return this;
    }

    @Override
    public BuildTableIndexesStep index(Index index)
    {
        indexes.add(index);
        return this;
    }

    @Override
    public BuildTableFKsStep foreignKey(ForeignKey foreignKey)
    {
        foreignKeys.add(foreignKey);
        return this;
    }

    @Override
    public Table build()
    {
        Column<?, ?>[] columnsArr = new Column<?, ?>[columns.size()];
        columns.toArray(columnsArr);
        Index[] indexesArr = new Index[indexes.size()];
        indexes.toArray(indexesArr);
        ForeignKey[] foreignKeysArr = new ForeignKey[foreignKeys.size()];
        foreignKeys.toArray(foreignKeysArr);

        return new TableImpl(name, columnsArr, indexesArr, foreignKeysArr);
    }
    
}
