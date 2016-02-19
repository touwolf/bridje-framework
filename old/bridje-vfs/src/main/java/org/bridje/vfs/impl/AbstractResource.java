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


import java.util.List;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFileVisitor;
import org.bridje.vfs.VirtualFolder;
import org.bridje.vfs.VirtualFolderVisitor;
import org.bridje.vfs.VirtualResource;

abstract class AbstractResource implements VirtualResource
{
    private static VfsService VFS;

    private final Path path;

    AbstractResource(Path path)
    {
        this.path = path;
    }

    @Override
    public VirtualFolder getParent()
    {
        if (getPath() == null)
        {
            return null;
        }

        return vfs().findFolder(getPath().getParent());
    }

    @Override
    public String getName()
    {
        if(getPath() == null)
        {
            return null;
        }
        return getPath().getName();
    }

    @Override
    public Path getPath()
    {
        return path;
    }

    @Override
    public Path getParentPath()
    {
        if(getPath() == null)
        {
            return null;
        }
        return getPath().getParent();
    }

    static VfsService vfs()
    {
        if(VFS == null)
        {
            VFS = Ioc.context().find(VfsService.class);
        }
        return VFS;
    }

    @Override
    public String toString()
    {
        return getPath().toString();
    }
    
    static void travel(VirtualFolder rootFolder, VirtualFileVisitor visitor)
    {
        List<VirtualFolder> listFolders = rootFolder.listFolders();
        if(listFolders != null)
        {
            for (VirtualFolder folder : listFolders)
            {
                folder.travel(visitor);
            }
        }
        List<VirtualFile> listFiles = rootFolder.listFiles();
        if(listFiles != null)
        {
            for (VirtualFile file : listFiles)
            {
                visitor.visit(file);
            }
        }
    }

    static void travel(VirtualFolder rootFolder, VirtualFolderVisitor visitor)
    {
        List<VirtualFolder> listFolders = rootFolder.listFolders();
        if(listFolders != null)
        {
            for (VirtualFolder folder : listFolders)
            {
                visitor.visit(folder);
                folder.travel(visitor);
            }
        }
    }
}
