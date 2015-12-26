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

package org.bridje.core.impl.cfg;

import org.bridje.core.cfg.ConfigRepository;
import org.bridje.core.cfg.ConfigService;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Inject;

@Component
class ConfigServiceImpl implements ConfigService
{
    @Inject
    private ConfigRepository[] repos;

    @Override
    public <T> T findConfig(Class<T> configClass)
    {
        for (ConfigRepository repo : repos)
        {
            T result = repo.findConfig(configClass);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }

    @Override
    public <T> T findOrCreateConfig(Class<T> configClass, T defaultConfig)
    {
        T result = findConfig(configClass);
        if(result == null)
        {
            result = defaultConfig;
            for (ConfigRepository repo : repos)
            {
                if(repo.canSave())
                {
                    repo.saveConfig(defaultConfig);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public <T> T findConfig(String configName, Class<T> configClass)
    {
        for (ConfigRepository repo : repos)
        {
            T result = repo.findConfig(configName, configClass);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }

    @Override
    public <T> T findOrCreateConfig(String configName, Class<T> configClass, T defaultConfig)
    {
        T result = findConfig(configName, configClass);
        if(result == null)
        {
            result = defaultConfig;
            for (ConfigRepository repo : repos)
            {
                if(repo.canSave())
                {
                    repo.saveConfig(configName, defaultConfig);
                    break;
                }
            }
        }
        return result;
    }
    
}
