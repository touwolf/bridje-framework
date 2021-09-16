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

import org.bridje.sql.*;

class TableAliasImpl implements Table
{
    private TableImpl table;

    private String alias;

    public TableAliasImpl(TableImpl table, String alias)
    {
        this.table = table;
        this.alias = alias;
    }

    @Override
    public String getName()
    {
        return this.table.getName();
    }

    @Override
    public Schema getSchema()
    {
        return this.table.getSchema();
    }

    @Override
    public Column<?, ?>[] getPrimaryKey()
    {
        return this.table.getPrimaryKey();
    }

    @Override
    public Column<?, ?>[] getAutoIncrement()
    {
        return this.table.getAutoIncrement();
    }

    @Override
    public Column<?, ?>[] getColumns()
    {
        return this.table.getColumns();
    }

    @Override
    public Column<?, ?> getColumn(String name)
    {
        return this.table.getColumn(name);
    }

    @Override
    public Index[] getIndexes()
    {
        return this.table.getIndexes();
    }

    @Override
    public ForeignKey[] getForeignKeys()
    {
        return this.table.getForeignKeys();
    }

    @Override
    public TableExpr as(String alias)
    {
        this.alias = alias;
        return this;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        this.table.writeSQL(builder);
        builder.append(" AS ");
        builder.appendObjectName(alias);
    }

    @Override
    public String getAlias()
    {
        return this.alias;
    }


}
