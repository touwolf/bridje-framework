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

package org.bridje.core.impl.vfs.cfg;

import java.util.List;
import org.bridje.core.cfg.ConfigService;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Inject;
import org.bridje.core.ioc.annotations.Priority;
import org.bridje.core.vfs.VfsMountConfig;
import org.bridje.core.vfs.VfsMountEntry;

@Component
@Priority(-1000)
class CfgServiceMountConfig implements VfsMountConfig
{
    @Inject
    private ConfigService cfgServ;

    @Override
    public List<VfsMountEntry> getEntries()
    {
        VfsConfig vfs = cfgServ.findConfig(VfsConfig.class);
        if(vfs != null)
        {
            return vfs.createEntries();
        }
        return null;
    }
    
}
