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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This interface represents the virtual file system for Bridje, it provides
 * methods to mount and file files and folder into the system.
 */
public interface VfsService
{
    /**
     * Finds the virtual folder by the given path.
     *
     * @param path the path of the folder.
     * @return The folder founded or {@literal null} if it does not exists.
     */
    VirtualFolder findFolder(String path);

    /**
     * Finds the virtual folder by the given path.
     *
     * @param path the path of the folder.
     * @return The folder founded or null if it does not exists.
     */
    VirtualFolder findFolder(Path path);

    /**
     * Finds the virtual file by the given path.
     *
     * @param path the path of the file.
     * @return The file founded or null if it does not exists.
     */
    VirtualFile findFile(String path);

    /**
     * Finds the virtual file by the given path.
     *
     * @param path the path of the file.
     * @return The file founded or null if it does not exists.
     */
    VirtualFile findFile(Path path);

    /**
     * Finds the virtual file by the given path. And reads it content to a new
     * instance of the result class.
     *
     * @param <T> The type of the result class.
     * @param path the path of the file.
     * @param resultCls The result class that the files need to be parsed to.
     *
     * @return The file founded or null if it does not exists.
     * @throws java.io.IOException If any input-output error occurs reading the file.
     */
    <T> T findFile(String path, Class<T> resultCls) throws IOException;

    /**
     * Finds the virtual file by the given path. And reads it content to a new
     * instance of the result class.
     *
     * @param <T> The type of the result class.
     * @param path the path of the file.
     * @param resultCls The result class that the files need to be parsed to.
     * @return The file founded or null if it does not exists.
     * @throws java.io.IOException If any input-output error occurs reading the file.
     */
    <T> T findFile(Path path, Class<T> resultCls) throws IOException;

    /**
     * List all the folders children to this folder.
     *
     * @return The children folders of this folder.
     */
    List<VirtualFolder> listFolders();

    /**
     * List all the files children to this folder.
     *
     * @return The children files of this folder.
     */
    List<VirtualFile> listFiles();

    /**
     * List all the folders children to this folder.
     *
     * @param query The regular expression to filter the folders names.
     * @return The children folders of this folder.
     */
    List<VirtualFolder> listFolders(String query);

    /**
     * List all the files children to this folder.
     *
     * @param query The regular expression to filter the files names.
     * @return The children files of this folder.
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
     * Mounts a new source into the given path.
     *
     * @param path The path to mount the source.
     * @param source The virtual file system source object to be mounted.
     */
    void mount(String path, VfsSource source);

    /**
     * Mounts a new class path resource vfs source into the given path.
     *
     * @param path The path to mount the source.
     * @param resource The class path resource folder to be mounted.
     * @throws java.io.IOException If any input-output exception occurs.
     * @throws java.net.URISyntaxException If an invalid resource is provided.
     */
    void mountResource(Path path, String resource) throws IOException, URISyntaxException;

    /**
     * Mounts a new class path resource vfs source into the given path.
     *
     * @param path The path to mount the source.
     * @param resource The class path resource folder to be mounted.
     * @throws java.io.IOException If any input-output exception occurs.
     * @throws java.net.URISyntaxException If an invalid resource is provided.
     */
    void mountResource(String path, String resource) throws IOException, URISyntaxException;

    /**
     * Mounts a new file vfs source into the given path.
     *
     * @param path The path to mount the source.
     * @param file The folder to be mounted.
     */
    void mountFile(Path path, File file);

    /**
     * Mounts a new file vfs source into the given path.
     *
     * @param path The path to mount the source.
     * @param file The folder to be mounted.
     */
    void mountFile(String path, File file);

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
     * @param query The regular expression to math the files full path.
     */
    void travel(VirtualFileVisitor visitor, String query);

    /**
     * Navigates the vfs tree from this folder, matching all the child folders
     * recursively.
     *
     * @param visitor The visitor to accept the folders.
     * @param query The regular expression to math the folders full path.
     */
    void travel(VirtualFolderVisitor visitor, String query);
}
