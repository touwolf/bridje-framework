/*
 * Copyright 2016 Bridje Framework.
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

/**
 * This interface must be implemented by any component that can read/write the
 * content of the files into objects the given classes. The VFS will use all
 * implementations of this interface to covert the content of the files to the
 * required classes.
 */
public interface VirtualFileAdapter
{
    /**
     * Gets all the file extensions that this adapter can handle.
     *
     * @return An array with the file extensions.
     */
    String[] getExtensions();

    /**
     * Gets all the classes that this adapter can handle.
     *
     * @return An array with all the classes.
     */
    Class<?>[] getClasses();

    /**
     * Determines when ever this adapter can handle the given file into the
     * given result class.
     *
     * @param vf The virtual file to serialize/deserialize.
     * @param resultCls The result class for the content of the file.
     * @return
     */
    boolean canHandle(VirtualFile vf, Class<?> resultCls);

    /**
     * Read the content of the given file into the result class.
     *
     * @param <T> The type of the resulting object for the read of the content
     * of the file.
     * @param vf The file to be read.
     * @param resultCls The result class for the content of the file.
     * @return An object of the result class with the content of the file.
     * @throws IOException If any exception occurs reading the file.
     */
    <T> T read(VirtualFile vf, Class<T> resultCls) throws IOException;

    /**
     * Read the content of the given file into the result class.
     *
     * @param <T> The type of the object to be written to the file.
     * @param vf The file to be write.
     * @param contentObj The object to be write to the file.
     * @throws IOException If any exception occurs reading the file.
     */
    <T> void write(VirtualFile vf, T contentObj) throws IOException;
}
