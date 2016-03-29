
package org.bridje.cfg.impl;

import org.bridje.cfg.ConfigAdapter;
import org.bridje.cfg.ConfigRepository;
import org.bridje.ioc.Ioc;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
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
        ConfigAdapter[] adapters = findAllAdapters(configClass);
        for (ConfigRepository repo : getRepos())
        {
            for (ConfigAdapter adapter : adapters)
            {
                String realFileName = findContextFileName(adapter.findDefaultFileName(configClass));
                T result = findConfigFromRepo(adapter, realFileName, configClass, repo);
                if(result != null)
                {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public <T> T findConfig(String configName, Class<T> configClass) throws IOException
    {
        ConfigAdapter[] adapters = findAllAdapters(configClass);
        for (ConfigRepository repo : getRepos())
        {
            for (ConfigAdapter adapter : adapters)
            {
                String realFileName = findContextFileName(adapter.findFileName(configName, configClass));
                T result = findConfigFromRepo(adapter, realFileName, configClass, repo);
                if(result != null)
                {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public <T> T saveConfig(T newConfig) throws IOException
    {
        ConfigAdapter adapter = findFirstAdapters(newConfig.getClass());
        for (ConfigRepository repo : getRepos())
        {
            if(repo.canSave() && adapter != null)
            {
                String realFileName = findContextFileName(adapter.findDefaultFileName(newConfig.getClass()));
                return saveConfigToRepo(adapter, realFileName, newConfig, repo);
            }
        }
        return newConfig;
    }

    @Override
    public <T> T saveConfig(String configName, T newConfig) throws IOException
    {
        ConfigAdapter adapter = findFirstAdapters(newConfig.getClass());
        for (ConfigRepository repo : getRepos())
        {
            if(repo.canSave() && adapter != null)
            {
                String realFileName = findContextFileName(adapter.findFileName(configName, newConfig.getClass()));
                return saveConfigToRepo(adapter, realFileName, newConfig, repo);
            }
        }
        return newConfig;
    }

    @Override
    public <T> T findOrCreateConfig(Class<T> configClass, T defaultConfig) throws IOException
    {
        T config = findConfig(configClass);
        if(config == null)
        {
            config = defaultConfig;
            saveConfig(config);
        }
        return config;
    }

    @Override
    public <T> T findOrCreateConfig(String configName, Class<T> configClass, T defaultConfig) throws IOException
    {
        T config = findConfig(configName, configClass);
        if(config == null)
        {
            config = defaultConfig;
            saveConfig(configName, config);
        }
        return config;
    }

    private String findContextFileName(String configFileName)
    {
        return fullContext + "/" + configFileName;
    }

    private <T> T saveConfigToRepo(ConfigAdapter adapter, String configName, T newConfig, ConfigRepository repo) throws IOException
    {
        try (Writer writer = repo.saveConfig(configName))
        {
            if(writer != null)
            {
                adapter.write(newConfig, writer);
                writer.flush();
            }
        }
        return newConfig;
    }

    private <T> T findConfigFromRepo(ConfigAdapter adapter, String configName, Class<T> configClass, ConfigRepository repo) throws IOException
    {
        T configInstance = null;
        try (Reader reader = repo.findConfig(configName))
        {
            if(reader != null)
            {
                configInstance = (T)adapter.read(configClass, reader);
            }
        }
        return configInstance;
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

    private <T> ConfigAdapter[] findAllAdapters(Class<T> configClass)
    {
        Configuration cfgAnnot = configClass.getAnnotation(Configuration.class);
        Class<? extends ConfigAdapter>[] configAdapter = null;
        if(cfgAnnot != null)
        {
            configAdapter = cfgAnnot.value();
        }
        if(configAdapter == null)
        {
            return getAdapters();
        }
        List<ConfigAdapter> result = new ArrayList<>();
        for (Class<? extends ConfigAdapter> clsAdapter : configAdapter)
        {
            result.add(Ioc.context().find(clsAdapter));
        }
        ConfigAdapter[] arr = new ConfigAdapter[result.size()];
        return result.toArray(arr);
    }
    
    public ConfigAdapter[] getAdapters()
    {
        if(parent != null)
        {
            return parent.getAdapters();
        }
        return new ConfigAdapter[]{};
    }

    private ConfigAdapter findFirstAdapters(Class<? extends Object> configClass)
    {
        ConfigAdapter[] adapters = findAllAdapters(configClass);
        if(adapters.length > 0)
        {
            return adapters[0];
        }
        return null;
    }
}
