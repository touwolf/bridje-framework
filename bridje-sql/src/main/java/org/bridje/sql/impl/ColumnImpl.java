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

import org.bridje.sql.BooleanColumn;
import org.bridje.sql.Column;
import org.bridje.sql.NumberColumn;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLType;
import org.bridje.sql.StringColumn;
import org.bridje.sql.Table;

class ColumnImpl<T> extends ExpressionBase<T> implements Column<T>, NumberColumn<T>, StringColumn<T>, BooleanColumn<T>
{
    private final Table table;

    private final String name;

    private final boolean key;

    private final boolean allowNull;

    private final SQLType<T> sqlType;

    private final boolean autoIncrement;

    private final T defValue;

    public ColumnImpl(Table table, String name, SQLType<T> sqlType, boolean key, boolean allowNull, boolean autoIncrement, T defValue)
    {
        super(sqlType.getJavaType());
        this.table = table;
        this.sqlType = sqlType;
        this.name = name;
        this.key = key;
        this.allowNull = allowNull;
        this.autoIncrement = autoIncrement;
        this.defValue = defValue;
    }

    @Override
    public Table getTable()
    {
        return table;
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

    @Override
    public boolean isAllowNull()
    {
        return allowNull;
    }

    @Override
    public SQLType<T> getSQLType()
    {
        return sqlType;
    }

    @Override
    public boolean isAutoIncrement()
    {
        return autoIncrement;
    }

    @Override
    public T getDefValue()
    {
        return defValue;
    }

    @Override
    public Class<T> getType()
    {
        return sqlType.getJavaType();
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.appendObjectName(name);
    }
}
