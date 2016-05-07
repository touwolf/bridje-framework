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
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE)
class RootServerHandler implements HttpServerHandler
{
    private static final Logger LOG = Logger.getLogger(RootServerHandler.class.getName());

    @InjectNext
    private HttpServerHandler handler;
    
    @Override
    public boolean handle(HttpServerContext context) throws IOException
    {
        HttpServerRequest req = context.get(HttpServerRequest.class);
        HttpServerResponse resp = context.get(HttpServerResponse.class);
        LOG.log(Level.INFO, "{0} {1} {2}", new Object[]{req.getMethod(), req.getPath(), req.getProtocol()});
        if(handler == null || !handler.handle(context))
        {
            LOG.log(Level.WARNING, "{0} {1} {2} - 404 Not Found", 
                        new Object[]{req.getMethod(), req.getPath(), req.getProtocol()});
            resp.setStatusCode(404);
            try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream()))
            {
                writer.append("<h1>404 - Not Found</h1>");
                writer.flush();
            }
        }
        return true;            
    }
    
}
