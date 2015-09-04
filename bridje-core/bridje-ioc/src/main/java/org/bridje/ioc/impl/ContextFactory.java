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
import org.bridje.ioc.IocContext;

public class ContextFactory
{
    private static final Logger LOG = Logger.getLogger(ContextFactory.class.getName());

    /**
     * Internal holder for the single APPLICATION scoped IocContext instance.
     */
    private static IocContext context;

    /**
     * Private constructor so this object cannot be instantiated.
     */
    private ContextFactory()
    {
        
    }

    /**
     * This method returns the IocContext for the application scope.
     * 
     * @param register
     * @return An object providing de IocContext for the application scope.
     */
    public static IocContext context(Register... register)
    {
        if(context == null)
        {
            context = createApplicationContext(register);
        }
        else
        {
            if(register != null && register.length > 0)
            {
                throw new IllegalStateException("The APPLICATION Context is already created.");
            }
        }
        return context;
    }
    
    private static IocContext createApplicationContext(Register... register)
    {
        try
        {
            return new ContextImpl(register);
        }
        catch(IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
