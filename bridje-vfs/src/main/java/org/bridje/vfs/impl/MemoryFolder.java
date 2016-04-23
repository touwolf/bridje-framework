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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bridje.vfs.Path;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFileVisitor;
import org.bridje.vfs.VirtualFolder;
import org.bridje.vfs.VirtualFolderVisitor;

class MemoryFolder extends AbstractResource implements VirtualFolder
{
    private Map<String,VirtualFolder> foldersMap;

    private Map<String, VirtualFile> filesMap;
    
    private List<VirtualFolder> folders;
    
    private List<VirtualFile> files;

    MemoryFolder(Path path)
    {
        super(path);
    }

    @Override
    public VirtualFolder findFolder(Path path)
    {
        if(foldersMap == null || path == null)
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
            return foldersMap.get(path.getName());
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
                folder = foldersMap.get(path.getFirstElement());
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
            if(filesMap == null || path.isSelf() || path.isParent())
            {
                return null;
            }
            return filesMap.get(path.getFirstElement());
        }
        else
        {
            if(foldersMap == null)
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
                    nextFolder = foldersMap.get(path.getFirstElement());
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
        if(folders == null) return Collections.EMPTY_LIST;
        return Collections.unmodifiableList(folders);
    }
    
    @Override
    public List<VirtualFolder> listFolders(String query)
    {
        if(folders == null) return Collections.EMPTY_LIST;
        List<VirtualFolder> result = new ArrayList<>();
        for (VirtualFolder folder : folders)
        {
            if(query == null || folder.getPath().toString().matches(query))
            {
                result.add(folder);
            }
        }
        return Collections.unmodifiableList(result);
    }
    
    @Override
    public List<VirtualFile> listFiles()
    {
        if(files == null) return Collections.EMPTY_LIST;
        return Collections.unmodifiableList(files);
    }

    @Override
    public List<VirtualFile> listFiles(String query)
    {
        if(files == null) return Collections.EMPTY_LIST;
        List<VirtualFile> result = new ArrayList<>();
        for (VirtualFile file : files)
        {
            if(query == null || file.getPath().toString().matches(query))
            {
                result.add(file);
            }
        }
        return Collections.unmodifiableList(result);
    }

    <T extends VirtualFolder> T addFolder(T virtualFolder)
    {
        if(virtualFolder == null || virtualFolder.getName() == null || virtualFolder.getName().trim().isEmpty())
        {
            return null;
        }
        if(foldersMap == null)
        {
            foldersMap = new LinkedHashMap<>();
            folders = new ArrayList<>();
        }
        VirtualFolder vf = foldersMap.get(virtualFolder.getName());
        if(vf != null)
        {
            if(vf instanceof ProxyFolder)
            {
                ((ProxyFolder)vf).add(virtualFolder);
            }
            else
            {
                folders.remove(vf);
                ProxyFolder pf = new ProxyFolder();
                pf.add(vf);
                pf.add(virtualFolder);
                foldersMap.put(pf.getName(), pf);
                folders.add(pf);
            }
        }
        else
        {
            foldersMap.put(virtualFolder.getName(), virtualFolder);
            folders.add(virtualFolder);
        }
        return virtualFolder;
    }

    void removeFolder(VirtualFolder virtualFolder)
    {
        if(foldersMap != null)
        {
            folders.remove(foldersMap.get(virtualFolder.getName()));
            foldersMap.remove(virtualFolder.getName());
        }
    }

    <T extends VirtualFile> T addFile(T virtualFile)
    {
        if(virtualFile == null || virtualFile.getName() == null || virtualFile.getName().trim().isEmpty())
        {
            return null;
        }
        if(filesMap == null)
        {
            filesMap = new HashMap<>();
            files = new ArrayList<>();
        }
        VirtualFile vf = filesMap.get(virtualFile.getName());
        if(vf != null)
        {
            if(vf instanceof ProxyFile)
            {
                ((ProxyFile)vf).add(virtualFile);
            }
            else
            {
                files.remove(vf);
                ProxyFile pf = new ProxyFile();
                pf.add(vf);
                pf.add(virtualFile);
                filesMap.put(vf.getName(), pf);
                files.add(pf);
            }
        }
        else
        {
            filesMap.put(virtualFile.getName(), virtualFile);
            files.add(virtualFile);
        }
        return virtualFile;
    }

    void removeFile(VirtualFile virtualFile)
    {
        if(filesMap == null)
        {
            return;
        }
        files.remove(filesMap.get(virtualFile.getName()));
        filesMap.remove(virtualFile.getName());
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

    @Override
    public void travel(VirtualFileVisitor visitor, String query)
    {
        travel(this, visitor, query);
    }

    @Override
    public void travel(VirtualFolderVisitor visitor, String query)
    {
        travel(this, visitor, query);
    }
}
