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

package org.bridje.core.ioc;

import org.bridje.core.impl.ioc.ContextFactory;

/**
 * Facade for the Bridje IoC API.
 * <p>
 * This class provides the method context() which will deliver the IocContext
 * for the application scope.
 */
public class Ioc
{
    /**
     * Private constructor so this object cannot be instantiated.
     */
    private Ioc()
    {
    }

    /**
     * This method initiates the {@link IocContext} for the APPLICATION scope,
     * and must be called one at the begining of the application, before any
     * call to the {@link Ioc#context()} method, in order to register default
     * implementations for especific services.
     * <p>
     * @param registers An array of {@link Register} that defines the default
     *                  implementations to by use when looking for specific
     *                  services in the {@link IocContext}.
     * <p>
     * @return The APPLICATION scoped {@link IocContext} instance for this
     *         application.
     */
    public static IocContext init(Register... registers)
    {
        return ContextFactory.context(registers);
    }

    /**
     * This method returns the {@link IocContext} for the application scope. You
     * may call this method the number of times you whant. in contrast with the
     * {@link Ioc#init(org.bridje.core.ioc.impl.Register...)} witch may be call
     * only once before this method is ever call.
     * <p>
     * @return The APPLICATION scoped {@link IocContext} instance for this
     *         application.
     */
    public static IocContext context()
    {
        return ContextFactory.context();
    }
}
