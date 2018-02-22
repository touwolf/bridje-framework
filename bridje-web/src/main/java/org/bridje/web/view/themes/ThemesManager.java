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
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.bridje.el.ElEnvironment;
import org.bridje.http.HttpBridletResponse;
import org.bridje.ioc.Application;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.thls.Thls;
import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;
import org.bridje.web.i18n.WebI18nServices;
import org.bridje.web.view.EventResult;
import org.bridje.web.view.WebView;
import org.bridje.web.view.controls.Control;
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
    private IocContext<Application> context;

    @Inject
    private AssetCompressor[] compressors;

    private Map<String, Object> themeTools;

    @Inject
    private WebI18nServices webI18nServ;

    /**
     * Component Initializer
     */
    @PostConstruct
    public void init()
    {
        themeTools = new HashMap<>();
        context.getClassRepository()
                .forEachClass(ThemeTool.class, (cls, ann) -> themeTools.put(ann.name(), context.find(cls)));
        ftlCfg = new Configuration(Configuration.VERSION_2_3_23);
        ftlCfg.setTemplateLoader(Ioc.context().find(ThemesTplLoader.class));
        ftlCfg.setDefaultEncoding("UTF-8");
        ftlCfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        ftlCfg.setLogTemplateExceptions(false);
    }

    /**
     * Renders the full web view to the given OutputStream.
     *
     * @param view      The view to be render.
     * @param writer    The writer to render the view.
     * @param stateProv The provider for the current state of the view.
     */
    public void render(WebView view, Writer writer, StateRenderProvider stateProv)
    {
        if (view == null) return;
        try
        {
            String themeName = view.getDefaultTheme();
            if (themeName == null || themeName.isEmpty()) return;
            String templatePath = themeName + "/Theme.ftlh";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map<String, Object> data = new HashMap<>();
            data.put("i18n", webI18nServ.getI18nMap());
            data.put("tools", themeTools);
            data.put("view", view);
            data.put("env", Thls.get(ElEnvironment.class));
            data.put("renderMode", "dinamic");
            data.put("stateProvider", stateProv);
            tpl.process(data, writer);
            writer.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Renders the full web view to the given OutputStream.
     *
     * @param view           The view to be render.
     * @param writer         The writer to render the view.
     */
    public void renderStatic(WebView view, Writer writer)
    {
        if (view == null) return;
        try
        {
            String themeName = view.getDefaultTheme();
            if (themeName == null || themeName.isEmpty()) return;
            String templatePath = themeName + "/Theme.ftlh";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map<String, Object> data = new HashMap<>();
            data.put("i18n", webI18nServ.getI18nMap());
            data.put("tools", themeTools);
            data.put("view", view);
            data.put("env", Thls.get(ElEnvironment.class));
            data.put("renderMode", "static");
            data.put("resourceRenderer", (ResourceRenderProvider) (theme, resource) -> getResourceContent(theme, resource));
            tpl.process(data, writer);
            writer.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * Renders the full web view to the given OutputStream.
     *
     * @param view      The view to be render.
     * @param os        The output stream to render the view.
     * @param stateProv The provider for the current state of the view.
     */
    public void render(WebView view, OutputStream os, StateRenderProvider stateProv)
    {
        if (view == null) return;
        try (Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            render(view, w, stateProv);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Renders only the given control to the given output stream.
     *
     * @param control     The control
     * @param view        The view to be render.
     * @param os          The output stream to render the view.
     * @param eventResult The result of the event invocation.
     * @param stateProv   The provider for the current state of the view.
     */
    public void render(Control control, WebView view, OutputStream os, EventResult eventResult, StateRenderProvider stateProv)
    {
        try (Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            String themeName = view.getDefaultTheme();
            String templatePath = themeName + "/Theme.ftlh";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map<String, Object> data = new HashMap<>();
            data.put("i18n", webI18nServ.getI18nMap());
            data.put("tools", themeTools);
            data.put("view", view);
            data.put("control", control);
            data.put("eventResult", eventResult);
            data.put("env", Thls.get(ElEnvironment.class));
            data.put("stateProvider", stateProv);
            data.put("renderMode", "dinamic");
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
     * @param resPath   The path of the resource within the theme.
     * @param resp      The bridlet response to render the resource.
     *
     * @return If the resource was found an was rendered to the output. false if
     *         the resource was not found.
     *
     * @throws IOException If any I/O exception occurs.
     */
    public boolean serveResource(String themeName, String resPath, HttpBridletResponse resp) throws IOException
    {
        Path path = new Path("/web/themes/" + themeName + "/resources/" + resPath);
        GlobExpr globExpr = new GlobExpr("/web/themes/**/resources/**");
        if (globExpr.globMatches(path.getCanonicalPath()))
        {
            VFile f = new VFile(path);
            if (f.isFile())
            {
                String contentType = f.getMimeType();
                resp.setContentType(contentType);
                try (OutputStream os = resp.getOutputStream())
                {
                    try (InputStream is = findInputStream(f))
                    {
                        copy(is, os);
                        os.flush();
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the content of the given resource.
     *
     * @param themeName The name of the theme for the resource.
     * @param resPath   The path of the resource within the theme.
     *
     * @return If the resource was found an was rendered to the output. false if
     *         the resource was not found.
     */
    public String getResourceContent(String themeName, String resPath)
    {
        Path path = new Path("/web/themes/" + themeName + "/resources/" + resPath);
        GlobExpr globExpr = new GlobExpr("/web/themes/**/resources/**");
        if (globExpr.globMatches(path.getCanonicalPath()))
        {
            VFile f = new VFile(path);
            if (f.isFile())
            {
                try (ByteArrayOutputStream os = new ByteArrayOutputStream())
                {
                    try (InputStream is = findInputStream(f))
                    {
                        copy(is, os);
                        os.flush();
                    }
                    return os.toString("UTF-8");
                }
                catch(IOException ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
        return "";
    }
    
    private InputStream findInputStream(VFile file) throws FileNotFoundException
    {
        boolean canCompress = true;//fixme: check for configuration to enable compression
        if (canCompress)
        {
            String content = compressedContent(file);
            if (content != null)
            {
                byte[] compressedBytes = content.getBytes(Charset.defaultCharset());
                return new ByteArrayInputStream(compressedBytes);
            }
        }
        return new VFileInputStream(file);
    }

    private final Map<String, String> compressedStreams = new HashMap<>();

    private String compressedContent(VFile file)
    {
        AssetCompressor compressor = findCompressor(file);
        if (compressor == null)
        {
            return null;
        }

        String fileStr = file.getPath().toString();
        String content = compressedStreams.get(fileStr);
        if (content == null)
        {
            content = compressor.compress(file);
            compressedStreams.put(fileStr, content);
        }
        return content;
    }

    private AssetCompressor findCompressor(VFile file)
    {
        for (AssetCompressor compressor : compressors)
        {
            if (compressor.canCompress(file))
            {
                return compressor;
            }
        }
        return null;
    }

    private void copy(InputStream is, OutputStream os) throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesCount = is.read(buffer);
        while (bytesCount > -1)
        {
            os.write(buffer, 0, bytesCount);
            bytesCount = is.read(buffer);
        }
    }

}
