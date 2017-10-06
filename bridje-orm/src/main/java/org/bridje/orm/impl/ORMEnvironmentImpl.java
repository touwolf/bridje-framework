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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.ioc.thls.ThlsActionException;
import org.bridje.ioc.thls.ThlsActionException2;
import org.bridje.orm.EntityContext;
import org.bridje.orm.ORMEnvironment;
import org.bridje.sql.SQLEnvironment;

class ORMEnvironmentImpl implements ORMEnvironment, EntityContext
{
    private static final Logger LOG = Logger.getLogger(ORMEnvironmentImpl.class.getName());

    private final EnvironmentBuilderImpl config;

    private final Map<Class<?>, Object> models;

    private final Map<Class<?>, EntityCache> cacheMap;

    private final Map<Class<?>, List<Field>> fieldsMap;
    
    private final Map<Class<?>, Constructor<?>> contructorsMap;

    public ORMEnvironmentImpl(EnvironmentBuilderImpl config)
    {
        this.config = config;
        this.models = new HashMap<>();
        this.cacheMap = new HashMap<>();
        this.fieldsMap = new HashMap<>();
        this.contructorsMap = new HashMap<>();
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
            Constructor<?> defConst = getDefConstructor(modelCls);
            if(defConst != null)
            {
                T object = (T)defConst.newInstance();
                injectModels(object, env);
                return object;
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException | InvocationTargetException e)
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
    public <T> void clear(Class<T> entity)
    {
        EntityCache cache = cacheMap.get(entity);
        if(cache != null) cache.clear();
    }

    @Override
    public void clear()
    {
        cacheMap.clear();
    }

    private <T> void injectModels(T object, SQLEnvironment env)
    {
        List<Field> lst = getFieldList(object.getClass());
        for (Field field : lst)
        {
            Object value = getValueForField(field, env);
            setFieldValue(field, object, value);
        }
    }

    private List<Field> getFieldList(Class<?> cls)
    {
        List<Field> result = fieldsMap.get(cls);
        if(result == null)
        {
            result = findFieldList(cls);
            if(result != null) fieldsMap.put(cls, result);
        }
        return result;
    }

    private List<Field> findFieldList(Class<?> cls)
    {
        List<Field> fieldsLst = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields)
        {
            Inject inject = field.getAnnotation(Inject.class);
            if(inject != null)
            {
                fieldsLst.add(field);
            }
        }
        return fieldsLst;
    }

    private Object getValueForField(Field field, SQLEnvironment env)
    {
        if(field.getType() == EntityContext.class)
        {
            return this;
        }
        else if(field.getType() == SQLEnvironment.class)
        {
            return env;
        }
        else 
        {
            if(config.contains(field.getType()))
            {
                return getModel(field.getType());
            }
            else
            {
                return Ioc.context().findGeneric(field.getGenericType());
            }
        }
    }

    private void setFieldValue(Field field, Object comp, Object value)
    {
        if(value != null)
        {
            try
            {
                field.setAccessible(true);
                field.set(comp, value);
            }
            catch (IllegalAccessException | IllegalArgumentException | SecurityException e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private Constructor<?> findDefConstructor(Class<?> modelCls)
    {
        for (Constructor<?> constructor : modelCls.getDeclaredConstructors())
        {
            constructor.setAccessible(true);
            if(constructor.getParameterTypes().length == 0)
            {
                return constructor;
            }
        }
        return null;
    }

    private <T> Constructor<?> getDefConstructor(Class<T> modelCls)
    {
        Constructor<?> defConst = contructorsMap.get(modelCls);
        if(defConst == null)
        {
            defConst = findDefConstructor(modelCls);
            if(defConst != null) contructorsMap.put(modelCls, defConst);
        }
        return defConst;
    }

    @Override
    public <T> T doWith(ThlsAction<T> action)
    {
        return Thls.doAs(action, ORMEnvironment.class, this);
    }

    @Override
    public <T, E extends Throwable> T doWithEx(ThlsActionException<T, E> action) throws E
    {
        return Thls.doAsEx(action, ORMEnvironment.class, this);
    }

    @Override
    public <T, E extends Throwable, E2 extends Throwable> T doWithEx2(ThlsActionException2<T, E, E2> action) throws E, E2
    {
        return Thls.doAsEx2(action, ORMEnvironment.class, this);
    }
}
