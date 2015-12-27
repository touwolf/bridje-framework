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

package org.bridje.core.impl.cfg.repos;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.core.cfg.ConfigRepository;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Inject;
import org.bridje.core.vfs.VfsService;
import org.bridje.core.vfs.VirtualFile;
import org.bridje.core.vfs.VirtualFolder;

@Component
class VfsConfigRepo implements ConfigRepository
{
    private static final Logger LOG = Logger.getLogger(VfsConfigRepo.class.getName());

    @Inject
    private VfsService vfsService;

    private VirtualFolder cfgFolder;
    
    @Override
    public <T> T findConfig(Class<T> configClass)
    {
        String configName = findConfigName(configClass);
        return findConfig(configName, configClass);
    }

    @Override
    public <T> T findConfig(String configName, Class<T> configClass)
    {
        if(cfgFolder == null)
        {
            cfgFolder = vfsService.findFolder("/etc");
        }
        if(cfgFolder != null)
        {
            if(configName != null)
            {
                VirtualFile file = cfgFolder.findFile(configName + ".xml");
                if(file != null)
                {
                    try
                    {
                        return readConfig(file, configClass);
                    }
                    catch(IOException | JAXBException ex)
                    {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public <T> T saveConfig(T newConfig)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T saveConfig(String configName, T newConfig)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canSave()
    {
        return false;
    }

    private <T> String findConfigName(Class<T> configClass)
    {
        XmlRootElement rootEl = configClass.getAnnotation(XmlRootElement.class);
        if(rootEl != null)
        {
            if(rootEl.name() != null && !rootEl.name().isEmpty() && !rootEl.name().equalsIgnoreCase("##default"))
            {
                return rootEl.name();
            }
            String simpleName = configClass.getSimpleName();
            return simpleName.substring(1).toLowerCase() + simpleName.substring(0, 1);
        }
        return null;
    }

    private <T> T readConfig(VirtualFile file, Class<T> configClass) throws IOException, JAXBException
    {
        try (InputStream is = file.open())
        {
            JAXBContext jaxbCtx = JAXBContext.newInstance(configClass);
            Unmarshaller unm = jaxbCtx.createUnmarshaller();
            return (T)unm.unmarshal(is);
        }
    }
    
}
