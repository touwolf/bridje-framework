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

import java.util.HashMap;
import java.util.Map;

class EntityCache
{
    private final Map<Object, Object> entitysMap;

    public EntityCache()
    {
        entitysMap = new HashMap<>();
    }

    public Object get(Object id)
    {
        return entitysMap.get(id);
    }

    public Object remove(Object id)
    {
        return entitysMap.remove(id);
    }

    public boolean contains(Object id)
    {
        return entitysMap.containsKey(id);
    }

    public void put(Object id, Object entity)
    {
        entitysMap.put(id, entity);
    }
    
    public void clear()
    {
        entitysMap.clear();
    }
}
