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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsService;
import org.bridje.web.view.widgets.WidgetManager;

@Component
@XmlTransient
public class WebLayoutManager
{
    private static final Logger LOG = Logger.getLogger(WebLayoutManager.class.getName());

    @Inject
    private VfsService vfsServ;

    @Inject
    private WidgetManager widgetManag;

    private final Path basePath = new Path("/web");

    public WebLayout loadLayout(String name)
    {
        VFile file = vfsServ.findFile(basePath.join(name + ".layout.xml"));
        return readLayout(file);
    }
    
    private WebLayout readLayout(VFile f)
    {
        try
        {
            return widgetManag.read(f, WebLayout.class);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}
