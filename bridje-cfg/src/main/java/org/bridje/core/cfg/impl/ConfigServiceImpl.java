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

package org.bridje.core.cfg.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.bridje.core.cfg.ConfigRepository;
import org.bridje.core.cfg.ConfigService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

@Component
class ConfigServiceImpl implements ConfigService
{
    @Inject
    private ConfigRepository[] repos;

    @Override
    public <T> T findConfig(Class<T> configClass) throws IOException
    {
        return findConfig(findDefaultName(configClass), configClass);
    }

    @Override
    public <T> T findOrCreateConfig(Class<T> configClass, T defaultConfig) throws IOException
    {
        return findOrCreateConfig(findDefaultName(configClass), configClass, defaultConfig);
    }

    @Override
    public <T> T findConfig(String configName, Class<T> configClass) throws IOException
    {
        for (ConfigRepository repo : repos)
        {
            T result = findConfigFromRepo(configName, configClass, repo);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }

    @Override
    public <T> T findOrCreateConfig(String configName, Class<T> configClass, T defaultConfig) throws IOException
    {
        T result = findConfig(configName, configClass);
        if(result == null)
        {
            result = saveConfig(configName, defaultConfig);
        }
        return result;
    }

    @Override
    public <T> T saveConfig(T newConfig) throws IOException
    {
        return saveConfig(findDefaultName(newConfig.getClass()), newConfig);
    }

    @Override
    public <T> T saveConfig(String configName, T newConfig) throws IOException
    {
        for (ConfigRepository repo : repos)
        {
            if(repo.canSave())
            {
                return saveConfigToRepo(configName, newConfig, repo);
            }
        }
        return null;
    }
    
    private <T> String findDefaultName(Class<T> cls)
    {
        return cls.getSimpleName().toLowerCase();
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
        try
        {
            JAXBContext context = JAXBContext.newInstance(newConfig.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(newConfig, writer);
        }
        catch(JAXBException ex)
        {
            throw new IOException(ex.getMessage(), ex);
        }
    }

    private <T> T readConfig(Reader reader, Class<T> configClass) throws IOException
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(configClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object result = unmarshaller.unmarshal(reader);
            return (T)result;
        }
        catch(JAXBException ex)
        {
            throw new IOException(ex.getMessage(), ex);
        }
    }
}
