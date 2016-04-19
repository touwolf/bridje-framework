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

package org.bridje.orm;

import javax.sql.DataSource;

/**
 * A service interface to access the Orm features. This interfaces can create
 * new EntityContext objects so you can manage your data model with then.
 */
public interface OrmService
{
    /**
     * Creates a entity context from the name of the datasource that must be use
     * to access the database. The name of the datasource must be a valid name
     * that the {@link org.bridje.jdbc.JdbcService} can return.
     *
     * @param dsName The name of the datasource.
     * @return The new created entity context.
     */
    EntityContext createContext(String dsName);

    /**
     * Creates an entity context from the actual datasource that must be use to
     * access the database.
     *
     * @param ds The data source to use in the entity context.
     * @return The new created entity context.
     */
    EntityContext createContext(DataSource ds);
    
    /**
     * 
     * @param <T>
     * @param entity
     * @return 
     */
    <T> Table<T> findTable(Class<T> entity);
}
