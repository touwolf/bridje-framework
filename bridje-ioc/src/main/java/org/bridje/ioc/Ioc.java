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

package org.bridje.ioc;

import org.bridje.ioc.impl.ContextFactory;

/**
 * Facade for the Bridje IoC API.
 * <p>
 * This class provides the method context() which will deliver the IocContext
 * for the application scope.
 */
public class Ioc
{
    private static IocContext<Application> appContext;

    /**
     * Private constructor so this object cannot be instantiated.
     */
    private Ioc()
    {
    }

    /**
     * This method returns the {@link IocContext} for the APPLICATION scoped
     * {@link IocContext}.
     * <p>
     *
     * @return The APPLICATION scoped {@link IocContext} instance for this
     * application.
     */
    public static IocContext<Application> context()
    {
        if (appContext == null)
        {
            appContext = ContextFactory.createApplicationContext(new Application());
        }
        return appContext;
    }
}
