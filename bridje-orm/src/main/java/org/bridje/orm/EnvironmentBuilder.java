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

package org.bridje.orm;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.bridje.sql.SQLEnvironment;

public interface EnvironmentBuilder
{
    EnvironmentBuilder model(Class<?> modelClass, SQLEnvironment env);

    EnvironmentBuilder model(Class<?> modelClass, DataSource ds) throws SQLException;

    EnvironmentBuilder model(Class<?> modelClass, Connection conn) throws SQLException;

    EnvironmentBuilder model(Class<?> modelClass, String dsName) throws SQLException;

    ORMEnvironment build();
}
