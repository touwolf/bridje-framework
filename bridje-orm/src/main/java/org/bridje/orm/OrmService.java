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

import java.util.List;
import javax.sql.DataSource;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.ioc.thls.ThlsActionException;
import org.bridje.ioc.thls.ThlsActionException2;

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
     * @param <T>
     * @param dsName The name of the datasource.
     * @param modelCls
     * @return The new created entity context.
     */
    <T extends OrmModel> T createModel(String dsName, Class<T> modelCls);

    /**
     * Creates an entity context from the actual datasource that must be use to
     * access the database.
     *
     * @param <T>
     * @param ds The data source to use in the entity context.
     * @param modelCls
     * @return The new created entity context.
     */
    <T extends OrmModel> T createModel(DataSource ds, Class<T> modelCls);

    /**
     * Finds the table for the given entity.
     *
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return The table object for the given entity.
     */
    <T> Table<T> findTable(Class<T> entity);
    
    /**
     * 
     * @param <T>
     * @param entity
     * @return 
     */
    <T> Class<? extends OrmModel> findModelClass(Class<T> entity);

    /**
     * 
     * @param <T>
     * @param modelClass
     * @return 
     */
    <T extends OrmModel> List<Class<?>> findEntitys(Class<T> modelClass);

    /**
     * 
     * @param <T>
     * @param modelClass
     * @return 
     */
    <T extends OrmModel> List<Table<?>> findTables(Class<T> modelClass);

    /**
     * 
     * @param <T>
     * @param action
     * @param setup
     * @return 
     */
    <T> T doWithModels(ThlsAction<T> action, DataSourcesSetup setup);

    /**
     * 
     * @param <T>
     * @param <E>
     * @param action
     * @param setup
     * @return 
     * @throws E 
     */
    <T, E extends Throwable> T doWithModelsEx(ThlsActionException<T, E> action, DataSourcesSetup setup) throws E;

    /**
     * 
     * @param <T>
     * @param <E>
     * @param <E2>
     * @param action
     * @param setup
     * @return 
     * @throws E 
     * @throws E2 
     */
    <T, E extends Throwable, E2 extends Throwable> T doWithModelsEx2(ThlsActionException2<T, E, E2> action, DataSourcesSetup setup) throws E, E2;

    /**
     * 
     * @param <T>
     * @param modelClass
     * @return 
     */
    <T extends OrmModel> T getModel(Class<T> modelClass);
}
