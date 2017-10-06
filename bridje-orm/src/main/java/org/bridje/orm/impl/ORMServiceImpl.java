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

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.orm.EnvironmentBuilder;
import org.bridje.orm.ORMService;
import org.bridje.sql.SQLEnvironment;

@Component
class ORMServiceImpl implements ORMService
{
    @Override
    public EnvironmentBuilder model(Class<?> modelClass, SQLEnvironment env)
    {
        return createEnvironment().model(modelClass, env);
    }

    @Override
    public EnvironmentBuilder model(Class<?> modelClass, DataSource ds) throws SQLException
    {
        return createEnvironment().model(modelClass, ds);
    }

    @Override
    public EnvironmentBuilder model(Class<?> modelClass, Connection conn) throws SQLException
    {
        return createEnvironment().model(modelClass, conn);
    }

    @Override
    public EnvironmentBuilder model(Class<?> modelClass, String dsName) throws SQLException
    {
        return createEnvironment().model(modelClass, dsName);
    }

    private EnvironmentBuilder createEnvironment()
    {
        return new EnvironmentBuilderImpl();
    }
}
