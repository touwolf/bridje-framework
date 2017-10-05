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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.orm.EntityContext;
import org.bridje.orm.ORMConfig;
import org.bridje.orm.ORMEnvironment;
import org.bridje.sql.SQLEnvironment;

class ORMEnvironmentImpl implements ORMEnvironment, EntityContext
{
    private static final Logger LOG = Logger.getLogger(ORMEnvironmentImpl.class.getName());

    private final ORMConfig config;

    private final Map<Class<?>, Object> models;
    
    private final Map<Class<?>, EntityCache> cacheMap;

    public ORMEnvironmentImpl(ORMConfig config)
    {
        this.config = config;
        this.models = new HashMap<>();
        this.cacheMap = new HashMap<>();
    }

    @Override
    public <T> T getModel(Class<T> modelCls)
    {
        T result = (T)models.get(modelCls);
        if(result == null)
        {
            result = createModel(modelCls);
            if(result != null) models.put(modelCls, result);
        }
        return result;
    }

    private <T> T createModel(Class<T> modelCls)
    {
        SQLEnvironment sqlEnv = config.get(modelCls);
        if(sqlEnv == null) return null;
        T result = instantiateModel(modelCls, sqlEnv);
        return result;
    }
    
    private <T> T instantiateModel(Class<T> modelCls, SQLEnvironment env)
    {
        try
        {
            Constructor<T> constructor = modelCls.getConstructor(EntityContext.class, SQLEnvironment.class);
            return constructor.newInstance(this, env);
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> boolean contains(Class<T> entity, Object id)
    {
        EntityCache cache = cacheMap.get(entity);
        if(cache != null) return cache.contains(id);
        return false;
    }

    @Override
    public <T> T get(Class<T> entity, Object id)
    {
        EntityCache cache = cacheMap.get(entity);
        if(cache != null) return (T)cache.get(id);
        return null;
    }

    @Override
    public <T> void put(Object id, T entity)
    {
        if(entity == null) return;
        EntityCache cache = cacheMap.get(entity.getClass());
        if(cache == null)
        {
            cache = new EntityCache();
            cacheMap.put(entity.getClass(), cache);
        }
        cache.put(id, entity);
    }

    @Override
    public <T> void remove(Class<T> entity, Object id)
    {
        EntityCache cache = cacheMap.get(entity);
        if(cache != null) cache.remove(id);
    }

    @Override
    public void clear()
    {
        cacheMap.clear();
    }
}
