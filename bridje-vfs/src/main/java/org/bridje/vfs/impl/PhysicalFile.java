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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFolder;
import org.bridje.vfs.VfsSource;

class PhysicalFile extends PhysicalResource implements VFile
{
    private static final Logger LOG = Logger.getLogger(PhysicalFile.class.getName());

    private final Object data;

    public PhysicalFile(Object data, VfsSource source, Path mountPath, Path path)
    {
        super(source, mountPath, path);
        this.data = data;
    }

    @Override
    public InputStream openForRead() throws IOException
    {
        return getSource().openForRead(data);
    }

    @Override
    public OutputStream openForWrite() throws IOException
    {
        return getSource().openForWrite(data);
    }

    @Override
    public boolean canOpenForWrite()
    {
        return getSource().canOpenForWrite(data);
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
        return MimeTypeUtils.getInstance().getMimeType(getExtension());
    }

    @Override
    public <T> T readFile(Class<T> resultCls) throws IOException
    {
        return Ioc.context().find(VfsServiceImpl.class).readFile(this, resultCls);
    }

    @Override
    public <T> void writeFile(T contentObj) throws IOException
    {
        Ioc.context().find(VfsServiceImpl.class).writeFile(this, contentObj);
    }

    @Override
    public boolean canDelete()
    {
        return getSource().canDelete(data);
    }

    @Override
    public void delete() throws IOException
    {
        getSource().delete(data);
    }

    @Override
    public void copyTo(VFolder folder) throws IOException
    {
        if(!folder.canCreateNewFile(getName()))
        {
            throw new IOException("The given file cannot be created on the destiny folder.");
        }
        VFile file = folder.createNewFile(getName());
        if(!file.canOpenForWrite())
        {
            throw new IOException("The given file cannot be open for writing.");
        }
        try(OutputStream os = file.openForWrite())
        {
            try(InputStream is = openForRead())
            {
                copy(is, os);
            }
        }
    }

    @Override
    public void copyTo(OutputStream os) throws IOException
    {
        try(InputStream is = openForRead())
        {
            copy(is, os);
        }
    }
    
    @Override
    public void moveTo(VFolder folder) throws IOException
    {
        if(!canDelete())
        {
            throw new IOException("The file cannot be moved");
        }
        try
        {
            copyTo(folder);
            delete();
        }
        catch(IOException ex)
        {
            try
            {
                VFile f = folder.findFile(getName());
                if(f != null)
                {
                    f.delete();
                }
            }
            catch (IOException e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
            throw ex;
        }
    }

    private void copy(InputStream is, OutputStream os) throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesCount = is.read(buffer);
        while(bytesCount > -1)
        {
            os.write(buffer, 0, bytesCount);
            bytesCount = is.read(buffer);
        }
    }
}
