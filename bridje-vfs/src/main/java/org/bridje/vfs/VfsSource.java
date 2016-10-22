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
import java.util.List;

/**
 * Represents a file source for the VFS API.
 * <p>
 * All VfsProvider implementation must provides an object that implements this
 * interface in order to put files in the vfs tree.
 */
public interface VfsSource
{
    /**
     * Gets all objects identifying the files in the specified path, if the file
     * represented by the path argument is a single file within the VfsSource
     * this method will return a single file object array, but if multiple files
     * within the VfsSource are map to the specified path then this method will
     * return all objects files for the path.
     * <p>
     * @param path The path to look for.
     *
     * @return An array of object, each acts as identifier for a file mapped to
     *         the path argument.
     *
     * @throws IOException This method may access to physical device so this
     *                     exception may be throw if any input output operation
     *                     fails.
     */
    Object[] getFiles(Path path) throws IOException;

    /**
     * This method retrieve all names for the child folders within the specified
     * path.
     * <p>
     * @param path The path to look for children folders.
     *
     * @return A list of string with the names of the child folders for the
     *         specified path.
     *
     * @throws IOException This method may access to physical device so this
     *                     exception may be throw if any input output operation
     *                     fails.
     */
    List<String> listFolders(Path path) throws IOException;

    /**
     * This method retrieve all names for the child files within the specified
     * path.
     * <p>
     * @param path The path to look for children files.
     *
     * @return A list of string with the names of the child file for the
     *         specified path.
     *
     * @throws IOException This method may access to physical device so this
     *                     exception may be throw if any input output operation
     *                     fails.
     */
    List<String> listFiles(Path path) throws IOException;

    /**
     * Determines if the specified path is a file from this VfsSource.
     * <p>
     * @param path The path of the file.
     *
     * @return {@literal true} if a file mapped to the path argument exists
     *         within this VfsSource.
     *
     * @throws IOException This method may access to physical device so this
     *                     exception may be throw if any input output operation
     *                     fails.
     */
    boolean fileExists(Path path) throws IOException;

    /**
     * Determines if the specified path is a folder from this VfsSource.
     * <p>
     * @param path The path of the file.
     *
     * @return {@literal true} if a folder mapped to the path argument exists
     *         within this VfsSource.
     *
     * @throws IOException This method may access to physical device so this
     *                     exception may be throw if any input output operation
     *                     fails.
     */
    boolean folderExists(Path path) throws IOException;

    /**
     * Open a file's InputStream for reading it's content.
     * <p>
     * @param data The file identifier retrieve from this VfsSource by the
     *             getFiles method.
     *
     * @return An InputStream to the file.
     *
     * @throws IOException This method may access to physical device so this
     *                     exception may be throw if any input output operation
     *                     fails.
     */
    InputStream openForRead(Object data) throws IOException;

    /**
     * Open a file's OutputStream for writing it's content.
     * <p>
     * @param data The file identifier retrieve from this VfsSource by the
     *             getFiles method.
     *
     * @return An OutputStream to the file.
     *
     * @throws IOException This method may access to physical device so this
     *                     exception may be throw if any input output operation
     *                     fails.
     */
    OutputStream openForWrite(Object data) throws IOException;

    /**
     * Determines when ever the file can or not be open for writing.
     * <p>
     * @param data The file identifier retrieve from this VfsSource by the
     *             getFiles method.
     *
     * @return true if the file can be open for writing false otherwise.
     */
    boolean canOpenForWrite(Object data);

    /**
     * Creates a new physical file on this source.
     * <p>
     * @param filePath The path to the file to be created.
     *
     * @return The file representation according this source.
     *
     * @throws java.io.IOException If any I/O exception occurs.
     */
    Object createNewFile(Path filePath) throws IOException;

    /**
     * Creates a new directory on this source.
     * <p>
     * @param folderPath The path to the folder to be created.
     *
     * @return The name of the folder created.
     *
     * @throws java.io.IOException If any I/O exception occurs.
     */
    String mkDir(Path folderPath) throws IOException;

    /**
     * Determines if a folder can be created on the given path.
     * <p>
     * @param folderPath The path to the folder to be created.
     *
     * @return true if a folder can be created on this path.
     */
    boolean canMkDir(Path folderPath);

    /**
     * Determines if a file can be created on the given path.
     * <p>
     * @param filePath The path to the file to be created.
     *
     * @return true if a folder can be created on this path.
     */
    boolean canCreateNewFile(Path filePath);

    /**
     * Determines when ever the givern file can be delete.
     *
     * @param data The data object representing the file.
     *
     * @return true the file can be deleted. false otherwise.
     */
    boolean canDelete(Object data);

    /**
     * Deletes the given file.
     * 
     * @param data The data object representing the file.
     *
     * @throws java.io.IOException If the file cannot be deleted.
     */
    void delete(Object data) throws IOException;

}
