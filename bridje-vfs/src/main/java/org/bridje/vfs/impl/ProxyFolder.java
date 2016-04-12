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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bridje.vfs.Path;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFileVisitor;
import org.bridje.vfs.VirtualFolder;
import org.bridje.vfs.VirtualFolderVisitor;

class ProxyFolder implements VirtualFolder
{
    private final List<VirtualFolder> folders;

    public ProxyFolder()
    {
        folders = new LinkedList<>();
    }

    public ProxyFolder(List<VirtualFolder> folders)
    {
        this.folders = folders;
    }
    
    @Override
    public VirtualFolder findFolder(String path)
    {
        return findFolder(new Path(path));
    }

    @Override
    public VirtualFolder findFolder(Path path)
    {
        List<VirtualFolder> result = new LinkedList<>();
        if(folders != null)
        {
            for (VirtualFolder folder : folders)
            {
                VirtualFolder ff = folder.findFolder(path);
                if(ff != null)
                {
                    result.add(ff);
                }
            }
        }
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() == 1)
        {
            return result.get(0);
        }
        return new ProxyFolder(result);
    }

    @Override
    public VirtualFile findFile(String path)
    {
        return findFile(new Path(path));
    }

    @Override
    public VirtualFile findFile(Path path)
    {
        List<VirtualFile> result = new LinkedList<>();
        if(folders != null)
        {
            for (VirtualFolder folder : folders)
            {
                VirtualFile ff = folder.findFile(path);
                if(ff != null)
                {
                    result.add(ff);
                }
            }
        }
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() == 1)
        {
            return result.get(0);
        }
        return new ProxyFile(result);
    }

    @Override
    public List<VirtualFolder> listFolders()
    {
        return listFolders(null);
    }

    @Override
    public List<VirtualFile> listFiles()
    {
        return listFiles(null);
    }
    
    @Override
    public List<VirtualFolder> listFolders(String query)
    {
        if(folders != null)
        {
            List<VirtualFolder> result = new LinkedList<>();
            Map<String, List<VirtualFolder>> foldersMap = new LinkedHashMap<>();
            folders.stream().forEach((folder) ->
            {
                folder.listFolders().stream()
                        .filter((f) -> query != null && f.getPath().toString().matches(query))
                        .forEach((chFolder) ->
                {
                    List<VirtualFolder> lst = foldersMap.get(chFolder.getName());
                    if(lst == null)
                    {
                        lst = new LinkedList<>();
                    }
                    lst.add(chFolder);
                    foldersMap.put(chFolder.getName(), lst);
                });
            });
            
            for (Map.Entry<String, List<VirtualFolder>> entry : foldersMap.entrySet())
            {
                List<VirtualFolder> value = entry.getValue();
                if(value == null || value.isEmpty())
                {
                    continue;
                }
                if(value.size() == 1)
                {
                    result.add(value.get(0));
                }
                if(value.size() > 1)
                {
                    result.add(new ProxyFolder(value));
                }
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<VirtualFile> listFiles(String query)
    {
        if(folders != null)
        {
            List<VirtualFile> result = new LinkedList<>();
            Map<String, List<VirtualFile>> filesMap = new LinkedHashMap<>();
            folders.stream().forEach((folder) ->
            {
                folder.listFiles().stream()
                        .filter((f) -> query != null && f.getPath().toString().matches(query))
                        .forEach((chFile) ->
                {
                    List<VirtualFile> lst = filesMap.get(chFile.getName());
                    if(lst == null)
                    {
                        lst = new LinkedList<>();
                    }
                    lst.add(chFile);
                    filesMap.put(chFile.getName(), lst);
                });
            });
            
            for (Map.Entry<String, List<VirtualFile>> entry : filesMap.entrySet())
            {
                List<VirtualFile> value = entry.getValue();
                if(value == null || value.isEmpty())
                {
                    continue;
                }
                if(value.size() == 1)
                {
                    result.add(value.get(0));
                }
                if(value.size() > 1)
                {
                    result.add(new ProxyFile(value));
                }
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public VirtualFolder getParent()
    {
        if(folders == null || folders.isEmpty())
        {
            return null;
        }
        return folders.get(0).getParent();
    }

    @Override
    public String getName()
    {
        if(folders == null || folders.isEmpty())
        {
            return null;
        }
        return folders.get(0).getName();
    }

    @Override
    public Path getPath()
    {
        if(folders == null || folders.isEmpty())
        {
            return null;
        }
        return folders.get(0).getPath();
    }

    @Override
    public Path getParentPath()
    {
        if(folders == null || folders.isEmpty())
        {
            return null;
        }
        return folders.get(0).getParentPath();
    }
    
    public void add(VirtualFolder vf)
    {
        folders.add(vf);
    }

    @Override
    public void travel(VirtualFileVisitor visitor)
    {
        AbstractResource.travel(this, visitor);
    }

    @Override
    public void travel(VirtualFolderVisitor visitor)
    {
        AbstractResource.travel(this, visitor);
    }

    @Override
    public void travel(VirtualFileVisitor visitor, String query)
    {
        AbstractResource.travel(this, visitor, query);
    }

    @Override
    public void travel(VirtualFolderVisitor visitor, String query)
    {
        AbstractResource.travel(this, visitor, query);
    }
}
