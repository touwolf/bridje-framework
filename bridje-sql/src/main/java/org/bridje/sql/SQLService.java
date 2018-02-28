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

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * The service for the SQL API.
 */
public interface SQLService
{
    /**
     * Creates a new SQL environment.
     *
     * @param dataSource The datasource for the new environment.
     *
     * @return The new created SQL environment.
     *
     * @throws SQLException If any SQL error occurs.
     */
    SQLEnvironment createEnvironment(DataSource dataSource) throws SQLException;

    /**
     * Creates a new SQL environment.
     *
     * @param connection The JDBC connection to use.
     *
     * @return The new created SQL environment.
     *
     * @throws SQLException If any SQL error occurs.
     */
    SQLEnvironment createEnvironment(Connection connection) throws SQLException;

    /**
     * Creates a new SQL environment.
     *
     * @param dataSourceName The name of the datasource.
     *
     * @return The new created SQL environment.
     *
     * @throws SQLException If any SQL error occurs.
     */
    SQLEnvironment createEnvironment(String dataSourceName) throws SQLException;

    /**
     * Creates a new SQL environment.
     *
     * @param schemName The name of the schema.
     *
     * @return The new created SQL environment.
     *
     * @throws SQLException If any SQL error occurs.
     */
    SQLEnvironment createEnvironmentBySchema(String schemName) throws SQLException;
}
