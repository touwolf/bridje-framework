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
import org.bridje.sql.BuildSchemaFKsStep;
import org.bridje.sql.BuildSchemaIndexesStep;
import org.bridje.sql.BuildSchemaStep;
import org.bridje.sql.ForeignKey;
import org.bridje.sql.Index;
import org.bridje.sql.Schema;
import org.bridje.sql.Table;

public class SchemaBuilder implements BuildSchemaStep
{
    private final String name;

    private final List<Table> tables;
    
    private final List<Index> indexes;
    
    private final List<ForeignKey> foreignKeys;

    public SchemaBuilder(String name)
    {
        this.name = name;
        this.tables = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
    }
    
    @Override
    public BuildSchemaStep table(Table table)
    {
        tables.add(table);
        return this;
    }

    @Override
    public BuildSchemaIndexesStep index(Index index)
    {
        indexes.add(index);
        return this;
    }

    @Override
    public BuildSchemaFKsStep foreignKey(ForeignKey foreignKey)
    {
        foreignKeys.add(foreignKey);
        return this;
    }

    @Override
    public Schema build()
    {
        Table[] tableArr = new Table[tables.size()];
        tables.toArray(tableArr);
        Index[] indexesArr = new Index[indexes.size()];
        indexes.toArray(indexesArr);
        ForeignKey[] foreignKeysArr = new ForeignKey[foreignKeys.size()];
        foreignKeys.toArray(foreignKeysArr);

        return new SchemaImpl(name, tableArr, indexesArr, foreignKeysArr);
    }
    
}
