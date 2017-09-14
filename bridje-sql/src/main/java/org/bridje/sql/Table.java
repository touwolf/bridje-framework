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

import java.util.Objects;
import org.bridje.sql.dialect.SQLDialect;
import org.bridje.sql.expr.TableExpr;

public class Table implements TableExpr
{
    private final String name;

    public Table(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Table other = (Table) obj;
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public void writeSQL(StringBuilder builder, SQLDialect dialect)
    {
        dialect.renderObjectName(builder, name);
    }
}
