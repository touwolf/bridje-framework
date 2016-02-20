
package org.bridje.core.impl.http.jetty;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import org.bridje.core.cfg.ConfigService;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

@Component
class JettyServer extends Server
{
    public static final int STOP_TIMEOUT = 30000;

    private static final Logger LOG = Logger.getLogger(JettyServer.class.getName());

    @Inject
    private IocContext context;
    
    @Inject
    private ConfigService configServ;

    private HttpConfig httpConfig;
    
    @PostConstruct
    public void init()
    {
        LOG.info("Configurando servidor de Jetty...");
        try
        {
            setStopAtShutdown(true);
            setStopTimeout(STOP_TIMEOUT);
            httpConfig = configServ.findOrCreateConfig(HttpConfig.class, new HttpConfig());
            for (HttpConnectorConfig connectorCfg : httpConfig.getConnectorConfigs())
            {
                addConnector(createConector(connectorCfg));
            }
            setHandler(createSessionHandler());
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private Handler createSessionHandler()
    {
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.getSessionManager().setMaxInactiveInterval(httpConfig.getSessionExp());
        sessionHandler.setHandler(createServletsHandler());
        return sessionHandler;
    }

    private Handler createServletsHandler()
    {
        ServletContextHandler sch = new ServletContextHandler();
        sch.setContextPath("/");

        context.getClassRepository().navigateAnnotClasses(WebServlet.class, (Class servletCls, WebServlet annotation) ->
        {
            if(Servlet.class.isAssignableFrom(servletCls))
            {
                WebServletConfig servletConfig = httpConfig.findWebServletByName(annotation.name());
                if (servletConfig == null)
                {
                    servletConfig = new WebServletConfig(annotation);
                }
                Servlet servlet = (Servlet)context.find(servletCls);
                ServletHolder servletHolder = new ServletHolder(servlet);
                servletHolder.setName(servletConfig.getName());
                servletHolder.setDisplayName(servletConfig.getDisplayName());
                servletHolder.setServletHandler(sch.getServletHandler());
                servletHolder.getRegistration().addMapping(servletConfig.getUrlPatterns());
                servletHolder.getRegistration().setLoadOnStartup(servletConfig.getLoadOnStartup());
                servletHolder.getRegistration().setAsyncSupported(servletConfig.isAsyncSupported());
                servletHolder.getRegistration().setInitParameters(createInitParameters(servletConfig.getInitParamsAnnotations()));
                MultipartConfig mpConfig = servlet.getClass().getAnnotation(MultipartConfig.class);
                if (mpConfig != null)
                {
                    servletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(mpConfig));
                }
                sch.getServletHandler().addServlet(servletHolder);
            }
        });

        context.getClassRepository().navigateAnnotClasses(WebFilter.class, (Class filterCls, WebFilter annotation) ->
        {
            if(Filter.class.isAssignableFrom(filterCls))
            {
                Filter filter = (Filter)context.find(filterCls);
                FilterHolder filterHolder = new FilterHolder(filter);
                filterHolder.setName(annotation.filterName());
                filterHolder.setDisplayName(annotation.displayName());
                filterHolder.setServletHandler(sch.getServletHandler());
                EnumSet<DispatcherType> dispacheTypes = createDispatcherType(annotation);
                filterHolder.getRegistration().addMappingForServletNames(dispacheTypes, true, annotation.servletNames());
                filterHolder.getRegistration().addMappingForUrlPatterns(dispacheTypes, true, annotation.urlPatterns());
                filterHolder.getRegistration().setAsyncSupported(annotation.asyncSupported());
                filterHolder.getRegistration().setInitParameters(createInitParameters(annotation.initParams()));
                sch.getServletHandler().addFilter(filterHolder);
            }
        });

        return sch;
    }

    private Connector createConector(HttpConnectorConfig connectorConfig)
    {
        if (connectorConfig == null)
        {
            connectorConfig = new HttpConnectorConfig();
        }

        if (!"http".equalsIgnoreCase(connectorConfig.getProtocol()) && !"https".equalsIgnoreCase(connectorConfig.getProtocol()))
        {
            connectorConfig.setProtocol("http");
        }

        if (connectorConfig.getPort() == null || connectorConfig.getPort() == 0)
        {
            connectorConfig.setPort(8080);
        }

        ServerConnector connector = createServerConnector(connectorConfig);

        connector.setPort(connectorConfig.getPort());
        return connector;
    }

    private ServerConnector createServerConnector(HttpConnectorConfig config)
    {
        ServerConnector connector;
        if ("https".equalsIgnoreCase(config.getProtocol()))
        {
            SslContextFactory sslFact = new SslContextFactory();
            if (!isBlank(config.getKeyStorePath()))
            {
                sslFact.setKeyStorePath(config.getKeyStorePath());
            }
            if (!isBlank(config.getKeyStorePassword()))
            {
                sslFact.setKeyStorePassword(config.getKeyStorePassword());
            }
            if (!isBlank(config.getKeyManagerPassword()))
            {
                sslFact.setKeyManagerPassword(config.getKeyManagerPassword());
            }
            if (!isBlank(config.getTrustStorePath()))
            {
                sslFact.setTrustStorePath(config.getTrustStorePath());
            }
            if (isBlank(config.getTrustStorePassword()))
            {
                sslFact.setTrustStorePassword(config.getTrustStorePassword());
            }
            connector = new ServerConnector(this, sslFact);
        }
        else
        {
            connector = new ServerConnector(this);
        }
        return connector;
    }

    private boolean isBlank(String value)
    {
        return value == null || value.trim().isEmpty();
    }

    private Map<String, String> createInitParameters(WebInitParam[] initParams)
    {
        Map<String, String> result = new LinkedHashMap<>();

        for (WebInitParam webInitParam : initParams)
        {
            result.put(webInitParam.name(), webInitParam.value());
        }

        return result;
    }

    private EnumSet<DispatcherType> createDispatcherType(WebFilter annot)
    {
        EnumSet<DispatcherType> result = EnumSet.allOf(DispatcherType.class);
        result.addAll(Arrays.asList(annot.dispatcherTypes()));
        return result;
    }
}
