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

import java.util.List;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.orm.EntityContext;
import org.bridje.orm.OrmModel;
import org.bridje.orm.OrmService;
import org.bridje.orm.Table;

public class TestOrmModel extends OrmModel
{
    private TestOrmModel(EntityContext context, List<Class<?>> entities, List<Table<?>> tables)
    {
        super(context, entities, tables);
    }
    
    public static TestOrmModel create(DataSource ds)
    {
        return Ioc.context().find(OrmService.class).createModel(ds, TestOrmModel.class);
    }

    public static TestOrmModel create(String ds)
    {
        return Ioc.context().find(OrmService.class).createModel(ds, TestOrmModel.class);
    }
}
