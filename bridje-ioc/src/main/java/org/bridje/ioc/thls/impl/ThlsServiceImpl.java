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

package org.bridje.ioc.thls.impl;

import org.bridje.ioc.Component;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.ioc.thls.ThlsActionException;
import org.bridje.ioc.thls.ThlsActionException2;
import org.bridje.ioc.thls.ThlsService;

@Component
class ThlsServiceImpl implements ThlsService
{
    private final ThreadLocalStorage threadLocalStorage;

    public ThlsServiceImpl()
    {
        threadLocalStorage = new ThreadLocalStorage();
    }

    @Override
    public <T, D> T doAs(ThlsAction<T> action, Class<D> cls, D data)
    {
        threadLocalStorage.put(cls, data);
        try
        {
            return action.execute();
        }
        finally
        {
            threadLocalStorage.pop(cls);
        }
    }

    @Override
    public <T, D, E extends Throwable> T doAsEx(ThlsActionException<T, E> action, Class<D> cls, D data) throws E
    {
        threadLocalStorage.put(cls, data);
        try
        {
            return action.execute();
        }
        finally
        {
            threadLocalStorage.pop(cls);
        }
    }
    
    @Override
    public <T, D, E extends Throwable, E2 extends Throwable> T doAsEx2(ThlsActionException2<T, E, E2> action, Class<D> cls, D data) throws E, E2
    {
        threadLocalStorage.put(cls, data);
        try
        {
            return action.execute();
        }
        finally
        {
            threadLocalStorage.pop(cls);
        }
    }

    @Override
    public <T> T get(Class<T> cls)
    {
        return threadLocalStorage.get(cls);
    }

}
