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

package org.bridje.core.impl.vfs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Inject;
import org.bridje.core.vfs.Path;
import org.bridje.core.vfs.VfsMountConfig;
import org.bridje.core.vfs.VfsMountEntry;
import org.bridje.core.vfs.VfsProvider;
import org.bridje.core.vfs.VfsService;
import org.bridje.core.vfs.VfsSource;
import org.bridje.core.vfs.VirtualFile;
import org.bridje.core.vfs.VirtualFileVisitor;
import org.bridje.core.vfs.VirtualFolder;
import org.bridje.core.vfs.VirtualFolderVisitor;

@Component
class VfsServiceImpl implements VfsService
{
    private static final Logger LOG = Logger.getLogger(VfsServiceImpl.class.getName());

    private MemoryFolder root;
    
    private Map<String,VfsProvider> providersMap;

    @Inject
    private VfsProvider[] providers;
    
    @Inject
    private VfsMountConfig[] mountConfig;

    @Override
    public VirtualFolder findFolder(String path)
    {
        load();
        return root.findFolder(path);
    }

    @Override
    public VirtualFolder findFolder(Path path)
    {
        load();
        return root.findFolder(path);
    }

    @Override
    public VirtualFile findFile(String path)
    {
        load();
        return root.findFile(path);
    }

    @Override
    public VirtualFile findFile(Path path)
    {
        load();
        return root.findFile(path);
    }

    @Override
    public List<VirtualFolder> listFolders()
    {
        load();
        return root.listFolders();
    }

    @Override
    public List<VirtualFile> listFiles()
    {
        load();
        return root.listFiles();
    }

    @Override
    public void mount(Path path, VfsSource source)
    {
        Path canPath = path.getCanonicalPath();
        if(canPath == null)
        {
            throw new IllegalArgumentException("The specified path " + path  + " is not valid .");
        }
        Path mountPath = canPath.getParent();
        String mountName = canPath.getName();
        MemoryFolder currentFolder = root;
        if(mountPath != null)
        {
            for (Path currentPath : mountPath)
            {
                VirtualFolder currVFolder = root.findFolder(currentPath);
                if(currVFolder != null && (currVFolder instanceof MemoryFolder))
                {
                    currentFolder = (MemoryFolder)currVFolder;
                }
                else
                {
                    currentFolder = currentFolder.addFolder(new MemoryFolder(currentPath));
                }
            }
        }

        currentFolder.addFolder(new PhysicalFolder(source, mountPath, new Path(mountName)));
    }

    @Override
    public void travel(VirtualFileVisitor visitor)
    {
        root.travel(visitor);
    }

    @Override
    public void travel(VirtualFolderVisitor visitor)
    {
        root.travel(visitor);
    }

    private void load()
    {
        if(root == null)
        {
            root = new MemoryFolder(null);
            loadProviders();
            loadConfig();
        }
    }

    private void loadProviders()
    {
        providersMap = new HashMap<>();
        for (VfsProvider provider : providers)
        {
            providersMap.put(provider.getName(), provider);
        }
    }

    private void loadConfig()
    {
        if(mountConfig != null)
        {
            for (VfsMountConfig cfg : mountConfig)
            {
                List<VfsMountEntry> entires = cfg.getEntries();
                if(entires != null)
                {
                    for (VfsMountEntry entry : entires)
                    {
                        String fileSystem = entry.getFileSystem();
                        VfsProvider fsProv = providersMap.get(fileSystem);
                        if(fsProv != null)
                        {
                            mount(new Path(entry.getMountPath()), fsProv.createVfsSource(entry));
                        }
                    }
                }
            }
        }
    }
}
