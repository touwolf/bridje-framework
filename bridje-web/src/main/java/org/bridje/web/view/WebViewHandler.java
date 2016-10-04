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

import org.bridje.web.view.widgets.UIEvent;
import org.bridje.web.view.themes.ThemesManager;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.el.ElEnvironment;
import org.bridje.el.ElService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Priority;
import org.bridje.ioc.thls.Thls;
import org.bridje.web.ReqPathRef;
import org.bridje.web.WebScope;
import org.bridje.web.view.state.StateManager;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpException;
import org.bridje.http.HttpReqParam;

@Component
@Priority(200)
@XmlTransient
class WebViewHandler implements HttpBridlet
{
    private static final Logger LOG = Logger.getLogger(WebViewHandler.class.getName());

    @Inject
    private WebViewsManager viewsMang;
    
    @InjectNext
    private HttpBridlet nextHandler;

    @Inject
    private ElService elServ;

    @Inject
    private ThemesManager themesMang;
    
    @Inject
    private StateManager stateManag;

    @Override
    public boolean handle(HttpBridletContext context) throws IOException, HttpException
    {
        HttpBridletRequest req = context.get(HttpBridletRequest.class);
        HttpReqParam viewUpdate = req.getPostParameter("__view");
        if(viewUpdate != null && !viewUpdate.isEmpty())
        {
            WebView view = viewsMang.findView(viewUpdate.getValue());
            if(view != null)
            {
                IocContext<WebScope> wrsCtx = context.get(IocContext.class);
                try
                {
                    Thls.doAs(() ->
                    {
                        updateParameters(view, req);
                        EventResult result = invokeAction(req, view);
                        HttpBridletResponse resp = context.get(HttpBridletResponse.class);
                        try(OutputStream os = resp.getOutputStream())
                        {
                            Map<String, String> state = stateManag.createViewState(wrsCtx);
                            themesMang.render(view.getRoot(), view, os, result, state);
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
                    IocContext<WebScope> wrsCtx = context.get(IocContext.class);
                    HttpBridletResponse resp = context.get(HttpBridletResponse.class);
                    try(OutputStream os = resp.getOutputStream())
                    {
                        Thls.doAs(() ->
                        {
                            Map<String, String> state = stateManag.createViewState(wrsCtx);
                            themesMang.render(view, os, state);
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

    private void updateParameters(WebView view, HttpBridletRequest req)
    {
        view.getRoot().readInput(req);
    }

    private EventResult invokeAction(HttpBridletRequest req, WebView view) throws Exception
    {
        HttpReqParam action = req.getPostParameter("__action");
        if(action != null)
        {
            UIEvent event = view.findEvent(action.getValue());
            if(event != null)
            {
                try
                {
                    Object res = event.invoke();
                    if(res instanceof EventResult)
                    {
                        return (EventResult)res;
                    }
                    return new EventResult(event, null, null, res, null);
                }
                catch (ELException e)
                {
                    if(e.getCause() != null && e.getCause() instanceof Exception)
                    {
                        Exception real = (Exception)e.getCause();
                        return new EventResult(event, EventResultType.ERROR, real.getMessage(), null, real);
                    }
                    return new EventResult(event, EventResultType.ERROR, e.getMessage(), null, e);
                }
                catch (Exception e)
                {
                    return new EventResult(event, EventResultType.ERROR, e.getMessage(), null, e);
                }
            }
        }
        return null;
    }

    private String getViewName(HttpBridletContext context)
    {
        WebViewRef viewRef = context.get(WebViewRef.class);
        if(viewRef != null && viewRef.getViewPath() != null)
        {
            return viewRef.getViewPath();
        }
        return "/public" + ReqPathRef.findCurrentPath(context);
    }
}
