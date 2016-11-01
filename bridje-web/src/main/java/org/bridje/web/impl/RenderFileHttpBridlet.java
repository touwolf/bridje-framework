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
import java.io.OutputStream;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.http.HttpException;
import org.bridje.ioc.Inject;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsService;

@Component
@Priority(700)
class RenderFileHttpBridlet implements HttpBridlet
{
    @InjectNext
    private HttpBridlet nextHandler;

    @Inject
    private VfsService vfsServ;

    private final Path PUBLIC_PATH = new Path("/web/public");

    @Override
    public boolean handle(HttpBridletContext context) throws IOException, HttpException
    {
        HttpBridletRequest req = context.get(HttpBridletRequest.class);
        String pathStr = req.getPath();
        if(pathStr != null && 
                !(pathStr.endsWith(".view.xml") || pathStr.endsWith(".layout.xml")))
        {
            Path path = new Path(pathStr).getCanonicalPath();
            if(path != null)
            {
                path = PUBLIC_PATH.join(path);
                VFile file = vfsServ.findFile(path);
                if(file != null)
                {
                    HttpBridletResponse resp = context.get(HttpBridletResponse.class);
                    try(OutputStream os = resp.getOutputStream())
                    {
                        resp.setContentType(file.getMimeType());
                        file.copyTo(os);
                        os.flush();
                        os.close();
                    }
                    return true;
                }
            }
        }
        if(nextHandler != null)
        {
            return nextHandler.handle(context);
        }
        return false;
    }
    
}
