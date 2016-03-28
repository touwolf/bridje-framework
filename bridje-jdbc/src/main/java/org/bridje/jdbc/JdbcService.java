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
 * class can be configure from the {@link org.bridje.jdbc.config.JdbcConfig}
 * class witch will be returned via bridje configuration API.
 */
public interface JdbcService
{

    /**
     * Retrives a pooled DataSource object associeted with the given name.
     *
     * @param name The name of the DataSource that was especify in the jdbc.xml
     * configuration file.
     * @return The DataSource object if it was configure in the jdbc.xml or null
     * the no DataSource was configure by the given name.
     */
    DataSource getDataSource(String name);

    /**
     * Creates a new ppoled DataSource objecto out of the given configuration.
     *
     * @param config The DataSource configuration parameters.
     * @return The new create DataSource object.
     */
    DataSource createDataSource(DataSourceConfig config);

    /**
     * Close the given DataSource and all of it´s connections to the database.
     *
     * @param dataSource The DataSource to be close.
     * @throws SQLException If any SQLException occurs during the close
     * proccess.
     */
    void closeDataSource(DataSource dataSource) throws SQLException;

    /**
     * Close all the DataSources stored in the JdbcService.
     */
    void closeAllDataSource();
}
