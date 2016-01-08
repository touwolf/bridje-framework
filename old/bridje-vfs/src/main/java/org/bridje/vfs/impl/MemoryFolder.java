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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bridje.vfs.Path;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFileVisitor;
import org.bridje.vfs.VirtualFolder;
import org.bridje.vfs.VirtualFolderVisitor;

class MemoryFolder extends AbstractResource implements VirtualFolder
{
    private Map<String,VirtualFolder> folders;

    private Map<String, VirtualFile> files;

    MemoryFolder(Path path)
    {
        super(path);
    }

    @Override
    public VirtualFolder findFolder(Path path)
    {
        if(folders == null || path == null)
        {
            return null;
        }
        if(path.isLast())
        {
            if(path.isSelf())
            {
                return this;
            }
            else if(path.isParent())
            {
                return getParent();
            }
            return folders.get(path.getName());
        }
        else
        {
            VirtualFolder folder;
            if(path.isSelf())
            {
                folder = this;
            }
            else if(path.isParent())
            {
                folder = getParent();
            }
            else
            {
                folder = folders.get(path.getFirstElement());
            }
            if(folder == null)
            {
                return null;
            }
            return folder.findFolder(path.getNext());
        }
    }

    @Override
    public VirtualFile findFile(Path path)
    {
        if(path == null)
        {
            return null;
        }
        if(path.isLast())
        {
            if(files == null || path.isSelf() || path.isParent())
            {
                return null;
            }
            return files.get(path.getFirstElement());
        }
        else
        {
            if(folders == null)
            {
                return null;
            }
            VirtualFolder nextFolder;
            if(path.isSelf())
            {
                nextFolder = this;
            }
            else
            {
                if(path.isParent())
                {
                    nextFolder = getParent();
                }
                else
                {
                    nextFolder = folders.get(path.getFirstElement());
                }
            }
            if(nextFolder == null)
            {
                return null;
            }
            return nextFolder.findFile(path.getNext());
        }
    }
    
    @Override
    public VirtualFolder findFolder(String path)
    {
        return findFolder(new Path(path));
    }

    @Override
    public VirtualFile findFile(String path)
    {
        return findFile(new Path(path));
    }

    @Override
    public List<VirtualFolder> listFolders()
    {
        if(folders == null)
        {
            return null;
        }
        List<VirtualFolder> result = new LinkedList<>();
        result.addAll(folders.values());
        return result;
    }

    @Override
    public List<VirtualFile> listFiles()
    {
        if(files == null)
        {
            return null;
        }
        List<VirtualFile> result = new LinkedList<>();
        result.addAll(files.values());
        return result;
    }

    <T extends VirtualFolder> T addFolder(T virtualFolder)
    {
        if(virtualFolder == null || virtualFolder.getName() == null || virtualFolder.getName().trim().isEmpty())
        {
            return null;
        }
        if(folders == null)
        {
            folders = new LinkedHashMap<>();
        }
        VirtualFolder vf = folders.get(virtualFolder.getName());
        if(vf != null)
        {
            if(vf instanceof ProxyFolder)
            {
                ((ProxyFolder)vf).add(virtualFolder);
            }
            else
            {
                ProxyFolder pf = new ProxyFolder();
                pf.add(vf);
                pf.add(virtualFolder);
            }
        }
        else
        {
            folders.put(virtualFolder.getName(), virtualFolder);
        }
        return virtualFolder;
    }

    void removeFolder(VirtualFolder virtualFolder)
    {
        if(folders != null)
        {
            folders.remove(virtualFolder.getName());
        }
    }

    <T extends VirtualFile> T addFile(T virtualFile)
    {
        if(virtualFile == null || virtualFile.getName() == null || virtualFile.getName().trim().isEmpty())
        {
            return null;
        }
        if(files == null)
        {
            files = new LinkedHashMap<>();
        }
        VirtualFile vf = files.get(virtualFile.getName());
        if(vf != null)
        {
            if(vf instanceof ProxyFile)
            {
                ((ProxyFile)vf).add(virtualFile);
            }
            else
            {
                ProxyFile pf = new ProxyFile();
                pf.add(vf);
                pf.add(virtualFile);
            }
        }
        else
        {
            files.put(virtualFile.getName(), virtualFile);
        }
        return virtualFile;
    }

    void removeFile(VirtualFile virtualFile)
    {
        if(files == null)
        {
            files = new LinkedHashMap<>();
        }
        files.remove(virtualFile.getName());
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
