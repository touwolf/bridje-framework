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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.vfs.CpSource;
import org.bridje.vfs.FileSource;
import org.bridje.vfs.MultiVFile;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileAdapter;
import org.bridje.vfs.VFolder;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VfsSource;

@Component
class VfsServiceImpl implements VfsService
{
    private static final Logger LOG = Logger.getLogger(VfsServiceImpl.class.getName());

    private MemoryFolder root;

    @Inject
    private VFileAdapter[] fileAdapters;

    private Map<String, Map<Class<?>, List<VFileAdapter>>> adaptersMap;

    private Map<String, List<VFileAdapter>> genericReadersMap;

    @PostConstruct
    public void init()
    {
        root = new MemoryFolder(null);
        initFileAdapters();
        try
        {
            mountResource("/vfs", "/BRIDJE-INF/vfs");
            List<Properties> mountCpList = readAllFiles("/vfs/classpath-sources.properties", Properties.class);
            for (Properties mountCp : mountCpList)
            {
                mountCp.forEach((p, r) ->
                {
                    try
                    {
                        mountResource((String)p, (String)r);
                    }
                    catch (IOException | URISyntaxException e)
                    {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                });
            }
        }
        catch (IOException | URISyntaxException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public VFolder findFolder(String path)
    {
        return root.findFolder(path);
    }

    @Override
    public VFolder findFolder(Path path)
    {
        return root.findFolder(path);
    }

    @Override
    public VFile findFile(String path)
    {
        return root.findFile(path);
    }

    @Override
    public VFile findFile(Path path)
    {
        return root.findFile(path);
    }

    @Override
    public List<VFolder> listFolders()
    {
        return root.listFolders();
    }

    @Override
    public List<VFile> listFiles()
    {
        return root.listFiles();
    }

    @Override
    public List<VFolder> listFolders(String query)
    {
        return root.listFolders(query);
    }

    @Override
    public List<VFile> listFiles(String query)
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
                VFolder currVFolder = root.findFolder(currentPath);
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
        mount(path, new CpSource(resource));
    }

    @Override
    public void mountResource(String path, String resource) throws IOException, URISyntaxException
    {
        mount(new Path(path), new CpSource(resource));
    }

    @Override
    public void mountFile(Path path, File file)
    {
        mount(path, new FileSource(file));
    }

    @Override
    public void mountFile(String path, File file)
    {
        mount(new Path(path), new FileSource(file));
    }

    @Override
    public void mountFile(Path path, String file)
    {
        mountFile(path, new File(file));
    }

    @Override
    public void mountFile(String path, String file)
    {
        mountFile(path, new File(file));
    }

    @Override
    public <T> T readFile(String path, Class<T> resultCls) throws IOException
    {
        VFile file = findFile(path);
        return readFile(file, resultCls);
    }

    @Override
    public <T> T readFile(Path path, Class<T> resultCls) throws IOException
    {
        VFile file = findFile(path);
        return readFile(file, resultCls);
    }

    @Override
    public <T> void writeFile(String path, T contentObj) throws IOException
    {
        VFile file = findFile(path);
        writeFile(file, contentObj);
    }

    @Override
    public <T> void writeFile(Path path, T contentObj) throws IOException
    {
        VFile file = findFile(path);
        writeFile(file, contentObj);
    }

    public <T> T readFile(VFile file, Class<T> resultCls) throws IOException
    {
        if(file != null)
        {
            Map<Class<?>, List<VFileAdapter>> map = adaptersMap.get(file.getExtension());
            if(map != null)
            {
                List<VFileAdapter> lst = map.get(resultCls);
                if(lst != null)
                {
                    for (VFileAdapter adapter : lst)
                    {
                        if(adapter.canHandle(file, resultCls))
                        {
                            return adapter.read(file, resultCls);
                        }
                    }
                }
            }

            List<VFileAdapter> lst = genericReadersMap.get(file.getExtension());
            if(lst != null)
            {
                for (VFileAdapter adapter : lst)
                {
                    if(adapter.canHandle(file, resultCls))
                    {
                        return adapter.read(file, resultCls);
                    }
                }
            }
        }
        return null;
    }

    public <T> void writeFile(VFile file, T contentObj) throws IOException
    {
        if(file != null)
        {
            Map<Class<?>, List<VFileAdapter>> map = adaptersMap.get(file.getExtension());
            if(map != null)
            {
                List<VFileAdapter> lst = map.get(contentObj.getClass());
                if(lst != null)
                {
                    for (VFileAdapter adapter : lst)
                    {
                        if(adapter.canHandle(file, contentObj.getClass()))
                        {
                            adapter.write(file, contentObj);
                        }
                    }
                }
            }

            List<VFileAdapter> lst = genericReadersMap.get(file.getExtension());
            if(lst != null)
            {
                for (VFileAdapter adapter : lst)
                {
                    if(adapter.canHandle(file, contentObj.getClass()))
                    {
                        adapter.write(file, contentObj);
                    }
                }
            }
        }
    }

    private void initFileAdapters()
    {
        genericReadersMap = new HashMap<>();
        adaptersMap = new HashMap<>();
        for (VFileAdapter reader : fileAdapters)
        {
            String[] extensions = reader.getExtensions();
            for (String extension : extensions)
            {
                Class<?>[] clsArray = reader.getClasses();
                if(clsArray != null)
                {
                    Map<Class<?>, List<VFileAdapter>> clsMap = adaptersMap.get(extension);
                    if(clsMap == null)
                    {
                        clsMap = new HashMap<>();
                        adaptersMap.put(extension, clsMap);
                    }
                    for (Class<?> cls : clsArray)
                    {
                        List<VFileAdapter> lst = clsMap.get(cls);
                        if(lst == null)
                        {
                            lst = new ArrayList<>();
                            clsMap.put(cls, lst);
                        }
                        lst.add(reader);
                    }
                }
                else
                {
                    List<VFileAdapter> lst = genericReadersMap.get(extension);
                    if(lst == null)
                    {
                        lst = new ArrayList<>();
                        genericReadersMap.put(extension, lst);
                    }
                    lst.add(reader);
                }
            }
        }
    }

    @Override
    public VFile createNewFile(Path filePath) throws IOException
    {
        return root.createNewFile(filePath);
    }

    @Override
    public VFolder mkDir(Path folderPath) throws IOException
    {
        return root.mkDir(folderPath);
    }

    @Override
    public boolean canCreateNewFile(Path filePath)
    {
        return root.canCreateNewFile(filePath);
    }

    @Override
    public boolean canMkDir(Path folderPath)
    {
        return root.canMkDir(folderPath);
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
    public VFolder getParent()
    {
        return root.getParent();
    }

    @Override
    public String getName()
    {
        return root.getName();
    }

    @Override
    public Path getPath()
    {
        return root.getPath();
    }

    @Override
    public Path getParentPath()
    {
        return root.getParentPath();
    }

    @Override
    public Path getPathFrom(String ancestorPath)
    {
        return root.getPathFrom(ancestorPath);
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
        List<T> result = new ArrayList<>();
        VFile file = findFile(path);
        if(file instanceof MultiVFile)
        {
            MultiVFile mvf = (MultiVFile)file;
            List<VFile> files = mvf.getFiles();
            for (VFile f : files)
            {
                T content = readFile(f, resultCls);
                if(content != null)
                {
                    result.add(content);
                }
            }
        }
        else
        {
            T content = readFile(file, resultCls);
            if(content != null)
            {
                result.add(content);
            }
        }
        return result;
    }
}
