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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Priority;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFolder;
import org.bridje.web.WebRequestScope;
import org.bridje.web.el.ElEnviroment;
import org.bridje.web.el.ElService;
import static org.bridje.web.view.WebCompProcessor.ENTITYS_RESOURCE_FILE;

@Component
@Priority(15)
class ViewHandler implements HttpServerHandler
{
    private static final Logger LOG = Logger.getLogger(ViewHandler.class.getName());

    @InjectNext
    private HttpServerHandler nextHandler;

    @Inject
    private VfsService vfsServ;

    @Inject
    private ElService elServ;
    
    private Map<String, WebView> views;

    private final Path basePath = new Path("/web/public");

    private JAXBContext webViewJaxbCtx;

    private Unmarshaller webViewUnmarsh;

    private Configuration ftlCfg;
    
    static final ThreadLocal<ElEnviroment> ENVS = new ThreadLocal<>();

    public static ElEnviroment getEnv()
    {
        return ENVS.get();
    }
    
    @PostConstruct
    public void init()
    {
        try
        {
            initJaxb();
            initViews();
            initThemes();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public boolean handle(HttpServerContext context) throws IOException
    {
        boolean handled = nextHandler.handle(context);
        if(!handled)
        {
            HttpServerRequest req = context.get(HttpServerRequest.class);
            WebView view = views.get(req.getPath());
            if(view != null)
            {
                IocContext<WebRequestScope> wrsCtx = context.get(IocContext.class);
                HttpServerResponse resp = context.get(HttpServerResponse.class);
                try(OutputStream os = resp.getOutputStream())
                {
                    render(wrsCtx, view, os);
                    os.flush();
                }
                return true;
            }
        }
        return false;
    }

    private void readView(VirtualFile f)
    {
        WebView view = toWebView(f);
        if(view != null)
        {
            String viewPath = toViewPath(f.getPath());
            views.put(viewPath, view);
        }
    }

    private WebView toWebView(VirtualFile f)
    {
        WebView result = null;
        try(InputStream is = f.open())
        {
            Object unmObj = webViewUnmarsh.unmarshal(is);
            if(unmObj instanceof WebView)
            {
                return (WebView)unmObj;
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    private void initJaxb() throws JAXBException, IOException
    {
        webViewJaxbCtx = JAXBContext.newInstance(findComponentsClasses());
        webViewUnmarsh= webViewJaxbCtx.createUnmarshaller();
    }

    private String toViewPath(Path path)
    {
        String viewPath = path.toString().substring(basePath.toString().length());
        viewPath = viewPath.substring(0, viewPath.length() - ".view.xml".length());
        return viewPath;
    }

    private void render(IocContext<WebRequestScope> wrsCtx, WebView view, OutputStream os)
    {
        try(Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            ENVS.set(elServ.createElEnviroment(wrsCtx));
            String themeName = "default";
            String templatePath = themeName + "/view.ftl";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map data = new HashMap();
            data.put("view", view);
            tpl.process(data, w);
            w.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void initViews()
    {
        views = new HashMap<>();
        VirtualFolder publicFolder = vfsServ.findFolder(basePath);
        if(publicFolder != null)
        {
            publicFolder.travel(this::readView, "(.*)\\.view\\.xml$");
        }
    }

    private void initThemes()
    {
        ftlCfg = new Configuration(Configuration.VERSION_2_3_23);
        ftlCfg.setTemplateLoader(Ioc.context().find(ThemesTplLoader.class));
        ftlCfg.setDefaultEncoding("UTF-8");
        ftlCfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        ftlCfg.setLogTemplateExceptions(false);
    }

    private Class<?>[] findComponentsClasses() throws IOException
    {
        List<Class<?>> result = new ArrayList<>();
        result.add(WebView.class);
        List<URL> files = findModelsFiles();
        files.stream()
                .map((url) -> readFile(url))
                .forEach((prop) -> readClasses(result, prop));
        Class<?>[] arr = new Class<?>[result.size()];
        return result.toArray(arr);
    }

    private Properties readFile(URL url)
    {
        Properties prop = new Properties();
        try (InputStream is = url.openStream())
        {
            prop.load(is);
        }
        catch(IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prop;
    }

    private List<URL> findModelsFiles() throws IOException
    {
        List<URL> urls = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(ENTITYS_RESOURCE_FILE);
        while (resources.hasMoreElements())
        {
            URL nextElement = resources.nextElement();
            urls.add(nextElement);
        }
        return urls;
    }

    private void readClasses(List<Class<?>> result, Properties prop)
    {
        prop.forEach((Object k, Object v) -> {
            try
            {
                Class<?> cls = Class.forName((String) k);
                if(!result.contains(cls))
                {
                    result.add(cls);
                }
            }
            catch (ClassNotFoundException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        });
    }
}
