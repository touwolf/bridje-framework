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

package org.bridje.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a file of the VFS tree.
 */
public interface VirtualFile extends VirtualResource
{
    /**
     * Open a file's InputStream for reading it's content.
     * <p>
     * @return An InputStream to the file.
     * @throws IOException This method may access to physical device so this
     *         exception may be throw if any input output operation fails.
     */
    InputStream openForRead() throws IOException;
    
    /**
     * Open a file's OutputStream for writing it's content.
     * <p>
     * @return An OutputStream to the file.
     * @throws IOException This method may access to physical device so this
     *         exception may be throw if any input output operation fails.
     */
    OutputStream openForWrite() throws IOException;

    /**
     * Determines when ever the file can be open for writing.
     * <p>
     * @return true the file can be open for writing.
     */
    boolean canOpenForWrite();

    /**
     * Gets the extension part of the name of the file.
     * 
     * @return The extension part of the name of the file.
     */
    public String getExtension();
}
