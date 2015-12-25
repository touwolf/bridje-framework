
package org.bridje.core.impl.http.jetty.cfg;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

public final class WebServletConfig
{
    private String name;

    private String[] urlPatterns;

    private Integer loadOnStartup;

    private boolean asyncSupported;

    private WebInitParamConfig[] initParams;

    private String description;

    private String displayName;

    public WebServletConfig()
    {
    }

    public WebServletConfig(WebServlet annot)
    {
        setName(annot.name());
        String[] urlPatternsAnnotated = (annot.urlPatterns().length == 0) ? annot.value() : annot.urlPatterns();
        setUrlPatterns(urlPatternsAnnotated);
        setLoadOnStartup(annot.loadOnStartup());
        setAsyncSupported(annot.asyncSupported());
        setInitParams(annot.initParams());
        setDescription(annot.description());
        setDisplayName(annot.displayName());
    }

    public String getName()
    {
        if (name == null)
        {
            name = "";
        }
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String[] getUrlPatterns()
    {
        if (urlPatterns == null)
        {
            urlPatterns = new String[0];
        }
        return urlPatterns;
    }

    public void setUrlPatterns(String[] urlPatterns)
    {
        this.urlPatterns = urlPatterns;
    }

    public Integer getLoadOnStartup()
    {
        if (loadOnStartup == null)
        {
            loadOnStartup = -1;
        }
        return loadOnStartup;
    }

    public void setLoadOnStartup(Integer loadOnStartup)
    {
        this.loadOnStartup = loadOnStartup;
    }

    public boolean isAsyncSupported()
    {
        return asyncSupported;
    }

    public void setAsyncSupported(boolean asyncSupported)
    {
        this.asyncSupported = asyncSupported;
    }

    public WebInitParamConfig[] getInitParams()
    {
        if (initParams == null)
        {
            initParams = new WebInitParamConfig[0];
        }
        return initParams;
    }

    public WebInitParam[] getInitParamsAnnotations()
    {
        WebInitParam[] annots = new WebInitParam[getInitParams().length];
        for (int i = 0; i < initParams.length; i++)
        {
            annots[i] = initParams[i].getAnnotation();
        }
        return annots;
    }

    public void setInitParams(WebInitParamConfig[] initParams)
    {
        this.initParams = initParams;
    }

    public void setInitParams(WebInitParam[] initParams)
    {
        if (initParams != null)
        {
            this.initParams = new WebInitParamConfig[initParams.length];
            for (int i = 0; i < initParams.length; i++)
            {
                this.initParams[i] = new WebInitParamConfig(initParams[i]);
            }
        }
    }

    public String getDescription()
    {
        if (description == null)
        {
            description = "";
        }
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDisplayName()
    {
        if (displayName == null)
        {
            displayName = "";
        }
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
}
