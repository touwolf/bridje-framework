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

import org.bridje.web.view.widgets.WidgetManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFolder;

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
     * @return The web view founded or null if it does not exists.
     */
    public WebView findView(String path)
    {
        return views.get(path);
    }

    private void initViews()
    {
        views = new HashMap<>();
        VFolder publicFolder = vfsServ.findFolder(basePath);
        if (publicFolder != null)
        {
            publicFolder.travel(this::readView, "**/*.view.xml");
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
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
