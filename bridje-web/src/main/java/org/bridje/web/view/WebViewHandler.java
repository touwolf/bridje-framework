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

import org.bridje.web.view.comp.UIEvent;
import org.bridje.web.view.comp.UIInputExpression;
import org.bridje.web.view.themes.ThemesManager;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.el.ElEnvironment;
import org.bridje.el.ElService;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Priority;
import org.bridje.ioc.thls.Thls;
import org.bridje.web.ReqPathRef;
import org.bridje.web.WebRequestScope;

@Component
@Priority(200)
@XmlTransient
class WebViewHandler implements HttpServerHandler
{
    private static final Logger LOG = Logger.getLogger(WebViewHandler.class.getName());

    @Inject
    private WebViewsManager viewsMang;
    
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
                    Thls.doAs(() ->
                    {
                        updateParameters(view, req);
                        Object result = invokeAction(req, view);
                        HttpServerResponse resp = context.get(HttpServerResponse.class);
                        try(OutputStream os = resp.getOutputStream())
                        {
                            themesMang.render(view.getRoot(), view, os, result);
                            os.flush();
                        }
                        return null;
                    },
                    ElEnvironment.class, elServ.createElEnvironment(wrsCtx));
                }
                catch(Exception ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            return true;
        }
        else
        {
            boolean handled = nextHandler.handle(context);
            if(!handled)
            {
                WebView view = viewsMang.findView(getViewName(context));
                if(view != null)
                {
                    IocContext<WebRequestScope> wrsCtx = context.get(IocContext.class);
                    HttpServerResponse resp = context.get(HttpServerResponse.class);
                    try(OutputStream os = resp.getOutputStream())
                    {
                        Thls.doAs(() ->
                        {
                            themesMang.render(view, os);
                            os.flush();
                            return null;
                        },
                        ElEnvironment.class, elServ.createElEnvironment(wrsCtx));
                    }
                    catch(Exception ex)
                    {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
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

    private Object invokeAction(HttpServerRequest req, WebView view)
    {
        String action = req.getPostParameter("__action");
        UIEvent event = view.findEvent(action);
        return event.invoke();
    }

    private String getViewName(HttpServerContext context)
    {
        WebViewRef viewRef = context.get(WebViewRef.class);
        if(viewRef != null && viewRef.getViewPath() != null)
        {
            return viewRef.getViewPath();
        }
        return "/public" + ReqPathRef.findCurrentPath(context);
    }
}
