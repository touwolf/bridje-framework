/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.cfg.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.bridje.cfg.ConfigRepository;
import org.bridje.cfg.ConfigService;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.ConfigurationAdapter;
import org.bridje.cfg.XmlConfigAdapter;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Ioc;

@Component
class ConfigServiceImpl implements ConfigService
{
    @Inject
    private ConfigRepository[] repos;

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
        for (ConfigRepository repo : repos)
        {
            if(repo.canSave())
            {
                return saveConfigToRepo(configFileName, newConfig, repo);
            }
        }
        return newConfig;
    }

    private <T> T findConfigInternal(String configFileName, Class<T> configClass) throws IOException
    {
        for (ConfigRepository repo : repos)
        {
            T result = findConfigFromRepo(configFileName, configClass, repo);
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

    private ConfigurationAdapter findAdapter(Class<?> cfgClass)
    {
        Configuration cfgAnnot = cfgClass.getAnnotation(Configuration.class);
        Class<? extends ConfigurationAdapter> configAdapter = null;
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
}
