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

import java.io.File;
import org.bridje.cfg.*;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

import java.util.List;

@Component
class ConfigServiceImpl extends ConfigContextImpl implements ConfigService
{
    @Inject
    private List<ConfigRepository> repos;

    @Inject    
    private ConfigAdapter[] allAdapters;

    public ConfigServiceImpl()
    {
        super(null, "");
    }

    @Override
    public void addRepository(ConfigRepository repo)
    {
        repos.add(repo);
    }

    @Override
    public ConfigRepository createFileRepository(File file)
    {
        return new FileRepository(file);
    }

    @Override
    public ConfigRepository createClassPathRepository(Class<?> cls, String path)
    {
        return new ClassPathRepository(cls, path);
    }

    @Override
    public List<ConfigRepository> getRepos()
    {
        return repos;
    }

    @Override
    public ConfigAdapter[] getAdapters()
    {
        return allAdapters;
    }
}
