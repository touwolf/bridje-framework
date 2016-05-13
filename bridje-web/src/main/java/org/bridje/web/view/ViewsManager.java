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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFolder;

@Component
public class ViewsManager
{
    private static final Logger LOG = Logger.getLogger(ViewsManager.class.getName());

    @Inject
    private VfsService vfsServ;
    
    @Inject
    private WebComponentsManager compMang;

    private Map<String, WebView> views;

    private final Path basePath = new Path("/web");

    @PostConstruct
    public void init()
    {
        initViews();
    }

    public WebView findView(String path)
    {
        return views.get(path);
    }
    
    private void initViews()
    {
        views = new HashMap<>();
        VirtualFolder publicFolder = vfsServ.findFolder(basePath);
        if(publicFolder != null)
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

    private void readView(VirtualFile f)
    {
        try
        {
            WebView view = compMang.read(f, WebView.class);
            if(view != null)
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
