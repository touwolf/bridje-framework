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

package org.bridje.web.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Inject;
import org.bridje.web.WebRequest;
import org.bridje.web.WebRequestHandler;
import org.bridje.web.WebResponse;
import org.bridje.web.WebResultHandler;

/**
 *
 * @author Gilberto
 */
@Component
class WebMethodHandler implements WebRequestHandler
{
    private static final Logger LOG = Logger.getLogger(WebMethodHandler.class.getName());

    private WebMethodNode rootNode;
    
    @Inject
    private WebResultHandler[] resultHandlers;
    
    private Map<Class<?>, WebResultHandler<?>> resultHandlersMap;

    @Override
    public boolean proccess(WebRequest req, WebResponse resp, IocContext reqContext)
    {
        if(rootNode == null)
        {
            rootNode = WebMethodNode.createWebMethodsTree(reqContext);
        }
        String requestedPath = req.getRequestedPath();
        requestedPath = WebMethodNode.removeSlashes(requestedPath);
        String[] splitPath = requestedPath.split("//");
        WebMethodNode currNode = rootNode;
        for (int i = 0; i < splitPath.length-1; i++)
        {
            String currPath = splitPath[i];
            currNode = rootNode.getNodes().get(currPath);
            if(currNode == null)
            {
                return false;
            }
        }
        String lastPathMember = splitPath[splitPath.length-1];
        WebMethodInf webMethodInf = currNode.getWebMethods().get(lastPathMember);
        if(webMethodInf == null)
        {
            return false;
        }
        Object compObj = reqContext.find(webMethodInf.getCompClass());
        try
        {
            webMethodInf.getMethod().setAccessible(true);
            Object result = webMethodInf.getMethod().invoke(compObj);
            if(result != null)
            {
                WebResultHandler resultHandler = getResultHandlersMap().get(result.getClass());
                if(resultHandler != null)
                {
                    resultHandler.handle(result, resp, reqContext);
                }
                else
                {
                    LOG.log(Level.SEVERE, "No result handler for the class %s was found", result.getClass().getName());
                }
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return true;
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
