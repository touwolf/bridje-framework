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

import java.io.IOException;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Priority;
import org.bridje.tpl.TplContext;
import org.bridje.tpl.TplNotFoundException;
import org.bridje.tpl.TplService;
import org.bridje.core.web.HttpException;
import org.bridje.core.web.WebRequestChain;
import org.bridje.core.web.WebRequestHandler;
import org.bridje.vfs.Path;

@Component
@Priority(1050)
public class TemplateRendererHandler implements WebRequestHandler
{
    @Inject
    private TplService tplServ;
    
    private TplContext tplWebContext;
    
    @PostConstruct
    public void init()
    {
        tplWebContext = tplServ.createTplContext("/web");
    }
    
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
                path = (String)result;
            }
            else
            {
                return result;
            }
        }

        if(path != null)
        {
            try
            {
                String template = new Path("public/").join(path + ".ftl").toString();
                if(tplWebContext.exists(template))
                {
                    tplWebContext.render(template, new HashMap(), chain.getResponse().getOutputStream());
                    chain.getResponse().processed();
                }
            }
            catch (TplNotFoundException ex)
            {
                return result;
            }
            catch (IOException ex)
            {
                throw new HttpException(500, "Internal Server Error");
            }
        }
        return result;
    }
}
