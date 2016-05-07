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

package org.bridje.ioc;

/**
 * This class represents the application scope wich is the root context of
 * bridje ioc.
 */
public final class Application implements Scope
{
    protected Application()
    {
        //Protected constructor
    }

    @Override
    public void preCreateComponent(Class<Object> clazz)
    {
        //Before creating an Application scoped component
    }

    @Override
    public void preInitComponent(Class<Object> clazz, Object instance)
    {
        //Before init an Application scoped component
    }

    @Override
    public void postInitComponent(Class<Object> clazz, Object instance)
    {
        //After init an Application scoped component
    }
}
