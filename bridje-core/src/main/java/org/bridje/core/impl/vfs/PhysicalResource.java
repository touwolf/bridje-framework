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

package org.bridje.core.impl.vfs;

import org.bridje.core.vfs.Path;
import org.bridje.core.vfs.VfsSource;
import org.bridje.core.vfs.VirtualResource;

class PhysicalResource extends AbstractResource implements VirtualResource
{
    private final VfsSource source;
    
    private final Path mountPath;

    public PhysicalResource(VfsSource source, Path mountPath, Path path)
    {
        super(path);
        this.source = source;
        this.mountPath = mountPath;
    }

    public VfsSource getSource()
    {
        return source;
    }

    @Override
    public Path getPath()
    {
        Path p = super.getPath();
        if(p != null)
        {
            if(mountPath != null)
            {
                return mountPath.join(super.getPath());
            }
            return p;
        }
        return mountPath;
    }
    
    public Path getRelativePath()
    {
        return super.getPath();
    }

    public Path getPhysicalPath()
    {
        return getRelativePath().getNext();
    }
    
    public Path getPhysicalPath(Path path)
    {
        if(getRelativePath().isLast())
        {
            return path;
        }
        return getPhysicalPath().join(path);
    }

    public Path getMountPath()
    {
        return mountPath;
    }
}
