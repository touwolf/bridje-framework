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

import java.util.List;

/**
 * This interface represents the virtual file system for bridje, it provides
 * methods to mount and file files and folder into the system.
 */
public interface VfsService
{
    /**
     * Finds the virtual folder by the given path.
     *
     * @param path the path of the folder.
     * @return The folder finded or null if it does not exists.
     */
    VirtualFolder findFolder(String path);

    /**
     * Finds the virtual folder by the given path.
     *
     * @param path the path of the folder.
     * @return The folder finded or null if it does not exists.
     */
    VirtualFolder findFolder(Path path);

    /**
     * Finds the virtual file by the given path.
     *
     * @param path the path of the file.
     * @return The file finded or null if it does not exists.
     */
    VirtualFile findFile(String path);

    /**
     * Finds the virtual file by the given path.
     *
     * @param path the path of the file.
     * @return The file finded or null if it does not exists.
     */
    VirtualFile findFile(Path path);

    /**
     * List all the folders children to this folder.
     *
     * @return The childs folders of this folder.
     */
    List<VirtualFolder> listFolders();

    /**
     * List all the files children to this folder.
     *
     * @return The childs files of this folder.
     */
    List<VirtualFile> listFiles();

    /**
     * List all the folders children to this folder.
     *
     * @param query The regular expression to filter the folders names.
     * @return The childs folders of this folder.
     */
    List<VirtualFolder> listFolders(String query);

    /**
     * List all the files children to this folder.
     *
     * @param query The regular expression to filter the files names.
     * @return The childs files of this folder.
     */
    List<VirtualFile> listFiles(String query);

    /**
     * Mounts a new source into the given path.
     *
     * @param path The path to mount the source.
     * @param source The virtual file system source object to be mounted.
     */
    void mount(Path path, VfsSource source);

    /**
     * Navigates the vfs tree from this folder, matching all the child folders
     * recursively.
     *
     * @param visitor The visitor to accept the files.
     */
    void travel(VirtualFileVisitor visitor);

    /**
     * Navigates the vfs tree from this folder, matching all the child folders
     * recursively.
     *
     * @param visitor The visitor to accept the folders.
     */
    void travel(VirtualFolderVisitor visitor);

    /**
     * Navigates the vfs tree from this folder, matching all the child folders
     * recursively.
     *
     * @param visitor The visitor to accept the files.
     * @param query The regular expresion to math the files full path.
     */
    void travel(VirtualFileVisitor visitor, String query);

    /**
     * Navigates the vfs tree from this folder, matching all the child folders
     * recursively.
     *
     * @param visitor The visitor to accept the folders.
     * @param query The regular expresion to math the folders full path.
     */
    void travel(VirtualFolderVisitor visitor, String query);
}