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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Application;
import org.bridje.ioc.IocContext;

/**
 * Factory object to create the application context.
 */
public class ContextFactory
{
    private static final Logger LOG = Logger.getLogger(ContextFactory.class.getName());

    /**
     * Private constructor so this object cannot be instantiated.
     */
    private ContextFactory()
    {
    }

    /**
     * Creates the application scoped IocContext.
     *
     * @param application The application scope object.
     *
     * @return The created IocContext object.
     */
    public static IocContext<Application> createApplicationContext(Application application)
    {
        try
        {
            return new ContextImpl<>(application);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

}
