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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.bridje.core.vfs.Path;
import org.bridje.core.vfs.VirtualFile;

class ByteArrayFile extends MemoryFile implements VirtualFile
{
    private final byte[] bytes;

    public ByteArrayFile(byte[] bytes, Path path)
    {
        super(path);
        this.bytes = bytes;
    }
    
    @Override
    public InputStream open()
    {
        return new ByteArrayInputStream(bytes);
    }
}
