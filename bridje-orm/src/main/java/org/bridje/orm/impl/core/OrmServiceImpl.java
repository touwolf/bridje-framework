/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.orm.impl.core;

import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.jdbc.JdbcService;
import org.bridje.orm.EntityContext;
import org.bridje.orm.OrmService;
import org.bridje.orm.dialects.SQLDialect;

/**
 *
 */
@Component
class OrmServiceImpl implements OrmService
{
    @Inject
    private JdbcService jdbcServ;

    @Inject
    private OrmMetaInfService metaInf;
    
    @Inject
    private SQLDialect[] dialects;
    
    @Override
    public EntityContext createContext(String dsName)
    {
        return createContext(jdbcServ.getDataSource(dsName));
    }

    @Override
    public EntityContext createContext(DataSource ds)
    {
        if(ds == null)
        {
            throw new IllegalArgumentException("No datasource was specified.");
        }
        for (SQLDialect dialect : dialects)
        {
            if(dialect.canHandle(ds))
            {
                return new EntityContextImpl(ds, dialect);
            }
        }
        throw new IllegalArgumentException("Can´t find a valid dialect for this DataSource");
    }

    @Override
    public Object findKeyValue(Object entity)
    {
        return metaInf.findEntityInf(entity.getClass()).findKeyValue(entity);
    }
}
