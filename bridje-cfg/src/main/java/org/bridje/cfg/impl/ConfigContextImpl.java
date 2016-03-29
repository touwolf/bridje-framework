
package org.bridje.cfg.impl;

import org.bridje.cfg.ConfigAdapter;
import org.bridje.cfg.ConfigRepository;
import org.bridje.cfg.adapter.XmlConfigAdapter;
import org.bridje.ioc.Ioc;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.ConfigContext;

class ConfigContextImpl implements ConfigContext
{
    private final ConfigContextImpl parent;
    
    private final String context;

    private final String fullContext;

    ConfigContextImpl(ConfigContextImpl parent, String context)
    {
        this.parent = parent;
        this.context = context;
        this.fullContext = findFullContext();
    }

    @Override
    public ConfigContext createContext(String context)
    {
        return new ConfigContextImpl(this, context);
    }

    @Override
    public <T> T findConfig(Class<T> configClass) throws IOException
    {
        return findConfigInternal(findFileName(configClass), configClass);
    }

    @Override
    public <T> T findOrCreateConfig(Class<T> configClass, T defaultConfig) throws IOException
    {
        return findOrCreateConfigInternal(findFileName(configClass), configClass, defaultConfig);
    }

    @Override
    public <T> T findConfig(String configName, Class<T> configClass) throws IOException
    {
        return findConfigInternal(findFileName(configName, configClass), configClass);
    }

    @Override
    public <T> T findOrCreateConfig(String configName, Class<T> configClass, T defaultConfig) throws IOException
    {
        return findOrCreateConfigInternal(findFileName(configName, configClass), configClass, defaultConfig);
    }

    @Override
    public <T> T saveConfig(T newConfig) throws IOException
    {
        return saveConfigInternal(findFileName(newConfig.getClass()), newConfig);
    }

    @Override
    public <T> T saveConfig(String configName, T newConfig) throws IOException
    {
        return saveConfigInternal(findFileName(configName, newConfig.getClass()), newConfig);
    }

    private <T> T saveConfigInternal(String configFileName, T newConfig) throws IOException
    {
        for (ConfigRepository repo : getRepos())
        {
            if(repo.canSave())
            {
                return saveConfigToRepo(findContextFileName(configFileName), newConfig, repo);
            }
        }
        return newConfig;
    }

    private <T> T findConfigInternal(String configFileName, Class<T> configClass) throws IOException
    {
        for (ConfigRepository repo : getRepos())
        {
            T result = findConfigFromRepo(findContextFileName(configFileName), configClass, repo);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }

    private <T> T findOrCreateConfigInternal(String configFileName, Class<T> configClass, T defaultConfig) throws IOException
    {
        T result = findConfigInternal(configFileName, configClass);
        if(result == null)
        {
            result = saveConfigInternal(configFileName, defaultConfig);
        }
        return result;
    }
    
    private String findContextFileName(String configFileName)
    {
        return fullContext + "/" + configFileName;
    }

    private <T> String findFileName(Class<T> cls)
    {
        return findAdapter(cls).findDefaultFileName(cls);
    }

    private <T> String findFileName(String configName, Class<T> cls)
    {
        return findAdapter(cls).findFileName(configName, cls);
    }

    private <T> T saveConfigToRepo(String configName, T newConfig, ConfigRepository repo) throws IOException
    {
        try (Writer writer = repo.saveConfig(configName))
        {
            if(writer != null)
            {
                writeConfig(writer, newConfig);
                writer.flush();
            }
        }
        return newConfig;
    }

    private <T> T findConfigFromRepo(String configName, Class<T> configClass, ConfigRepository repo) throws IOException
    {
        T configInstance = null;
        try (Reader reader = repo.findConfig(configName))
        {
            if(reader != null)
            {
                configInstance = readConfig(reader, configClass);
            }
        }
        return configInstance;
    }

    private <T> void writeConfig(Writer writer, T newConfig) throws IOException
    {
        findAdapter(newConfig.getClass()).write(newConfig, writer);
    }

    private <T> T readConfig(Reader reader, Class<T> configClass) throws IOException
    {
        return (T)findAdapter(configClass).read(configClass, reader);
    }

    private ConfigAdapter findAdapter(Class<?> cfgClass)
    {
        Configuration cfgAnnot = cfgClass.getAnnotation(Configuration.class);
        Class<? extends ConfigAdapter> configAdapter = null;
        if(cfgAnnot != null)
        {
            configAdapter = cfgAnnot.value();
        }
        if(configAdapter == null)
        {
            configAdapter = XmlConfigAdapter.class;
        }
        return Ioc.context().find(configAdapter);
    }
    
    public List<ConfigRepository> getRepos()
    {
        if(this.parent != null)
        {
            return this.parent.getRepos();
        }
        return Collections.EMPTY_LIST;
    }

    public String getContext()
    {
        return this.context;
    }

    public String getFullContext()
    {
        return this.fullContext;
    }

    private String findFullContext()
    {
        if(parent != null 
            && this.parent.getFullContext() != null 
            && !this.parent.getFullContext().isEmpty())
        {
            return this.parent.getFullContext() + "/" + this.context;
        }
        return this.context;
    }
}
