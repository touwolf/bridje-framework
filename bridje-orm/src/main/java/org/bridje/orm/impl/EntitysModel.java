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

package org.bridje.orm.impl;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
class EntitysModel
{
    private final String name;

    private final Map<Class<?>, EntityInf<?>> entitysMap;

    public EntitysModel(String name)
    {
        this.name = name;
        this.entitysMap = new HashMap<>();
    }

    public String getName()
    {
        return name;
    }

    protected void addEntity(Class<?> entityClass)
    {
        this.entitysMap.put(entityClass, new EntityInf<>(entityClass));
    }

    protected void fillRelations()
    {
        entitysMap.forEach((k, e) -> e.fillRelations(EntitysModel.this));
    }

    public <T> EntityInf<T> findEntityInf(Class<T> entityClass)
    {
        return (EntityInf<T>)entitysMap.get(entityClass);
    }

    public <T> EntityInf<T> findEntityInf(T entity)
    {
        return findEntityInf((Class<T>)entity.getClass());
    }
}
