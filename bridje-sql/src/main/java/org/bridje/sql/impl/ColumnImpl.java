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

class ColumnImpl<T, E> extends ExpressionBase<T, E> implements Column<T, E>, NumberColumn<T, E>, StringColumn<T, E>, BooleanColumn<T, E>, DateColumn<T, E>
{
    private Table table;

    private final String name;

    private boolean key;

    private final boolean allowNull;

    private boolean autoIncrement;

    private final T defValue;

    public ColumnImpl(String name, SQLType<T, E> sqlType, boolean allowNull, T defValue)
    {
        super(sqlType);
        this.name = name;
        this.key = false;
        this.allowNull = allowNull;
        this.defValue = defValue;
        this.autoIncrement = false;
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
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isKey()
    {
        return key;
    }

    void setKey(boolean key)
    {
        this.key = key;
    }

    @Override
    public boolean isAllowNull()
    {
        if(isKey()) return false;
        return allowNull;
    }

    @Override
    public boolean isAutoIncrement()
    {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement)
    {
        this.autoIncrement = autoIncrement;
    }

    @Override
    public T getDefValue()
    {
        return defValue;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        if(table != null && !builder.isSimpleColumnNames())
        {
            builder.appendObjectName(table.getName());
            builder.append('.');
        }
        builder.appendObjectName(name);
    }

    @Override
    public Expression<T, E> asParam()
    {
        return getSQLType().asParam();
    }

    @Override
    public Column<T, E> forAlias(TableExpr tableAlias)
    {
        return new ColumnAliasImpl<>(tableAlias, this, this.getSQLType());
    }

    @Override
    public Column<?, ?> copyWithName(String name)
    {
        ColumnImpl column = new ColumnImpl<>(name, getSQLType(), allowNull, defValue);
        column.setTable(table);
        column.setAutoIncrement(autoIncrement);
        column.setKey(key);
        return column;
    }
}
