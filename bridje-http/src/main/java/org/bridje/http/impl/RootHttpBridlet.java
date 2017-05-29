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

package org.bridje.http.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.http.HttpException;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE)
class RootHttpBridlet implements HttpBridlet
{
    private static final Logger LOG = Logger.getLogger(RootHttpBridlet.class.getName());

    @InjectNext
    private HttpBridlet handler;
    
    @Override
    public boolean handle(HttpBridletContext context) throws IOException
    {
        HttpBridletRequest req = context.getRequest();
        HttpBridletResponse resp = context.getResponse();
        LOG.log(Level.INFO, "{0} {1} {2}", new Object[]{req.getMethod(), req.getPath(), req.getProtocol()});
        try
        {
            if(handler == null || !handler.handle(context))
            {
                throw new HttpException(404, "Not Found");
            }
        }
        catch (HttpException e)
        {
            LOG.log(Level.WARNING, "{0} {1} {2} - {3} {4}", 
                        new Object[]{req.getMethod(), req.getPath(), req.getProtocol(), e.getStatus(), e.getMessage()});
            resp.setStatusCode(e.getStatus());
            try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream()))
            {
                writer.append("<h1>" + e.getStatus() + " - " + e.getMessage() + "</h1>");
                writer.flush();
            }
        }

        return true;            
    }
    
}
