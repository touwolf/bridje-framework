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

package org.bridje.core.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Represents a file source for the VFS API.
 *
 * All VfsProvider implementation must provides an object that implements this
 * interface in order to put files in the vfs tree.
 */
public interface VfsSource
{
    /**
     * Gets all objects identifiying the files in the especified path, if the
     * file represented by the path argument is a single file withing the
     * VfsSource this method will return a single file object array, but if
     * multiple files withing the VfsSource are map to the especified path then
     * this method will return all objects files for the path.
     *
     * @param path The path to look for.
     * @return An array of object, each acts as identifier for a file maped to
     * the path argument.
     * @throws IOException This method may access to phisical device so this
     * exception may be throw if any input output operation fails.
     */
    Object[] getFiles(Path path) throws IOException;

    /**
     * This method retrive all names for the child folders withing the
     * especified path.
     *
     * @param path The path to look for children folders.
     * @return A list of string with the names of the child folders for the
     * especified path.
     * @throws IOException This method may access to phisical device so this
     * exception may be throw if any input output operation fails.
     */
    List<String> listFolders(Path path) throws IOException;

    /**
     * This method retrive all names for the child files withing the especified
     * path.
     *
     * @param path The path to look for children files.
     * @return A list of string with the names of the child file for the
     * especified path.
     * @throws IOException This method may access to phisical device so this
     * exception may be throw if any input output operation fails.
     */
    List<String> listFiles(Path path) throws IOException;

    /**
     * Determines if the especified path is a file from this VfsSource.
     *
     * @param path The path of the file.
     * @return true if a file mapped to the path argument exists withing this
     * VfsSource.
     * @throws IOException This method may access to phisical device so this
     * exception may be throw if any input output operation fails.
     */
    boolean fileExists(Path path) throws IOException;

    /**
     * Determines if the especified path is a folder from this VfsSource.
     *
     * @param path The path of the file.
     * @return true if a folder mapped to the path argument exists withing this
     * VfsSource.
     * @throws IOException This method may access to phisical device so this
     * exception may be throw if any input output operation fails.
     */
    boolean folderExists(Path path) throws IOException;

    /**
     * Opens a file's InputStream for reading it's content.
     *
     * @param data The file identifier retrive from this VfsSource by the
     * getFiles method.
     * @return An InputStream to the file.
     * @throws IOException This method may access to phisical device so this
     * exception may be throw if any input output operation fails.
     */
    InputStream open(Object data) throws IOException;
}
