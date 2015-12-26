
package org.bridje.core.impl.http.jetty.cfg;

import java.lang.annotation.Annotation;
import javax.servlet.annotation.WebInitParam;

public final class WebInitParamConfig
{
    private String name;

    private String value;

    private String description;

    public WebInitParamConfig()
    {
    }

    public WebInitParamConfig(WebInitParam annot)
    {
        setName(annot.name());
        setValue(annot.value());
        setDescription(annot.description());
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public WebInitParam getAnnotation()
    {
         class OWebInitparam implements WebInitParam{
            @Override
            public String name()
            {
                return WebInitParamConfig.this.getName();
            }

            @Override
            public String value()
            {
                return WebInitParamConfig.this.getValue();
            }

            @Override
            public String description()
            {
                return WebInitParamConfig.this.getDescription();
            }

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return WebInitParam.class;
            }
        }
        OWebInitparam annot = new OWebInitparam();
        
        return annot;
    }
}
