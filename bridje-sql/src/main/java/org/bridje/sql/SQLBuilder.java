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

package org.bridje.sql;

import java.util.ArrayList;
import java.util.List;
import org.bridje.sql.dialect.SQLDialect;
import org.bridje.sql.expr.SQLWritable;

public class SQLBuilder
{
    private final StringBuilder builder;

    private final List<Object> parameters;

    private final SQLDialect dialect;

    private boolean simpleColumnNames;

    public SQLBuilder(SQLDialect dialect)
    {
        this.builder = new StringBuilder();
        this.parameters = new ArrayList<>();
        this.dialect = dialect;
    }

    public boolean isSimpleColumnNames()
    {
        return simpleColumnNames;
    }

    public void setSimpleColumnNames(boolean simpleColumnNames)
    {
        this.simpleColumnNames = simpleColumnNames;
    }

    public SQLDialect getDialect()
    {
        return dialect;
    }

    public List<Object> getParameters()
    {
        return parameters;
    }

    public SQLBuilder append(String str)
    {
        builder.append(str);
        return this;
    }

    public SQLBuilder append(CharSequence s)
    {
        builder.append(s);
        return this;
    }

    public SQLBuilder append(char c)
    {
        builder.append(c);
        return this;
    }

    public SQLBuilder append(int i)
    {
        builder.append(i);
        return this;
    }

    public SQLBuilder append(long lng)
    {
        builder.append(lng);
        return this;
    }

    public SQLBuilder append(float f)
    {
        builder.append(f);
        return this;
    }

    public SQLBuilder append(double d)
    {
        builder.append(d);
        return this;
    }

    public SQLBuilder appendObjectName(String name)
    {
        dialect.writeObjectName(builder, name);
        return this;
    }

    public SQLBuilder appendLimit(int offset, int count)
    {
        dialect.writeLimit(builder, offset, count);
        return this;
    }

    public SQLBuilder append(SQLWritable expression)
    {
        expression.writeSQL(this);
        return this;
    }

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
}
