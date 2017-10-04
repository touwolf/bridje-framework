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

import org.bridje.sql.ForeignKey;
import org.bridje.sql.Index;
import org.bridje.sql.Schema;
import org.bridje.sql.Table;

class SchemaImpl implements Schema
{
    private final String name;

    private final Table[] tables;

    private final Index[] indexes;

    private final ForeignKey[] foreignKeys;

    public SchemaImpl(String name, Table[] tables, Index[] indexes, ForeignKey[] foreignKeys)
    {
        this.name = name;
        this.tables = tables;
        this.indexes = indexes;
        this.foreignKeys = foreignKeys;
        for (Table table : tables)
        {
            ((TableImpl)table).setSchema(this);
        }
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Table[] getTables()
    {
        return tables;
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
}
