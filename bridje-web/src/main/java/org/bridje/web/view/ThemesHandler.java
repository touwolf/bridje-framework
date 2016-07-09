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

package org.bridje.web.view;

import java.io.IOException;
import java.util.Arrays;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;
import org.bridje.web.ReqPathRef;

@Component
@Priority(150)
class ThemesHandler implements HttpServerHandler
{
    @Inject
    private ThemesManager themesMang;
    
    @InjectNext
    private HttpServerHandler nextHandler;
    
    @Override
    public boolean handle(HttpServerContext context) throws IOException
    {
        HttpServerRequest req = context.get(HttpServerRequest.class);
        if(req.getPath().startsWith("/__themes"))
        {
            String[] arrPath = ReqPathRef.findCurrentPath(context).split("[//]");
            if(arrPath.length > 3)
            {
                String themeName = arrPath[2];
                String[] arrResPath = Arrays.copyOfRange(arrPath, 3, arrPath.length);
                String resPath = String.join("/", arrResPath);
                HttpServerResponse resp = context.get(HttpServerResponse.class);
                return themesMang.serveResource(themeName, resPath, resp);
            }
        }
        return nextHandler.handle(context);
    }
    
}
