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

package org.bridje.orm.impl.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
class EntitysCache
{
    private final Map<Class<?>, Map<Object, Object>> entitysMap = new HashMap<>();

    public <T> T get(Class<T> entityClass, Object id)
    {
        Map<Object, Object> map = entitysMap.get(entityClass);
        if(map != null)
        {
            return (T)map.get(id);
        }
        return null;
    }

    public <T> boolean exists(Class<T> entityClass, Object id)
    {
        Map<Object, Object> map = entitysMap.get(entityClass);
        if(map != null)
        {
            return map.containsKey(id);
        }
        return false;
    }

    public <T> void put(T entity, Object id)
    {
        Map<Object, Object> map = entitysMap.get(entity.getClass());
        if(map == null)
        {
            map = new HashMap<>();
            entitysMap.put(entity.getClass(), map);
        }
        if(map.get(id) == null)
        {
            map.put(id, entity);
        }
    }
    
    public <T> void remove(Class<T> entityClass, Object id)
    {
        Map<Object, Object> map = entitysMap.get(entityClass);
        if(map != null && map.containsKey(id))
        {
            map.remove(id);
        }
    }

    public void clear()
    {
        entitysMap.clear();
    }
}
