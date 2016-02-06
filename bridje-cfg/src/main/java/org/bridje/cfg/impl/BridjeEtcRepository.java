/*
 * Copyright 2016 Bridje Framework.
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.bridje.cfg.ConfigRepository;
import org.bridje.ioc.Component;

@Component
public class BridjeEtcRepository implements ConfigRepository
{
    @Override
    public Reader findConfig(String configName) throws IOException
    {
        URL resource = this.getClass().getResource("/BRIDJE-INF/etc/" + configName);
        if(resource != null)
        {
            return new InputStreamReader(resource.openStream());
        }
        return null;
    }

    @Override
    public Writer saveConfig(String configName) throws IOException
    {
        return null;
    }

    @Override
    public boolean canSave()
    {
        return false;
    }
    
}
