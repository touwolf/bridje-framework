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
import java.io.OutputStream;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Priority;
import org.bridje.web.WebRequestScope;
import org.bridje.web.el.ElEnviroment;
import org.bridje.web.el.ElService;

@Component
@Priority(50)
class ViewHandler implements HttpServerHandler
{
    private static final ThreadLocal<ElEnviroment> ENVS = new ThreadLocal<>();

    public static ElEnviroment getEnv()
    {
        return ENVS.get();
    }

    @Inject
    private ViewsManager viewsMang;
    
    @InjectNext
    private HttpServerHandler nextHandler;

    @Inject
    private ElService elServ;

    @Inject
    private ThemesManager themesMang;

    @Override
    public boolean handle(HttpServerContext context) throws IOException
    {
        HttpServerRequest req = context.get(HttpServerRequest.class);
        String viewUpdate = req.getPostParameter("__view");
        if(viewUpdate != null && !viewUpdate.isEmpty())
        {
            WebView view = viewsMang.findView(viewUpdate);
            if(view != null)
            {
                IocContext<WebRequestScope> wrsCtx = context.get(IocContext.class);
                try
                {
                    ENVS.set(elServ.createElEnviroment(wrsCtx));
                    updateParameters(view, req);
                    invokeAction(req, view);
                    HttpServerResponse resp = context.get(HttpServerResponse.class);
                    try(OutputStream os = resp.getOutputStream())
                    {
                        themesMang.render(findComponent(req, view), view, os);
                        os.flush();
                    }
                }
                finally
                {
                    ENVS.remove();
                }
            }
            return true;
        }
        else
        {
            boolean handled = nextHandler.handle(context);
            if(!handled)
            {
                WebView view = viewsMang.findView("/public" + req.getPath());
                if(view != null)
                {
                    IocContext<WebRequestScope> wrsCtx = context.get(IocContext.class);
                    HttpServerResponse resp = context.get(HttpServerResponse.class);
                    try(OutputStream os = resp.getOutputStream())
                    {
                        ENVS.set(elServ.createElEnviroment(wrsCtx));
                        themesMang.render(view, os);
                        os.flush();
                    }
                    finally
                    {
                        ENVS.remove();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void updateParameters(WebView view, HttpServerRequest req)
    {
        String[] names = req.getPostParametersNames();
        for (String name : names)
        {
            if(!name.startsWith("__state.")
                    && !name.startsWith("__view")
                    && !name.startsWith("__action"))
            {
                UIInputExpression input = view.findInput(name);
                if(input != null)
                {
                    input.set(req.getPostParameter(name));
                }
            }
        }
    }

    private void invokeAction(HttpServerRequest req, WebView view)
    {
        String action = req.getPostParameter("__action");
        UIEvent event = view.findEvent(action);
        event.invoke();
    }

    private WebComponent findComponent(HttpServerRequest req, WebView view)
    {
        return view.getRoot();
    }
}
