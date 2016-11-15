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

package org.bridje.web.view.themes;

import java.io.IOException;
import java.util.Arrays;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;
import org.bridje.web.ReqPathRef;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpException;

@Component
@Priority(150)
class ThemesBridlet implements HttpBridlet
{
    @Inject
    private ThemesManager themesMang;

    @InjectNext
    private HttpBridlet nextHandler;

    @Override
    public boolean handle(HttpBridletContext context) throws IOException, HttpException
    {
        HttpBridletRequest req = context.get(HttpBridletRequest.class);
        if(req.getPath().startsWith("/__themes"))
        {
            String[] arrPath = ReqPathRef.findCurrentPath(context).split("[//]");
            if(arrPath.length > 3)
            {
                String themeName = arrPath[2];
                String[] arrResPath = Arrays.copyOfRange(arrPath, 3, arrPath.length);
                String resPath = String.join("/", arrResPath);
                HttpBridletResponse resp = context.get(HttpBridletResponse.class);
                return themesMang.serveResource(themeName, resPath, resp);
            }
        }
        return nextHandler.handle(context);
    }
}
