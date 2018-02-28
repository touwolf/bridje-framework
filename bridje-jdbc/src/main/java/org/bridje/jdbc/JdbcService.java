/*
 * Copyright 2015 Bridje Framework.
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
package org.bridje.jdbc;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.bridje.jdbc.config.DataSourceConfig;

/**
 * Provides services for JDBC data sources. {@link javax.sql.DataSource} This
 * class can be configured from the {@link org.bridje.jdbc.config.JdbcConfig}
 * class which will be returned via bridje configuration API.
 */
public interface JdbcService
{
    /**
     * Retrieves a pooled DataSource object associated with the given name.
     *
     * @param schemaName The name of the model that was specified in the
     *                   jdbc.xml configuration file.
     *
     * @return The DataSource object if it was configured in the jdbc.xml or
     *         null if no DataSource was configure with the given name.
     */
    DataSource getDataSourceBySchema(String schemaName);

    /**
     * Retrieves a pooled DataSource object associated with the given name.
     *
     * @param name The name of the DataSource that was specified in the jdbc.xml
     *             configuration file.
     *
     * @return The DataSource object if it was configured in the jdbc.xml or
     *         null if no DataSource was configure with the given name.
     */
    DataSource getDataSource(String name);

    /**
     * Creates a new pooled DataSource object from the given configuration.
     *
     * @param config The DataSource configuration parameters.
     *
     * @return The new created DataSource object.
     */
    DataSource createDataSource(DataSourceConfig config);

    /**
     * Close the given DataSource and all its database connections.
     *
     * @param dataSource The DataSource to be closed.
     *
     * @throws SQLException If any SQLException occurs during the closing
     *                      process.
     */
    void closeDataSource(DataSource dataSource) throws SQLException;

    /**
     * Close all the DataSources stored in the JdbcService.
     */
    void closeAllDataSource();

}
