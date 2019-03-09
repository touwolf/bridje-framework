/*
 * Copyright 2017 Bridje Framework.
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.bridje.vfs.GlobExpr;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsSource;

class VfsSourceWrapper implements VfsSource
{
    private final Path path;
    
    private final VfsSource source;

    public VfsSourceWrapper(Path path, VfsSource source)
    {
        this.path = path;
        this.source = source;
    }
    
    @Override
    public boolean isDirectory(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.isDirectory(leftTrim);
            return true;
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.isDirectory(leftTrim);
        }
        return false;
    }

    @Override
    public boolean isFile(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.isFile(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.isFile(leftTrim);
        }
        return false;
    }

    @Override
    public boolean exists(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.exists(leftTrim);
            return true;
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.exists(leftTrim);
        }
        return false;
    }

    @Override
    public boolean canWrite(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.canWrite(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.canWrite(leftTrim);
        }
        return false;
    }

    @Override
    public boolean canRead(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.canRead(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.canRead(leftTrim);
        }
        return false;
    }

    @Override
    public String[] list(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.list(leftTrim);
            return new String[]{leftTrim.getFirstElement()};
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.list(leftTrim);
        }
        return null;
    }

    @Override
    public InputStream openForRead(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.openForRead(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.openForRead(leftTrim);
        }
        return null;
    }

    @Override
    public OutputStream openForWrite(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.openForWrite(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.openForWrite(leftTrim);
        }
        return null;
    }

    @Override
    public List<Path> search(GlobExpr globExpr, Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.search(globExpr, leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.search(globExpr, leftTrim);
        }
        return null;
    }

    @Override
    public boolean createNewFile(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.createNewFile(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.createNewFile(leftTrim);
        }
        return false;
    }

    @Override
    public boolean mkdir(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.mkdir(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.mkdir(leftTrim);
        }
        return false;
    }

    @Override
    public boolean delete(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.delete(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.delete(leftTrim);
        }
        return false;
    }

    @Override
    public File getRawFile(Path path)
    {
        Path leftTrim = this.path.leftTrim(path);
        if(leftTrim != null)
        {
            if(leftTrim.isRoot()) return this.source.getRawFile(leftTrim);
        }
        else
        {
            leftTrim = path.leftTrim(this.path);
            if(leftTrim != null) return this.source.getRawFile(leftTrim);
        }
        return null;    }
    
}
