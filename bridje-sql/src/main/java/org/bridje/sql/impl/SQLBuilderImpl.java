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
import org.bridje.sql.Column;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLDialect;
import org.bridje.sql.SQLWritable;
import org.bridje.sql.Table;

public class SQLBuilderImpl implements SQLBuilder
{
    private final StringBuilder builder;

    private final List<Object> parameters;

    private final SQLDialect dialect;

    private boolean simpleColumnNames;

    public SQLBuilderImpl(SQLDialect dialect)
    {
        this.builder = new StringBuilder();
        this.parameters = new ArrayList<>();
        this.dialect = dialect;
    }

    @Override
    public boolean isSimpleColumnNames()
    {
        return simpleColumnNames;
    }

    @Override
    public void setSimpleColumnNames(boolean simpleColumnNames)
    {
        this.simpleColumnNames = simpleColumnNames;
    }

    @Override
    public SQLDialect getDialect()
    {
        return dialect;
    }

    @Override
    public List<Object> getParameters()
    {
        return parameters;
    }

    @Override
    public SQLBuilder append(String str)
    {
        builder.append(str);
        return this;
    }

    @Override
    public SQLBuilder append(CharSequence s)
    {
        builder.append(s);
        return this;
    }

    @Override
    public SQLBuilder append(char c)
    {
        builder.append(c);
        return this;
    }

    @Override
    public SQLBuilder append(int i)
    {
        builder.append(i);
        return this;
    }

    @Override
    public SQLBuilder append(long lng)
    {
        builder.append(lng);
        return this;
    }

    @Override
    public SQLBuilder append(float f)
    {
        builder.append(f);
        return this;
    }

    @Override
    public SQLBuilder append(double d)
    {
        builder.append(d);
        return this;
    }

    @Override
    public SQLBuilder appendObjectName(String name)
    {
        dialect.writeObjectName(builder, name);
        return this;
    }

    @Override
    public SQLBuilder appendLimit(int offset, int count)
    {
        dialect.writeLimit(builder, offset, count);
        return this;
    }

    @Override
    public SQLBuilder append(SQLWritable expression)
    {
        expression.writeSQL(this);
        return this;
    }

    @Override
    public void appendAll(SQLWritable[] expressions, String sep)
    {
        boolean first = true;
        for (SQLWritable expression : expressions)
        {
            if(!first) builder.append(sep);
            expression.writeSQL(this);
            first = false;
        }
    }

    @Override
    public String toString()
    {
        return builder.toString();
    }

    @Override
    public SQLBuilder appendCreateTable(Table table)
    {
        dialect.createTable(builder, table);
        return this;
    }

    @Override
    public SQLBuilder appendCreateColumn(Column<?> column, boolean isKey)
    {
        dialect.createColumn(builder, parameters, column, isKey);
        return this;
    }

    @Override
    public SQLBuilder appendPrimaryKey(Column<?> column)
    {
        dialect.primaryKey(builder, column);
        return this;
    }

    @Override
    public SQLBuilder appendAlterTable(Table table)
    {
        dialect.alterTable(builder, table);
        return this;
    }

    @Override
    public SQLBuilder appendAddColumn(Column<?> column, boolean isLast)
    {
        dialect.addColumn(builder, parameters, column, isLast);
        return this;
    }

    @Override
    public SQLBuilder appendDropColumn(Column<?> column, boolean isLast)
    {
        dialect.dropColumn(builder, column, isLast);
        return this;
    }

    @Override
    public SQLBuilder appendChangeColumn(Column<?> column, String oldColumn, boolean isLast)
    {
        dialect.changeColumn(builder, parameters, column, oldColumn, isLast);
        return this;
    }

    @Override
    public SQLBuilder appendCreateIndex(String name, Table table, Column<?>[] columns)
    {
        dialect.createIndex(builder, name, table, columns);
        return this;
    }

    @Override
    public SQLBuilder appendCreateUniqueIndex(String name, Table table, Column<?>[] columns)
    {
        dialect.createUniqueIndex(builder, name, table, columns);
        return this;
    }

    @Override
    public SQLBuilder appendDropIndex(String name, Table table)
    {
        dialect.dropIndex(builder, name, table);
        return this;
    }
}
