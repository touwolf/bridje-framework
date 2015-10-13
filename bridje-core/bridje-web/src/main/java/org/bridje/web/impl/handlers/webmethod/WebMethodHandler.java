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

package org.bridje.web.impl.handlers.webmethod;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Priority;
import org.bridje.web.WebRequestChain;
import org.bridje.web.WebRequestHandler;

/**
 *
 * @author Gilberto
 */
@Component
@Priority(950)
class WebMethodHandler implements WebRequestHandler
{
    private static final Logger LOG = Logger.getLogger(WebMethodHandler.class.getName());

    private WebMethodNode rootNode;

    @Override
    public Object proccess(WebRequestChain chain)
    {
        if(rootNode == null)
        {
            rootNode = WebMethodNode.createWebMethodsTree(chain.getRequestContext());
        }
        String requestedPath = chain.getRequest().getRequestedPath();
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
            return chain.procced();
        }
        Object compObj = chain.getRequestContext().find(webMethodInf.getCompClass());
        try
        {
            webMethodInf.getMethod().setAccessible(true);
            return webMethodInf.getMethod().invoke(compObj);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}
