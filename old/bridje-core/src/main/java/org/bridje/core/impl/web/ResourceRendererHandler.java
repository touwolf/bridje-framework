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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Priority;
import org.bridje.core.web.HttpException;
import org.bridje.core.web.WebRequestChain;
import org.bridje.core.web.WebRequestHandler;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VirtualFile;

@Component
@Priority(1100)
public class ResourceRendererHandler implements WebRequestHandler
{
    @Inject
    private VfsService vfsServ;
    
    @Override
    public Object proccess(WebRequestChain chain) throws HttpException
    {
        String path = chain.getRequest().getRequestedPath();
        Object result = chain.procced();
        if(chain.getResponse().isProcessed())
        {
            return result;
        }

        if(result != null)
        {
            if( result instanceof String )
            {
                renderVirtualFile((String)result, chain);
            }
            else if( result instanceof File )
            {
                renderFile((File)result, chain);
            }
        }
        else
        {
            renderVirtualFile(path, chain);
        }

        return result;
    }

    private void renderVirtualFile(String path, WebRequestChain chain) throws HttpException
    {
        VirtualFile file = vfsServ.findFile(new Path("/web/public").join(path));
        if(file != null)
        {
            try(InputStream is = file.open())
            {
                IOUtils.copy(is, chain.getResponse().getOutputStream());
                chain.getResponse().processed();
            }
            catch (IOException ex)
            {
                throw new HttpException(500, "Internal Server Error");
            }
        }
    }

    private void renderFile(File file, WebRequestChain chain) throws HttpException
    {
        try(InputStream is = new FileInputStream(file))
        {
            IOUtils.copy(is, chain.getResponse().getOutputStream());
            chain.getResponse().processed();
        }
        catch (IOException ex)
        {
            throw new HttpException(500, "Internal Server Error");
        }
    }
    
}
