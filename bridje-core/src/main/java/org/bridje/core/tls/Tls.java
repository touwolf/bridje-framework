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

package org.bridje.core.tls;

import org.bridje.core.ioc.Ioc;

/**
 *
 */
public class Tls
{
    private static TlsService tlsServ;

    public static <T> T get(Class<T> cls)
    {
        if(tlsServ == null)
        {
            tlsServ = Ioc.context().find(TlsService.class);
        }
        return tlsServ.get(cls);
    }
    
    public static <T> T doAs(TlsAction<T> action, Object... data) throws Exception
    {
        if(tlsServ == null)
        {
            tlsServ = Ioc.context().find(TlsService.class);
        }
        return tlsServ.doAs(action, data);
    }
}
