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
import java.util.List;

/**
 * Represents a folder of the VFS tree.
 */
public interface VFolder extends VResource
{
    /**
     * Finds a folder mapped to the specified path.
     * <p>
     * @param path The string representation of the path to the folder.
     * @return A VFolder object representing the folder mapped to the path
     * argument or null if the specified path is not a valid folder in the VFS.
     * tree.
     */
    VFolder findFolder(String path);

    /**
     * Finds a folder mapped to the specified path.
     * <p>
     * @param path The path to the folder.
     * @return A VFolder object representing the folder mapped to the path
     * argument or {@literal null} if the specified path is not a valid folder
     * in the VFS tree.
     */
    VFolder findFolder(Path path);

    /**
     * Finds a file mapped to the specified path.
     * <p>
     * @param path The string representation of the path to the file.
     * @return A VFile object representing the file mapped to the path
     * argument or null if the specified path is not a valid folder in the VFS.
     * tree.
     */
    VFile findFile(String path);

    /**
     * Finds a file mapped to the specified path.
     * <p>
     * @param path The path to the file.
     * @return A VFile object representing the file mapped to the path
     * argument or null if the specified path is not a valid folder in the VFS.
     * tree.
     */
    VFile findFile(Path path);

    /**
     * Gets a list of all child folders of this folder.
     * <p>
     * @return A List of VFolders representing the child folders of this
     * folder.
     */
    List<VFolder> listFolders();

    /**
     * Gets a list of all child folders of this folder.
     * <p>
     * @param query The regular expression to match the folders full path that will
     * be listed with this method.
     * @return A List of VFolders representing the child folders of this
     * folder.
     */
    List<VFolder> listFolders(String query);

    /**
     * Gets a list of all child files of this folder.
     * <p>
     * @return A List of VFiles representing the child files of this
     * folder.
     */
    List<VFile> listFiles();

    /**
     * Gets a list of all child files of this folder.
     * <p>
     * @param query The regular expression to match the files full path that will
     * be listed with this method.
     * @return A List of VFiles representing the child files of this
     * folder.
     */
    List<VFile> listFiles(String query);

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
    <T> T readFile(String path, Class<T> resultCls) throws IOException;

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
    <T> T readFile(Path path, Class<T> resultCls) throws IOException;

    /**
     * Finds the virtual file by the given path. And writes it's content with the
     * instance of the given object.
     *
     * @param <T> The type of the result class.
     * @param path The path of the file.
     * @param contentObj The object to be serialize to the file.
     *
     * @throws java.io.IOException If any input-output error occurs writing the file.
     */
    <T> void writeFile(String path, T contentObj) throws IOException;

    /**
     * Finds the virtual file by the given path. And writes it's content with the
     * instance of the given object.
     *
     * @param <T> The type of the result class.
     * @param path The path of the file.
     * @param contentObj The object to be serialize to the file.
     * 
     * @throws java.io.IOException If any input-output error occurs writing the file.
     */
    <T> void writeFile(Path path, T contentObj) throws IOException;

    /**
     * Creates a new physical file on this folder.
     * <p>
     * @param filePath The path and name to the file to be created.
     * @return The file representation according this source.
     * @throws java.io.IOException If any I/O exception occurs.
     */
    VFile createNewFile(Path filePath) throws IOException;

    /**
     * Creates a new directory on this folder.
     * <p>
     * @param folderPath The path and name to the folder to be created.
     * @return The name of the folder created.
     * @throws java.io.IOException If any I/O exception occurs. 
     */
    VFolder mkDir(Path folderPath) throws IOException;

    /**
     * Determines if a file can be created on the given path.
     * <p>
     * @param filePath The path and name to the file to be created.
     * @return true if a file can be created on this path.
     */
    boolean canCreateNewFile(Path filePath);

    /**
     * Determines if a folder can be created on the given path.
     * <p>
     * @param folderPath The path and name to the folder to be created.
     * @return true if a folder can be created on this path.
     */
    boolean canMkDir(Path folderPath);

    /**
     * Creates and writes a new physical file on this folder, with the given content.
     * <p>
     * @param <T> The type of the result class.
     * @param filePath The path and name to the file to be created.
     * @param contentObj 
     * @return The file representation according this source.
     * @throws java.io.IOException If any I/O exception occurs.
     */
    <T> VFile createAndWriteNewFile(Path filePath, T contentObj) throws IOException;
    
    /**
     * Travels through all files recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     */
    void travel(VFileVisitor visitor);

    /**
     * Travels through all folders recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     */
    void travel(VFolderVisitor visitor);

    /**
     * Travels through all folders recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     * @param query The regular expression to match the files full path that will
     * be listed with this method.
     */
    void travel(VFileVisitor visitor, String query);

    /**
     * Travels through all folders recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     * @param query The regular expression to match the folders full path that will
     * be listed with this method.
     */
    void travel(VFolderVisitor visitor, String query);
}
