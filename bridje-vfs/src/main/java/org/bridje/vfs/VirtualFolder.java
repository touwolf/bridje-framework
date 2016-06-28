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
public interface VirtualFolder extends VirtualResource
{
    /**
     * Finds a folder mapped to the specified path.
     * <p>
     * @param path The string representation of the path to the folder.
     * @return A VirtualFolder object representing the folder mapped to the path
     * argument or null if the specified path is not a valid folder in the vfs
     * tree.
     */
    VirtualFolder findFolder(String path);

    /**
     * Finds a folder mapped to the specified path.
     * <p>
     * @param path The path to the folder.
     * @return A VirtualFolder object representing the folder mapped to the path
     * argument or {@literal null} if the specified path is not a valid folder
     * in the vfs tree.
     */
    VirtualFolder findFolder(Path path);

    /**
     * Finds a file mapped to the specified path.
     * <p>
     * @param path The string representation of the path to the file.
     * @return A VirtualFile object representing the file mapped to the path
     * argument or null if the specified path is not a valid folder in the vfs
     * tree.
     */
    VirtualFile findFile(String path);

    /**
     * Finds a file mapped to the specified path.
     * <p>
     * @param path The path to the file.
     * @return A VirtualFile object representing the file mapped to the path
     * argument or null if the specified path is not a valid folder in the vfs
     * tree.
     */
    VirtualFile findFile(Path path);

    /**
     * Gets a list of all child folders of this folder.
     * <p>
     * @return A List of VirtualFolders representing the child folders of this
     * folder.
     */
    List<VirtualFolder> listFolders();

    /**
     * Gets a list of all child folders of this folder.
     * <p>
     * @param query The regular expresion to match the folders full path that will
     * be listed with this method.
     * @return A List of VirtualFolders representing the child folders of this
     * folder.
     */
    List<VirtualFolder> listFolders(String query);

    /**
     * Gets a list of all child files of this folder.
     * <p>
     * @return A List of VirtualFiles representing the child files of this
     * folder.
     */
    List<VirtualFile> listFiles();

    /**
     * Gets a list of all child files of this folder.
     * <p>
     * @param query The regular expresion to match the files full path that will
     * be listed with this method.
     * @return A List of VirtualFiles representing the child files of this
     * folder.
     */
    List<VirtualFile> listFiles(String query);
    
    /**
     * 
     * @param fileName
     * @return
     * @throws IOException 
     */
    VirtualFile createPhysicalFile(String fileName) throws IOException;

    /**
     * 
     * @param folderName
     * @return
     * @throws IOException 
     */
    VirtualFolder createPhysicalFolder(String folderName) throws IOException;

    /**
     * 
     * @param fileName
     * @return 
     */
    boolean canCreatePhysicalFile(String fileName);

    /**
     * 
     * @param folderName
     * @return 
     */
    boolean canCreatePhysicalFolder(String folderName);

    /**
     * Travels through all files recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     */
    void travel(VirtualFileVisitor visitor);

    /**
     * Travels through all folders recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     */
    void travel(VirtualFolderVisitor visitor);

    /**
     * Travels through all folders recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     * @param query The regular expresion to match the files full path that will
     * be listed with this method.
     */
    void travel(VirtualFileVisitor visitor, String query);

    /**
     * Travels through all folders recursively from this folder and its children
     * folders.
     * <p>
     * @param visitor The visitor to be used.
     * @param query The regular expresion to match the folders full path that will
     * be listed with this method.
     */
    void travel(VirtualFolderVisitor visitor, String query);
}
