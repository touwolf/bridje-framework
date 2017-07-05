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

package org.bridje.orm.impl;

import java.sql.SQLException;
import org.bridje.orm.EntityContext;
import org.bridje.orm.OrmModel;
import org.bridje.orm.Table;

public class TestOrmModel implements OrmModel
{
    private final EntityContext ctx;

    private TestOrmModel(EntityContext ctx)
    {
        this.ctx = ctx;
    }
    
    @Override
    public EntityContext getContext()
    {
        return ctx;
    }

    @Override
    public Table<?>[] tables()
    {
        return new Table<?>[] {Group.TABLE, Rol.TABLE, User.TABLE};
    }

    @Override
    public void fixAllTables() throws SQLException
    {
        ctx.fixTable(tables());
    }

    @Override
    public <T> boolean haveEntity(Class<T> entity)
    {
        return true;
    }
}
