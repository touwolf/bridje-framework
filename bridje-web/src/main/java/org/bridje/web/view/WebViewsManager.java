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

import org.bridje.web.i18n.WebI18nServices;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.el.ElEnvironment;
import org.bridje.el.ElService;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.http.HttpException;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.thls.Thls;
import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.web.RedirectTo;
import org.bridje.web.ReqPathRef;
import org.bridje.web.WebScope;
import org.bridje.web.view.controls.Control;
import org.bridje.web.view.controls.ControlInputReader;
import org.bridje.web.view.controls.ControlManager;
import org.bridje.web.view.state.StateManager;
import org.bridje.web.view.themes.ThemesManager;

/**
 * A manager for all the web views present in the application. with this
 * component you can get access to the list of web views that the current
 * application has.
 */
@Component
@XmlTransient
public class WebViewsManager
{
    private static final Logger LOG = Logger.getLogger(WebViewsManager.class.getName());

    @Inject
    private ControlManager controlManag;

    @Inject
    private ThemesManager themesMang;

    @Inject
    private ElService elServ;

    @Inject
    private StateManager stateManag;

    @Inject
    private WebLayoutManager layoutManag;
    
    @Inject
    private WebI18nServices webI18nServ;

    private Map<String, WebView> views;

    private final Path basePath = new Path("/web");

    /**
     * IoC init method do not call this manually.
     */
    @PostConstruct
    public void init()
    {
        initViews();
    }

    /**
     * Finds the view by the given path.
     *
     * @param path The web view path to be found.
     *
     * @return The web view founded or null if it does not exists.
     */
    public WebView findView(String path)
    {
        return views.get(path);
    }

    /**
     * Finds the view by the requested path of the given HTTP bridlet context.
     *
     * @param context The current HTTP bridlet context to extract the path of
     *                the view.
     *
     * @return The web view founded or null if it does not exists.
     */
    public WebView findView(HttpBridletContext context)
    {
        String viewName = findViewName(context);
        return findView(viewName);
    }

    /**
     * Finds the view to be updated by the Bridje-View header sended to the
     * server.
     *
     * @param context The current HTTP bridlet context to extract the path of
     *                the view.
     *
     * @return The web view if the header Bridje-View was send to the server, null
     *         otherwise.
     *
     * @throws org.bridje.http.HttpException If the Bridje-View parameter was send
     *                                       but the view referenced by it does
     *                                       not exists.
     */
    public WebView findUpdateView(HttpBridletContext context) throws HttpException
    {
        String viewName = findUpdateViewName(context);
        if (viewName != null)
        {
            WebView view = findView(viewName);
            if (view == null)
            {
                throw new HttpException(400, "Bad Request");
            }
            return view;
        }
        return null;
    }

    /**
     * Finds if there is a view to be updated by the  Bridje-View header name sended to
     * the server.
     *
     * @param context The current HTTP bridlet context to extract the path of
     *                the view.
     *
     * @return The current request is a view update request.
     */
    public boolean isUpdateView(HttpBridletContext context)
    {
        return findUpdateViewName(context) != null;
    }

    /**
     * Base path for all views.
     * 
     * @return The base path for all views.
     */
    public Path getBasePath()
    {
        return basePath;
    }

    /**
     * Finds the name of the view to be updated by the Bridje-View header name sended
     * to the server.
     *
     * @param context The current HTTP bridlet context to extract the path of
     *                the view.
     *
     * @return The name of the web view if the header Bridje-View was send to the
     *         server, null otherwise.
     */
    public String findUpdateViewName(HttpBridletContext context)
    {
        HttpBridletRequest req = context.getRequest();
        String viewUpdate = req.getHeader("Bridje-View");
        if (viewUpdate != null && !viewUpdate.isEmpty())
        {
            return viewUpdate;
        }
        return null;
    }

