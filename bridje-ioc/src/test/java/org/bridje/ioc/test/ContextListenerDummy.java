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
package org.bridje.ioc.test;

import org.bridje.ioc.ContextListener;
import org.bridje.ioc.Component;

@Component
public class ContextListenerDummy implements ContextListener<DummyComponent>
{
    @Override
    public void preCreateComponent(Class<DummyComponent> clazz)
    {
        System.out.println("This method is called only when DummyComponent is preCreate");
    }

    @Override
    public void preInitComponent(Class<DummyComponent> clazz, DummyComponent instance)
    {
        System.out.println("This method is called only when DummyComponent is preInit");
    }

    @Override
    public void postInitComponent(Class<DummyComponent> clazz, DummyComponent instance)
    {
        System.out.println("This method is called only when DummyComponent is postInit");
    }    
}
