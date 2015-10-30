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
 * Facade to thread local storage (tls) service. {@link TlsService}
 */
public class Tls
{
    private static TlsService tlsServ;

    /**
     * Get the last object of the cls Class that was put in the thread local
     * storage.
     * <p>
     * @param <T> The type of the object to look for.
     * @param cls The class of the object to look for.
     * <p>
     * @return The last object of the cls Class that was put in the thread local
     *         storage, or null if none can be found.
     */
    public static <T> T get(Class<T> cls)
    {
        if (tlsServ == null)
        {
            tlsServ = Ioc.context().find(TlsService.class);
        }
        return tlsServ.get(cls);
    }

    /**
     * This method puts all the data objects on the internal thread
     * local storage an executes the {@link TlsAction}.
     * <p>
     * @param <T>    The type of the resulting object for the action.
     * @param action The action to be executed.
     * @param data   The data that must be available for the action.
     * <p>
     * @return The object returned by the {@link TlsAction#execute()} method.
     * <p>
     * @throws Exception If {@link TlsAction#execute()} throw an exception.
     */
    public static <T> T doAs(TlsAction<T> action, Object... data) throws Exception
    {
        if (tlsServ == null)
        {
            tlsServ = Ioc.context().find(TlsService.class);
        }
        return tlsServ.doAs(action, data);
    }
}
