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

package org.bridje.ioc.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class IocContainer
{
    private final Map<Class, Object> components;

    private final ComponentCreator creator;

    public IocContainer(ComponentCreator creator, List instances)
    {
        this.components = new ConcurrentHashMap<>();
        this.creator = creator;
        for (Object instance : instances)
        {
            components.put(instance.getClass(), instance);
            creator.injectDependencies(instance.getClass(), instance);
        }
    }

    public boolean contains(Class cls)
    {
        return components.containsKey(cls);
    }

    public <T> T get(Class<T> cls)
    {
        return (T)components.get(cls);
    }

    public <T> T create(Class<T> cls)
    {
        if(components.containsKey(cls))
        {
            return (T)components.get(cls);
        }
        else
        {
            T obj = creator.instantiate(cls);
            if(obj == null)
            {
                return null;
            }
            components.put(cls, obj);
            creator.injectDependencies(cls, obj);
            return obj;
        }
    }
}
