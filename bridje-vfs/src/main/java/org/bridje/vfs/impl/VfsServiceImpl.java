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

package org.bridje.vfs.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.vfs.ClassPathVfsSource;
import org.bridje.vfs.FileVfsSource;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VfsSource;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFileVisitor;
import org.bridje.vfs.VirtualFolder;
import org.bridje.vfs.VirtualFolderVisitor;

@Component
class VfsServiceImpl implements VfsService
{
    private MemoryFolder root;

    @PostConstruct
    public void init()
    {
        if(root == null)
        {
            root = new MemoryFolder(null);
        }
    }

    @Override
    public VirtualFolder findFolder(String path)
    {
        return root.findFolder(path);
    }

    @Override
    public VirtualFolder findFolder(Path path)
    {
        return root.findFolder(path);
    }

    @Override
    public VirtualFile findFile(String path)
    {
        return root.findFile(path);
    }

    @Override
    public VirtualFile findFile(Path path)
    {
        return root.findFile(path);
    }

    @Override
    public List<VirtualFolder> listFolders()
    {
        return root.listFolders();
    }

    @Override
    public List<VirtualFile> listFiles()
    {
        return root.listFiles();
    }
    
    @Override
    public List<VirtualFolder> listFolders(String query)
    {
        return root.listFolders(query);
    }

    @Override
    public List<VirtualFile> listFiles(String query)
    {
        return root.listFiles(query);
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
    public void mount(String path, VfsSource source)
    {
        mount(new Path(path), source);
    }

    @Override
    public void mountResource(Path path, String resource) throws IOException, URISyntaxException
    {
        mount(path, new ClassPathVfsSource(resource));
    }

    @Override
    public void mountResource(String path, String resource) throws IOException, URISyntaxException
    {
        mount(new Path(path), new ClassPathVfsSource(resource));
    }

    @Override
    public void mountFile(Path path, File file)
    {
        mount(path, new FileVfsSource(file));
    }

    @Override
    public void mountFile(String path, File file)
    {
        mount(new Path(path), new FileVfsSource(file));
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

    @Override
    public void travel(VirtualFileVisitor visitor, String query)
    {
        root.travel(visitor, query);
    }

    @Override
    public void travel(VirtualFolderVisitor visitor, String query)
    {
        root.travel(visitor, query);
    }
}
