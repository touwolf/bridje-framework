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
import org.bridje.sql.DateColumn;
import org.bridje.sql.Expression;
import org.bridje.sql.NumberColumn;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLType;
import org.bridje.sql.StringColumn;
import org.bridje.sql.Table;
import org.bridje.sql.TableExpr;

class ColumnAliasImpl<T, E> extends ExpressionBase<T, E> implements Column<T, E>, NumberColumn<T, E>, StringColumn<T, E>, BooleanColumn<T, E>, DateColumn<T, E>
{
    private TableExpr tableAlias;
    
    private final ColumnImpl<T, E> column;

    public ColumnAliasImpl(TableExpr tableAlias, ColumnImpl<T, E> column, SQLType<T, E> sqlType)
    {
        super(sqlType);
        this.tableAlias = tableAlias;
        this.column = column;
    }

    public ColumnImpl<T, E> getColumn()
    {
        return column;
    }
    
    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.appendObjectName(tableAlias.getAlias());
        builder.append(".");
        builder.appendObjectName(this.getName());
    }

    @Override
    public Table getTable()
    {
        return this.column.getTable();
    }

    @Override
    public String getName()
    {
        return this.column.getName();
    }

    @Override
    public boolean isKey()
    {
        return this.column.isKey();
    }

    @Override
    public boolean isAllowNull()
    {
        return this.column.isAllowNull();
    }

    @Override
    public boolean isAutoIncrement()
    {
        return this.column.isAutoIncrement();
    }

    @Override
    public T getDefValue()
    {
        return this.column.getDefValue();
    }

    @Override
    public Expression<T, E> asParam()
    {
        return this.column.asParam();
    }

    @Override
    public Column<T, E> forAlias(TableExpr tableAlias)
    {
        this.tableAlias = tableAlias;
        return this;
    }
}
