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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.MultiVFile;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFolder;

class ProxyFolder implements VFolder
{
    private final List<VFolder> folders;

    public ProxyFolder()
    {
        folders = new LinkedList<>();
    }

    public ProxyFolder(List<VFolder> folders)
    {
        this.folders = folders;
    }

    @Override
    public VFolder findFolder(String path)
    {
        return findFolder(new Path(path));
    }

    @Override
    public VFolder findFolder(Path path)
    {
        List<VFolder> result = new LinkedList<>();
        if(folders != null)
        {
            for (VFolder folder : folders)
            {
                VFolder ff = folder.findFolder(path);
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
    public VFile findFile(String path)
    {
        return findFile(new Path(path));
    }

    @Override
    public VFile findFile(Path path)
    {
        List<VFile> result = new LinkedList<>();
        if(folders != null)
        {
            for (VFolder folder : folders)
            {
                VFile ff = folder.findFile(path);
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
    public List<VFolder> listFolders()
    {
        return listFolders(null);
    }

    @Override
    public List<VFile> listFiles()
    {
        return listFiles(null);
    }

    @Override
    public List<VFolder> listFolders(String query)
    {
        if(folders != null)
        {
            List<VFolder> result = new LinkedList<>();
            Map<String, List<VFolder>> foldersMap = new LinkedHashMap<>();
            folders.stream().forEach((folder) ->
            {
                String path = folder.getPath().toString();
                folder
                    .listFolders().stream()
                    .filter((f) -> query == null ||
                            f.getPath().globMatches(query) ||
                            f.getPathFrom(path).globMatches(query))
                    .forEach((chFolder) ->
                    {
                        List<VFolder> lst = foldersMap.get(chFolder.getName());
                        if(lst == null)
                        {
                            lst = new LinkedList<>();
                        }
                        lst.add(chFolder);
                        foldersMap.put(chFolder.getName(), lst);
                    });
            });

            for (Map.Entry<String, List<VFolder>> entry : foldersMap.entrySet())
            {
                List<VFolder> value = entry.getValue();
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
    public List<VFile> listFiles(String query)
    {
        if(folders != null)
        {
            List<VFile> result = new LinkedList<>();
            Map<String, List<VFile>> filesMap = new LinkedHashMap<>();
            folders.stream().forEach((folder) ->
            {
                String path = folder.getPath().toString();
                folder
                    .listFiles().stream()
                    .filter((f) -> query == null ||
                            f.getPath().globMatches(query) ||
                            f.getPathFrom(path).globMatches(query))
                    .forEach((chFile) ->
                    {
                        List<VFile> lst = filesMap.get(chFile.getName());
                        if(lst == null)
                        {
                            lst = new LinkedList<>();
                        }
                        lst.add(chFile);
                        filesMap.put(chFile.getName(), lst);
                    });
            });

            for (Map.Entry<String, List<VFile>> entry : filesMap.entrySet())
            {
                List<VFile> value = entry.getValue();
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
    public VFolder getParent()
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

    @Override
    public Path getPathFrom(String ancestorPath)
    {
        if(folders == null || folders.isEmpty())
        {
            return null;
        }
        return folders.get(0).getPathFrom(ancestorPath);
    }

    public void add(VFolder vf)
    {
        folders.add(0, vf);
    }

    @Override
    public VFile createNewFile(String filePath) throws IOException
    {
        return createNewFile(new Path(filePath));
    }

    @Override
    public VFolder mkDir(String folderPath) throws IOException
    {
        return mkDir(new Path(folderPath));
    }

    @Override
    public boolean canCreateNewFile(String filePath)
    {
        return canCreateNewFile(new Path(filePath));
    }

    @Override
    public boolean canMkDir(String folderPath)
    {
        return canMkDir(new Path(folderPath));
    }

    @Override
    public VFile createNewFile(Path filePath) throws IOException
    {
        if(folders != null && folders.size() >= 1)
        {
            return folders.get(0).createNewFile(filePath);
        }
        throw new IOException("Cannot create physical file here.");
    }

    @Override
    public VFolder mkDir(Path folderPath) throws IOException
    {
        if(folders != null && folders.size() >= 1)
        {
            return folders.get(0).mkDir(folderPath);
        }
        throw new IOException("Cannot create physical folder here.");
    }

    @Override
    public boolean canCreateNewFile(Path filePath)
    {
        if(folders != null && folders.size() >= 1)
        {
            return folders.get(0).canCreateNewFile(filePath);
        }
        return false;
    }

    @Override
    public boolean canMkDir(Path folderPath)
    {
        if(folders != null && folders.size() >= 1)
        {
            return folders.get(0).canMkDir(folderPath);
        }
        return false;
    }

    @Override
    public <T> T readFile(String path, Class<T> resultCls) throws IOException
    {
        if(folders != null && folders.size() >= 1)
        {
            return folders.get(0).readFile(path, resultCls);
        }
        return null;
    }

    @Override
    public <T> T readFile(Path path, Class<T> resultCls) throws IOException
    {
        if(folders != null && folders.size() >= 1)
        {
            return folders.get(0).readFile(path, resultCls);
        }
        return null;
    }

    @Override
    public <T> void writeFile(String path, T contentObj) throws IOException
    {
        if(folders != null && folders.size() >= 1)
        {
            folders.get(0).writeFile(path, contentObj);
        }
        throw new IOException("Invalid proxy folder.");
    }

    @Override
    public <T> void writeFile(Path path, T contentObj) throws IOException
    {
        if(folders != null && folders.size() >= 1)
        {
            folders.get(0).writeFile(path, contentObj);
        }
        throw new IOException("Invalid proxy folder.");
    }

    @Override
    public <T> VFile createAndWriteNewFile(Path filePath, T contentObj) throws IOException
    {
        VFile file = createNewFile(filePath);
        if(!file.canOpenForWrite())
        {
            throw new IOException("The file cannot be open for writing");
        }
        writeFile(filePath, contentObj);
        return file;
    }

    @Override
    public <T> VFile createAndWriteNewFile(String filePath, T contentObj) throws IOException
    {
        return createAndWriteNewFile(new Path(filePath), contentObj);
    }

    @Override
    public <T> List<T> readAllFiles(String path, Class<T> resultCls) throws IOException
    {
        return readAllFiles(new Path(path), resultCls);
    }

    @Override
    public <T> List<T> readAllFiles(Path path, Class<T> resultCls) throws IOException
    {
        VfsServiceImpl vfsServ = Ioc.context().find(VfsServiceImpl.class);
        List<T> result = new ArrayList<>();
        VFile file = findFile(path);
        if(file instanceof MultiVFile)
        {
            MultiVFile mvf = (MultiVFile)file;
            List<VFile> files = mvf.getFiles();
            for (VFile f : files)
            {
                T content = vfsServ.readFile(f, resultCls);
                if(content != null)
                {
                    result.add(content);
                }
            }
        }
        else
        {
            T content = vfsServ.readFile(file, resultCls);
            if(content != null)
            {
                result.add(content);
            }
        }
        return result;
    }
}
