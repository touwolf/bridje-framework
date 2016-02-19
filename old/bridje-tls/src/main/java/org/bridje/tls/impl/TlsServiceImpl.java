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

package org.bridje.tls.impl;

import org.bridje.ioc.Component;
import org.bridje.tls.TlsAction;
import org.bridje.tls.TlsService;

@Component
class TlsServiceImpl implements TlsService
{
    private final ThreadLocalStorage threadLocalStorage;

    public TlsServiceImpl()
    {
        threadLocalStorage = new ThreadLocalStorage();
    }
    
    @Override
    public <T> T doAs(TlsAction<T> action, Object... data) throws Exception
    {
        for (Object obj : data)
        {
            threadLocalStorage.put(obj);
        }
        try
        {
            return action.execute();
        }
        catch(Exception ex)
        {
            throw ex;
        }
        finally
        {
            for (Object obj : data)
            {
                threadLocalStorage.pop(obj.getClass());
            }
        }
    }

    @Override
    public <T> T get(Class<T> cls)
    {
        return threadLocalStorage.get(cls);
    }
}
