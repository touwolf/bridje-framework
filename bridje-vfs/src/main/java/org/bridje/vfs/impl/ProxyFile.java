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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import org.bridje.vfs.MultiVirtualFile;
import org.bridje.vfs.Path;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFolder;

class ProxyFile implements MultiVirtualFile
{
    private final List<VirtualFile> files;

    public ProxyFile()
    {
        this.files = new LinkedList<>();
    }

    public ProxyFile(List<VirtualFile> files)
    {
        this.files = files;
    }

    @Override
    public List<VirtualFile> getFiles()
    {
        return files;
    }

    @Override
    public InputStream openForRead() throws IOException
    {
        return getDefFile().openForRead();
    }

    @Override
    public OutputStream openForWrite() throws IOException
    {
        return getDefFile().openForWrite();
    }

    @Override
    public boolean canOpenForWrite()
    {
        return getDefFile().canOpenForWrite();
    }

    @Override
    public VirtualFolder getParent()
    {
        return getDefFile().getParent();
    }

    @Override
    public String getName()
    {
        return getDefFile().getName();
    }

    @Override
    public Path getPath()
    {
        return getDefFile().getPath();
    }

    @Override
    public Path getParentPath()
    {
        return getDefFile().getParentPath();
    }
    
    public void add(VirtualFile vf)
    {
        files.add(vf);
    }

    private VirtualFile getDefFile()
    {
        return files.get(files.size()-1);
    }

    @Override
    public String getExtension()
    {
        int lastIndexOf = getName().lastIndexOf(".");
        if(lastIndexOf > -1)
        {
            return getName().substring(lastIndexOf+1);
        }
        return "";
    }
}
