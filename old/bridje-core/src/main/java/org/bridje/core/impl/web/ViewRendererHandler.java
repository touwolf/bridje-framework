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
import java.util.Map;
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
import org.bridje.core.web.view.Page;
import org.bridje.core.web.view.UIContext;
import org.bridje.vfs.VfsService;

@Component
@Priority(1000)
public class ViewRendererHandler implements WebRequestHandler
{
    @Inject
    private TplService tplServ;

    @Inject
    private VfsService vfsServ;
    
    private TplContext tplThemesContext;
    
    private UIContext uiContext;
    
    @PostConstruct
    public void init()
    {
        tplThemesContext = tplServ.createTplContext("/web/themes");
        uiContext = new UIContext(vfsServ.findFolder("/web/public"));
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
                Page page = uiContext.findPage(path);
                if(page != null)
                {
                    String template = "default.ftl";
                    if(tplThemesContext.exists(template))
                    {
                        Map map = new HashMap();
                        map.put("renderType", "full");
                        map.put("view", page);
                        tplThemesContext.render(template, map, chain.getResponse().getOutputStream());
                        chain.getResponse().processed();
                    }
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
