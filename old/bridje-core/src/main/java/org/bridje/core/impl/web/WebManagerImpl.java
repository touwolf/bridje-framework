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

package org.bridje.core.impl.web;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.core.web.HttpException;
import org.bridje.core.web.WebManager;
import org.bridje.core.web.WebRequest;
import org.bridje.core.web.WebRequestChain;
import org.bridje.core.web.WebRequestHandler;
import org.bridje.core.web.WebResponse;

@Component
class WebManagerImpl implements WebManager
{
    private static final Logger LOG = Logger.getLogger(WebManagerImpl.class.getName());

    @Inject
    private WebRequestHandler[] handlersList;

    @Inject
    private IocContext context;

    @Override
    public void proccess(WebRequest req, WebResponse resp)
    {
        try
        {
            IocContext reqContext = context.createChild("WEBREQUEST");
            WebRequestChain chain = new WebRequestChainImpl(req, resp, reqContext, handlersList);
            chain.procced();
            if(!resp.isProcessed())
            {
                writeError(404, "Not Found", resp);
            }
        }
        catch(HttpException ex)
        {
            if(!resp.isProcessed())
            {
                writeError(ex.getCode(), ex.getMessage(), resp);
            }
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            if(!resp.isProcessed())
            {
                writeError(500, ex.getMessage(), resp);
            }
        }
    }

    private void writeError(int code, String message, WebResponse resp)
    {
        resp.setStatusCode(code);
        try (PrintWriter w = new PrintWriter(resp.getOutputStream()))
        {
            w.print(code);
            w.print(" - ");
            w.print(message);
            w.flush();
        }
        catch(Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
}
