/*
 * Copyright 2016 Bridje Framework.
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Priority;
import org.bridje.web.WebMethod;
import org.bridje.web.WebRequestScope;

@Component
@Priority(100)
class ControllerHandler implements HttpServerHandler
{
    private static final Logger LOG = Logger.getLogger(ControllerHandler.class.getName());

    private List<WebMethodData> methodsData;

    @Override
    public boolean handle(HttpServerContext context) throws IOException
    {
        HttpServerRequest req = context.get(HttpServerRequest.class);
        IocContext<WebRequestScope> wrsCtx = context.get(IocContext.class);
        if(methodsData == null)
        {
            initMethods(wrsCtx);
        }
        invokeMethod(wrsCtx, req.getPath());
        return false;
    }

    private synchronized void initMethods(IocContext<WebRequestScope> wrsCtx)
    {
        if(methodsData == null)
        {
            methodsData = new ArrayList<>();
            wrsCtx.getClassRepository().forEachMethod(WebMethod.class, 
                    (Method method, Class component, WebMethod annotation) ->
                    {
                        methodsData.add(new WebMethodData(
                                            annotation.value(), component, method));
                    });
        }
    }

    private Object invokeMethod(IocContext<WebRequestScope> wrsCtx, String path)
    {
        for (WebMethodData methodData : methodsData)
        {
            Object[] matches = methodData.matches(path);
            if(matches != null)
            {
                Object cmp = wrsCtx.find(methodData.getComponent());
                try
                {
                    return methodData.getMethod().invoke(cmp, matches);
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return null;
    }
}