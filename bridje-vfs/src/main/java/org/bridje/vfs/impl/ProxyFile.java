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
import org.bridje.vfs.MultiVFile;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFolder;

class ProxyFile extends MimeType implements MultiVFile
{
    private final List<VFile> files;

    public ProxyFile()
    {
        files = new LinkedList<>();
    }

    public ProxyFile(List<VFile> files)
    {
        this.files = files;
    }

    @Override
    public List<VFile> getFiles()
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
    public VFolder getParent()
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

    public void add(VFile vf)
    {
        files.add(vf);
    }

    private VFile getDefFile()
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

    @Override
    public String getMimeType()
    {
        return MimeType.getInstance().getMimeType(getExtension());
    }

    @Override
    public <T> T readFile(Class<T> resultCls) throws IOException
    {
        return getDefFile().readFile(resultCls);
    }

    @Override
    public <T> void writeFile(T contentObj) throws IOException
    {
        getDefFile().writeFile(contentObj);
    }

    @Override
    public boolean canDelete()
    {
        return getDefFile().canDelete();
    }

    @Override
    public void delete() throws IOException
    {
        getDefFile().delete();
    }

    @Override
    public void copyTo(VFolder folder) throws IOException
    {
        getDefFile().copyTo(folder);
    }

    @Override
    public void moveTo(VFolder folder) throws IOException
    {
        getDefFile().moveTo(folder);
    }

    @Override
    public Path getPathFrom(String ancestorPath)
    {
        return getDefFile().getPathFrom(ancestorPath);
    }
}
