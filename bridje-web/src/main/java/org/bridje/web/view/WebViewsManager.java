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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.el.ELException;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.el.ElEnvironment;
import org.bridje.el.ElService;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.http.HttpException;
import org.bridje.http.HttpReqParam;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.thls.Thls;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFolder;
import org.bridje.vfs.VfsService;
import org.bridje.web.ReqPathRef;
import org.bridje.web.WebScope;
import org.bridje.web.view.state.StateManager;
import org.bridje.web.view.themes.ThemesManager;
import org.bridje.web.view.widgets.UIEvent;
import org.bridje.web.view.widgets.WidgetManager;

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
    private VfsService vfsServ;

    @Inject
    private WidgetManager widgetManag;

    @Inject
    private ThemesManager themesMang;

    @Inject
    private ElService elServ;

    @Inject
    private StateManager stateManag;

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
     * Finds the view to be updated by the __view param name sended to the
     * server.
     *
     * @param context The current HTTP bridlet context to extract the path of
     *                the view.
     *
     * @return The web view if the parameter __view was send to the server, null
     *         otherwise.
     *
     * @throws org.bridje.http.HttpException If the __view parameter was send
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
     * Finds if there is a view to be updated by the __view param name sended to
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
     * Finds the name of the view to be updated by the __view param name sended
     * to the server.
     *
     * @param context The current HTTP bridlet context to extract the path of
     *                the view.
     *
     * @return The name of the web view if the parameter __view was send to the
     *         server, null otherwise.
     */
    public String findUpdateViewName(HttpBridletContext context)
    {
        HttpBridletRequest req = context.get(HttpBridletRequest.class);
        HttpReqParam viewUpdate = req.getPostParameter("__view");
        if (viewUpdate != null && !viewUpdate.isEmpty())
        {
            return viewUpdate.getValue();
        }
        return null;
    }

    /**
     * Renders the given web view to the response output stream.
     *
     * @param view    The view to render.
     * @param context The HTTP bridlet context for the current request.
     * @param params  The parameters that can be accesed withing the view.
     */
    public void renderView(WebView view, HttpBridletContext context, Map<String,Object> params)
    {
        IocContext<WebScope> wrsCtx = context.get(IocContext.class);
        HttpBridletResponse resp = context.get(HttpBridletResponse.class);
        try (OutputStream os = resp.getOutputStream())
        {
            ElEnvironment elEnv = elServ.createElEnvironment(wrsCtx);
            elEnv.setVar("params", params);
            Thls.doAsEx(() ->
            {
                themesMang.render(view, os, () -> stateManag.createViewState(wrsCtx));
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
        HttpBridletResponse resp = context.get(HttpBridletResponse.class);
        try (OutputStream os = resp.getOutputStream())
        {
            ElEnvironment elEnv = elServ.createElEnvironment(wrsCtx);
            Thls.doAsEx(() ->
            {
                themesMang.render(view, os, () -> stateManag.createViewState(wrsCtx));
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
        HttpBridletRequest req = context.get(HttpBridletRequest.class);
        try
        {
            Thls.doAsEx(() ->
            {
                view.getRoot().readInput(req);
                EventResult result = invokeEvent(req, view);
                HttpBridletResponse resp = context.get(HttpBridletResponse.class);
                try (OutputStream os = resp.getOutputStream())
                {
                    themesMang.render(view.getRoot(), view, os, result, () -> stateManag.createViewState(wrsCtx));
                    os.flush();
                }
                return null;
            }, ElEnvironment.class, elServ.createElEnvironment(wrsCtx));
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Invokes the event expression sended to the server.
     *
     * @param req  The HTTP request to look for the event expression.
     * @param view The view that declares the event.
     *
     * @return The result of the invocation of the event.
     */
    public EventResult invokeEvent(HttpBridletRequest req, WebView view)
    {
        UIEvent event = findEvent(req, view);
        if (event != null)
        {
            try
            {
                return invokeEvent(event);
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * Invokes the given event.
     *
     * @param event The event to be invoked.
     *
     * @return The result of the event invokation.
     *
     * @throws java.lang.Exception If any unhandled exception ocurss.
     */
    public EventResult invokeEvent(UIEvent event) throws Exception
    {
        return Thls.doAs(() ->
        {
            try
            {
                Object res = event.invoke();
                if (res instanceof EventResult)
                {
                    return (EventResult) res;
                }
                return EventResult.of(null, null, res, null);
            }
            catch (ELException e)
            {
                if (e.getCause() != null && e.getCause() instanceof Exception)
                {
                    Exception real = (Exception) e.getCause();
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                    return EventResult.error(real.getMessage(), real);
                }
                LOG.log(Level.SEVERE, e.getMessage(), e);
                return EventResult.error(e.getMessage(), e);
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
                return EventResult.error(e.getMessage(), e);
            }
        }, UIEvent.class, event);
    }

    /**
     * Finds the event sended to the server in the __action parameter.
     *
     * @param req  The request to look for the event.
     * @param view The view to look for the event.
     *
     * @return The event.
     */
    public UIEvent findEvent(HttpBridletRequest req, WebView view)
    {
        HttpReqParam action = req.getPostParameter("__action");
        if (action != null)
        {
            UIEvent event = view.findEvent(action.getValue());
            return event;
        }
        return null;
    }

    /**
     * Finds the name of the view to be rendered by the path of the HTTP
     * request.
     *
     * @param context The HTTP bidlet context to get the path form.
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
        VFolder publicFolder = vfsServ.findFolder(basePath);
        if (publicFolder != null)
        {
            publicFolder
                    .listFiles("**/*.view.xml")
                    .forEach(this::readView);
        }
    }

    private String toViewPath(Path path)
    {
        String viewPath = path.toString().substring(basePath.toString().length());
        viewPath = viewPath.substring(0, viewPath.length() - ".view.xml".length());
        return viewPath;
    }

    private void readView(VFile f)
    {
        try
        {
            WebView view = widgetManag.read(f, WebView.class);
            if (view != null)
            {
                String viewPath = toViewPath(f.getPath());
                view.setName(viewPath);
                views.put(viewPath, view);
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, "Could not parse " + f.getPath() + ". " + e.getMessage(), e);
        }
    }

}
