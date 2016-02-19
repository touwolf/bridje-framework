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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsSource;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFileVisitor;
import org.bridje.vfs.VirtualFolder;
import org.bridje.vfs.VirtualFolderVisitor;

class PhysicalFolder extends PhysicalResource implements VirtualFolder
{
    private static final Logger LOG = Logger.getLogger(PhysicalFolder.class.getName());

    public PhysicalFolder(VfsSource source, Path mountPath, Path path)
    {
        super(source, mountPath, path);
    }

    @Override
    public VirtualFolder findFolder(String path)
    {
        return findFolder(new Path(path));
    }

    @Override
    public VirtualFolder findFolder(Path path)
    {
        try
        {
            if(getSource().folderExists(getPhysicalPath(path)))
            {
                return createChildFolder(path);
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public VirtualFile findFile(String path)
    {
        return findFile(new Path(path));
    }

    @Override
    public VirtualFile findFile(Path path)
    {
        try
        {
            if(getSource().fileExists(getPhysicalPath(path)))
            {
                return createChildFile(path);
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<VirtualFolder> listFolders()
    {
        List<VirtualFolder> result = new LinkedList<>();
        try
        {
            if(getSource().folderExists(getPhysicalPath()))
            {
                List<String> folders = getSource().listFolders(getPhysicalPath());
                if(folders != null)
                {
                    for (String folder : folders)
                    {
                        VirtualFolder vf = createChildFolder(new Path(folder));
                        if(vf != null)
                        {
                            result.add(vf);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<VirtualFile> listFiles()
    {
        List<VirtualFile> result = new LinkedList<>();
        try
        {
            List<String> files = getSource().listFiles(getPhysicalPath());
            if(files != null)
            {
                for (String file : files)
                {
                    VirtualFile vf = createChildFile(new Path(file));
                    if(vf != null)
                    {
                        result.add(vf);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    private VirtualFolder createChildFolder(Path path)
    {
        return new PhysicalFolder(getSource(), getMountPath(), getRelativePath().join(path));
    }

    private VirtualFile createChildFile(Path path)
    {
        try
        {
            Object[] files = getSource().getFiles(getPhysicalPath(path));
            if(files != null)
            {
                if(files.length > 0)
                {
                    if(files.length == 1)
                    {
                        return new PhysicalFile(files[0], getSource(), getMountPath(), getRelativePath().join(path));
                    }
                    if(files.length > 1)
                    {
                        ProxyFile pf = new ProxyFile();
                        for (Object file : files)
                        {
                            pf.add(new PhysicalFile(file, getSource(), getMountPath(), getRelativePath().join(path)));
                        }
                        return pf;
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void travel(VirtualFileVisitor visitor)
    {
        travel(this, visitor);
    }

    @Override
    public void travel(VirtualFolderVisitor visitor)
    {
        travel(this, visitor);
    }
}
