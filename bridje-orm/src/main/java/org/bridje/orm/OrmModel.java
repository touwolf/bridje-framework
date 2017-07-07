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

import java.sql.SQLException;
import java.util.List;

/**
 * This class is the base class for all ORM models, this class is not mean to 
 * serve as a common ground for all models. ORM models need to be specific classes 
 * that extends from this one. Is necesary that child clases of this class have 
 * a constructor with the 3 parameters that the constructor of this class has, 
 * becouse the OrmService will use it to create the object. It recomended that
 * the constructor is private as a way to avoid manual creating of the models, 
 * ORM models should be created only with the OrmService.
 */
public abstract class OrmModel
{
    private final EntityContext context;

    private final List<Class<?>> entities;

    private final List<Table<?>> tables;

    protected OrmModel(EntityContext context, List<Class<?>> entities, List<Table<?>> tables)
    {
        this.context = context;
        this.entities = entities;
        this.tables = tables;
    }

    /**
     * Obtains the entity context.
     * 
     * @return The entity context object.
     */
    public EntityContext getContext()
    {
        return context;
    }

    /**
     * Retrieve all the tables for the entities that are handled by this model.
     * 
     * @return all the tables for the entities that are handled by this model.
     */
    public List<Table<?>> getTables()
    {
        return tables;
    }

    /**
     * Retrieve all the classes of the entities that are handled by this model.
     * 
     * @return all the classes of the entities that are handled by this model.
     */
    public List<Class<?>> getEntities()
    {
        return entities;
    }

    /**
     * Fix all the tables of the model in the database.
     * 
     * @throws java.sql.SQLException If any SQL exception occurs.
     */
    public void fixAllTables() throws SQLException
    {
        Table<?>[] tables = getTables().toArray(new Table<?>[getTables().size()]);
        context.fixTable(tables);
    }

    /**
     * Determines when ever this model has the given entity.
     *
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return true the given class is an entity of this model, false otherwise.
     */
    public <T> boolean haveEntity(Class<T> entity)
    {
        return getEntities().contains(entity);
    }

    /**
     * Clears the internal cache of the entity context, so new queries retrieve
     * fresh data from the database, note that entities returned from this
     * context will be cached, so if you what to reset the context and release
     * memory this method must be call.
     */
    public void clearCache()
    {
        context.clearCache();
    }

    /**
     * Finds the table for the given entity.
     *
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return The table object for the given entity.
     */
    public <T> Table<T> findTable(Class<T> entity)
    {
        return context.findTable(entity);
    }

    /**
     * This method will find an entity given his class and id.
     *
     * @param <T> The type of the entity.
     * @param table The entity table to be find.
     * @param id The id of the entity to be find.
     * @return The finded entity, or null if no entity can be found by that id.
     * @throws SQLException If any SQLException occurs.
     */
    public <T> T find(Table<T> table, Object id) throws SQLException
    {
        return context.find(table, id);
    }

    /**
     * This method will update all the fields of the entity from the actual
     * values in the database.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be refreshed.
     * @return The same entity passed to this method but with the fields
     * refreshed.
     * @throws SQLException If any SQLException occurs.
     */
    public <T> T refresh(T entity) throws SQLException
    {
        return context.refresh(entity);
    }

    /**
     * Creates a new query with the given entity as the base entity for the
     * query, the object returned by this method can be customized to build must
     * common queries you'l whant to execute on the database.
     *
     * @param <T> The type of the entity.
     * @param table The entity table to be query.
     * @return A new Query object.
     */
    public <T> Query<T> query(Table<T> table)
    {
        return context.query(table);
    }
}
