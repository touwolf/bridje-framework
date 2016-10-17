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

package org.bridje.web.view.themes;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.bridje.el.ElEnvironment;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.thls.Thls;
import org.bridje.vfs.VfsService;
import org.bridje.web.view.widgets.Widget;
import org.bridje.web.view.WebView;
import org.bridje.http.HttpBridletResponse;
import org.bridje.ioc.Application;
import org.bridje.ioc.IocContext;
import org.bridje.vfs.VFile;
import org.bridje.web.view.EventResult;
import org.bridje.web.view.state.StateRenderProvider;

/**
 * The manager for the web themes that can be used in the web application.
 */
@Component
public class ThemesManager
{
    private static final Logger LOG = Logger.getLogger(ThemesManager.class.getName());

    private Configuration ftlCfg;

    @Inject
    private VfsService vfs;

    @Inject
    private IocContext<Application> context;

    private Map<String,Object> themeTools;

    /**
     * Component Initializer
     */
    @PostConstruct
    public void init()
    {
        themeTools = new HashMap<>();
        context.getClassRepository()
                .forEachClass(ThemeTool.class, (cls, ann) -> themeTools.put(ann.name(), context.find(cls)) );
        ftlCfg = new Configuration(Configuration.VERSION_2_3_23);
        ftlCfg.setTemplateLoader(Ioc.context().find(ThemesTplLoader.class));
        ftlCfg.setDefaultEncoding("UTF-8");
        ftlCfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        ftlCfg.setLogTemplateExceptions(false);
    }

    /**
     * Renders the full web view to the given OutputStream.
     * 
     * @param view The view to be render.
     * @param os The output stream to render the view.
     * @param stateProv The provider for the current state of the view.
     */
    public void render(WebView view, OutputStream os, StateRenderProvider stateProv)
    {
        try(Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            String themeName = view.getTheme();
            String templatePath = themeName + "/Theme.ftl";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map data = new HashMap();
            data.put("tools", themeTools);
            data.put("view", view);
            data.put("env", Thls.get(ElEnvironment.class));
            data.put("stateProvider", stateProv);
            tpl.process(data, w);
            w.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Renders only the given widget to the given output stream.
     * 
     * @param widget The widget 
     * @param view The view to be render.
     * @param os The output stream to render the view.
     * @param result The result of the event invocation.
     * @param stateProv The provider for the current state of the view.
     */
    public void render(Widget widget, WebView view, OutputStream os, EventResult result, StateRenderProvider stateProv)
    {
        try(Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            String themeName = view.getTheme();
            String templatePath = themeName + "/Theme.ftl";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map data = new HashMap();
            data.put("tools", themeTools);
            data.put("view", view);
            data.put("widget", widget);
            data.put("result", result);
            data.put("env", Thls.get(ElEnvironment.class));
            data.put("stateProvider", stateProv);
            tpl.process(data, w);
            w.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Renders the expected resource to the client.
     * 
     * @param themeName The name of the theme for the resource.
     * @param resPath The path of the resource within the theme.
     * @param resp The bridlet response to render the resource.
     * @return If the resource was found an was rendered to the output. false if the resource was not found.
     * @throws IOException If any I/O exception occurs.
     */
    public boolean serveResource(String themeName, String resPath, HttpBridletResponse resp) throws IOException
    {
        VFile f = vfs.findFile("/web/themes/" + themeName + "/resources/" + resPath);
        if(f != null)
        {
            String contentType = f.getMimeType();
            resp.setContentType(contentType);
            try(OutputStream os = resp.getOutputStream())
            {
                f.copyTo(os);
                os.flush();
            }
            return true;
        }
        return false;
    }
}
