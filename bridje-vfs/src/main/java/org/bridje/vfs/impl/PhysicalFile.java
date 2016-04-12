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
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsSource;
import org.bridje.vfs.VirtualFile;

class PhysicalFile extends PhysicalResource implements VirtualFile
{
    private final Object data;

    public PhysicalFile(Object data, VfsSource source, Path mountPath, Path path)
    {
        super(source, mountPath, path);
        this.data = data;
    }

    @Override
    public InputStream open() throws IOException
    {
        return getSource().open(data);
    }
}