    /**
     * Renders the given web view to the response output stream.
     *
     * @param view    The view to render.
     * @param context The HTTP bridlet context for the current request.
     * @param params  The parameters that can be accessed within the view.
     */
    public void renderView(WebView view, HttpBridletContext context, Map<String,Object> params)
    {
        if(view == null) return;
        IocContext<WebScope> wrsCtx = context.get(IocContext.class);
        HttpBridletResponse resp = context.getResponse();
        try (OutputStream os = resp.getOutputStream())
        {
            ElEnvironment elEnv = elServ.createElEnvironment(wrsCtx);
            elEnv.pushVar("view", view);
            elEnv.pushVar("i18n", webI18nServ.getI18nMap());
            elEnv.pushVar("params", params);
            elEnv.pushVar("eventResult", EventResult.none());
            Thls.doAsEx(() ->
            {
                themesMang.render(view, os, () -> stateManag.createStringViewState(wrsCtx));
                os.flush();
                return null;
            }, ElEnvironment.class, elEnv);
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Renders the given web view to the response output stream.
     *
     * @param view    The view to render.
     * @param context The HTTP bridlet context for the current request.
     */
    public void renderView(WebView view, HttpBridletContext context)
    {
        IocContext<WebScope> wrsCtx = context.get(IocContext.class);
        HttpBridletResponse resp = context.getResponse();
        try (OutputStream os = resp.getOutputStream())
        {
            ElEnvironment elEnv = elServ.createElEnvironment(wrsCtx);
            elEnv.pushVar("view", view);
            elEnv.pushVar("i18n", webI18nServ.getI18nMap());
            elEnv.pushVar("eventResult", EventResult.none());
            Thls.doAsEx(() ->
            {
                themesMang.render(view, os, () -> stateManag.createStringViewState(wrsCtx));
                os.flush();
                return null;
            }, ElEnvironment.class, elEnv);
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Performs a view update and/or an event invocation by the data sended to
     * the server in the given request.
     *
     * @param view    The view to be updated.
     * @param context The HTTP bridlet context for the current request.
     */
    public void updateView(WebView view, HttpBridletContext context)
    {
        IocContext<WebScope> wrsCtx = context.get(IocContext.class);
        HttpBridletRequest req = context.getRequest();
        ElEnvironment elEnv = elServ.createElEnvironment(wrsCtx);
        Thls.doAs(() ->
        {
            EventResult evResult = view.getRoot()
                    .findById(elEnv, req.getHeader("Bridje-Form"), (Control ctrl) ->
                    {
                        ctrl.readInput(new ControlInputReader(req), elEnv);
                        EventResult result = ctrl.executeEvent(new ControlInputReader(req), elEnv);
                        if(result == null) result = EventResult.none();
                        return result;
                    });
            
            view.getRoot()
                    .findById(elEnv, req.getHeader("Bridje-Container"), (Control ctrl) ->
                    {
                        if(evResult.getData() != null && evResult.getData() instanceof RedirectTo)
                        {
                            RedirectTo redirectTo = (RedirectTo)evResult.getData();
                            context.getResponse().setHeader("Bridje-Location", redirectTo.getResource());
                        }
                        else
                        {
                            elEnv.pushVar("view", view);
                            elEnv.pushVar("i18n", webI18nServ.getI18nMap());
                            elEnv.pushVar("eventResult", evResult);
                            elEnv.pushVar("control", ctrl);
                            HttpBridletResponse resp = context.getResponse();
                            try (OutputStream os = resp.getOutputStream())
                            {
                                themesMang.render(ctrl, view, os, evResult, () -> stateManag.createStringViewState(wrsCtx));
                                os.flush();
                            }
                            catch (IOException ex)
                            {
                                LOG.log(Level.SEVERE, ex.getMessage(), ex);
                            }
                            context.getResponse().setHeader("Bridje-State", stateManag.createStringViewState(wrsCtx));
                        }
                        return true;
                    });
            return null;
        }, ElEnvironment.class, elEnv);
    }
    
    /**
     * Renders the given web view partially, (as the result of an update) to the response output stream.
     *
     * @param view    The view to render.
     * @param context The HTTP bridlet context for the current request.
     * @param result  The event result for this render operation.
     * @param params  The parameters that can be accessed within the view.
     */
    public void renderPartialView(WebView view, HttpBridletContext context, EventResult result, Map<String,Object> params)
    {
        IocContext<WebScope> wrsCtx = context.get(IocContext.class);
        try
        {
            ElEnvironment elEnv = elServ.createElEnvironment(wrsCtx);
            Thls.doAsEx(() ->
            {
                elEnv.pushVar("view", view);
                elEnv.pushVar("i18n", webI18nServ.getI18nMap());
                elEnv.pushVar("params", params);
                elEnv.pushVar("eventResult", result);
                HttpBridletResponse resp = context.getResponse();
                try (OutputStream os = resp.getOutputStream())
                {
                    themesMang.render(view.getRoot(), view, os, result, () -> stateManag.createStringViewState(wrsCtx));
                    os.flush();
                }
                return null;
            }, ElEnvironment.class, elEnv);
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Finds the name of the view to be rendered by the path of the HTTP
     * request.
     *
     * @param context The HTTP bridlet context to get the path form.
     *
     * @return The name of the view to be use to render this request.
     */
    public String findViewName(HttpBridletContext context)
    {
        WebViewRef viewRef = context.get(WebViewRef.class);
        if (viewRef != null && viewRef.getViewPath() != null)
        {
            return viewRef.getViewPath();
        }
        return "/public" + ReqPathRef.findCurrentPath(context);
    }

    private void initViews()
    {
        views = new HashMap<>();
        VFile publicFolder = new VFile(basePath);
        if (publicFolder.isDirectory())
        {
            GlobExpr exp = new GlobExpr("**.view.xml");
            VFile[] files = publicFolder.search(exp);
            Arrays.asList(files).forEach(this::readView);
        }
    }

    private void readView(VFile f)
    {
        try
        {
            WebView view = controlManag.read(f, WebView.class);
            if (view != null)
            {
                view.setFile(f);
                views.put(view.getName(), view);
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, "Could not parse " + f.getPath() + ". " + e.getMessage(), e);
        }
    }
}
