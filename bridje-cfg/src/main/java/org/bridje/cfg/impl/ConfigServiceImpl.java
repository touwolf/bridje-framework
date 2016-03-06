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

import org.bridje.cfg.*;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.Logger;

@Component
class ConfigServiceImpl implements ConfigService
{
    private static final Logger LOG = Logger.getLogger(ConfigServiceImpl.class.getName());

    @Inject
    private ConfigRepository[] repos;

    private ConfigRepositoryContext repoContext;

    @PostConstruct
    public void init()
    {
        repoContext = new ConfigRepositoryContextImpl(repos);
    }

    @Override
    public ConfigRepositoryContext createRepoContext(String context)
    {
        return new ConfigRepositoryContextImpl(context, repos);
    }

    @Override
    public <T> T findConfig(Class<T> configClass) throws IOException
    {
        return repoContext.findConfig(configClass);
    }

    @Override
    public <T> T findConfig(String configName, Class<T> configClass) throws IOException
    {
        return repoContext.findConfig(configName, configClass);
    }

    @Override
    public <T> T findOrCreateConfig(Class<T> configClass, T defaultConfig) throws IOException
    {
        return repoContext.findOrCreateConfig(configClass, defaultConfig);
    }

    @Override
    public <T> T findOrCreateConfig(String configName, Class<T> configClass, T defaultConfig) throws IOException
    {
        return repoContext.findOrCreateConfig(configName, configClass, defaultConfig);
    }

    @Override
    public <T> T saveConfig(T newConfig) throws IOException
    {
        return repoContext.saveConfig(newConfig);
    }

    @Override
    public <T> T saveConfig(String configName, T newConfig) throws IOException
    {
        return repoContext.saveConfig(configName, newConfig);
    }
}
