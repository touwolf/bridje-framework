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
 * A service interface to access the ORM features. This interfaces can create
 * ORM models objects so you can manage its entitys, it can also put the models 
 * in the current thread so they are accesible from any method without injecting 
 * it or passing it in arguments.
 */
public interface OrmService
{
    /**
     * Creates a new ORM model of the given class from the name of the datasource 
     * that must be use to access the database. 
     * The name of the datasource must be a valid name that the 
     * {@link org.bridje.jdbc.JdbcService} can return.
     *
     * @param <T> The type of the ORM model.
     * @param dsName The name of the datasource.
     * @param modelCls The model class.
     * @return The new created model of the given class.
     */
    <T extends OrmModel> T createModel(String dsName, Class<T> modelCls);

    /**
     * Creates a new ORM model of the given class from the actual datasource that must be use to
     * access the database.
     *
     * @param <T> The type of the ORM model.
     * @param ds The data source to use in the entity context.
     * @param modelCls The model class.
     * @return The new created model of the given class.
     */
    <T extends OrmModel> T createModel(DataSource ds, Class<T> modelCls);

    /**
     * Creates a new ORM model of the given class from the actual datasource that must be use to
     * access the database.
     * 
     * @param <T> The type of the ORM model.
     * @param ctx The current entity context.
     * @param modelCls The model class.
     * @return The new created model of the given class.
     */
    <T extends OrmModel> T createModel(EntityContext ctx, Class<T> modelCls);

    /**
     * Determines if the given class is an entity class.
     * 
     * @param cls The entity class to test.
     * @return true the class is an entity, false otherwise.
     */
    boolean isEntityClass(Class<?> cls);

    /**
     * Finds the table for the given entity.
     * 
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return The table object for the given entity.
     */
    <T> Table<T> findTable(Class<T> entity);

    /**
     * Finds the class for the model of the given entity.
     * 
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return The class of the ORM model.
     */
    <T> Class<? extends OrmModel> findModelClass(Class<T> entity);

    /**
     * Gets all the entitys associated with the given model.
     * 
     * @param <T> The type of the model.
     * @param modelClass The model class.
     * @return A list with all the entity classes for the given ORM model class.
     */
    <T extends OrmModel> List<Class<?>> findEntitys(Class<T> modelClass);

    /**
     * Gets the list of all the tables associated with the given model.
     * 
     * @param <T> The type of the model.
     * @param modelClass The class of the model.
     * @return The list with all the tables for the ORM model.
     */
    <T extends OrmModel> List<Table<?>> findTables(Class<T> modelClass);

    /**
     * Executes a Thls action with the data sources configuration given so the action can be execute in a ORM model provided environment. 
     * 
     * @param <T> The type for the result.
     * @param action The action to execute.
     * @param setup The data sources setup for the ORM models.
     * @return The same object returned by the action.
     */
    <T> T doWithModels(ThlsAction<T> action, DataSourcesSetup setup);

    /**
     * Executes a Thls action with the data sources configuration given so the action can be execute in a ORM model provided environment. 
     * 
     * @param <T> The type for the result.
     * @param <E> The type for the first exception.
     * @param action The action to execute.
     * @param setup The data sources setup for the ORM models.
     * @return The same object returned by the action.
     * @throws E The first exception.
     */
    <T, E extends Throwable> T doWithModelsEx(ThlsActionException<T, E> action, DataSourcesSetup setup) throws E;

    /**
     * Executes a Thls action with the data sources configuration given so the action can be execute in a ORM model provided environment. 
     * 
     * @param <T> The type for the result.
     * @param <E> The type for the first exception.
     * @param <E2> The type for the second exception.
     * @param action The action to execute.
     * @param setup The data sources setup for the ORM models.
     * @return The same object returned by the action.
     * @throws E The first exception.
     * @throws E2 The second exception.
     */
    <T, E extends Throwable, E2 extends Throwable> T doWithModelsEx2(ThlsActionException2<T, E, E2> action, DataSourcesSetup setup) throws E, E2;

    /**
     * Gets the current thread saved ORM model for its class.
     * 
     * @param <T> The type of the ORM model.
     * @param modelClass The model class.
     * @return The current thread saved model if it exists or null if not.
     */
    <T extends OrmModel> T getModel(Class<T> modelClass);

    /**
     * Gets the ORM model in the current thread for given the entity class.
     * 
     * @param entityClass The class of entity whos model needs to be found.
     * @return The Orm model save in the current thread for the given entity.
     */
    OrmModel getModelForEntity(Class<?> entityClass);
}
