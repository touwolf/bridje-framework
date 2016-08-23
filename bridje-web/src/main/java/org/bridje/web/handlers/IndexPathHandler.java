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

package org.bridje.web.handlers;

import java.io.IOException;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;
import org.bridje.web.ReqPathRef;

@Component
@Priority(100)
class IndexPathHandler implements HttpServerHandler
{
    @InjectNext
    private HttpServerHandler nextHandler;

    @Override
    public boolean handle(HttpServerContext context) throws IOException
    {
        String currPath = ReqPathRef.findCurrentPath(context);
        if(currPath == null || currPath.trim().isEmpty() || currPath.trim().equalsIgnoreCase("/"))
        {
            context.set(ReqPathRef.class, new ReqPathRef("/index"));
        }
        return nextHandler.handle(context);
    }
    
}