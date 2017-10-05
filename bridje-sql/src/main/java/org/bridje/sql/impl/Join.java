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

import org.bridje.sql.BooleanExpr;
import org.bridje.sql.SQLBuilder;
import org.bridje.sql.SQLWritable;
import org.bridje.sql.TableExpr;

class Join implements SQLWritable
{
    private final TableExpr table;

    private final JoinType type;

    private final BooleanExpr<?, ?> on;

    Join(TableExpr table, JoinType type, BooleanExpr<?, ?> on)
    {
        this.table = table;
        this.type = type;
        this.on = on;
    }

    public TableExpr getTable()
    {
        return table;
    }

    public JoinType getType()
    {
        return type;
    }

    public BooleanExpr<?, ?> getOn()
    {
        return on;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        builder.append(type.name());
        builder.append(" JOIN ");
        table.writeSQL(builder);
        builder.append(" ON ");
        on.writeSQL(builder);
    }
}
