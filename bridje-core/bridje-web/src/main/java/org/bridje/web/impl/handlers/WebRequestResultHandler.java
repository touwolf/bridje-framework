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

package org.bridje.web.impl.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Inject;
import org.bridje.ioc.annotations.Priority;
import org.bridje.web.WebRequestChain;
import org.bridje.web.WebRequestHandler;
import org.bridje.web.WebResultHandler;

/**
 *
 * @author Gilberto
 */
@Component
@Priority(1000)
class WebRequestResultHandler implements WebRequestHandler
{
    private static final Logger LOG = Logger.getLogger(WebResultHandler.class.getName());

    @Inject
    private WebResultHandler[] resultHandlers;
    
    private Map<Class<?>, WebResultHandler<?>> resultHandlersMap;

    @Override
    public Object proccess(WebRequestChain chain)
    {
        try
        {
            Object result = chain.procced();
            if(result != null)
            {
                WebResultHandler resultHandler = getResultHandlersMap().get(result.getClass());
                if(resultHandler != null)
                {
                    resultHandler.handle(result, chain.getResponse(), chain.getRequestContext());
                }
                else
                {
                    LOG.log(Level.SEVERE, "No result handler for the class %s was found", result.getClass().getName());
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public Map<Class<?>, WebResultHandler<?>> getResultHandlersMap()
    {
        if(resultHandlersMap == null)
        {
            resultHandlersMap = new HashMap<>();
            if(resultHandlers != null)
            {
                for (WebResultHandler resultHandler : resultHandlers)
                {
                    resultHandlersMap.put(resultHandler.getHandledClass(), resultHandler);
                }
            }
        }
        return resultHandlersMap;
    }

}
