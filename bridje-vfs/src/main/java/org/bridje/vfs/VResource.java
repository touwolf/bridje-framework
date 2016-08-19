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

/**
 * Base interface for all resource type in the VFS tree.
 * <p>
 * This interface represents a file or folder in the VFS tree.
 */
public interface VirtualResource
{
    /**
     * The parent folder of this resource.
     * <p>
     * @return A VirtualFlder representing the parent folder of this resource.
     */
    VirtualFolder getParent();

    /**
     * The name of this file or folder.
     * 
     * @return An String containing the name of the file or folder.
     */
    String getName();

    /**
     * The path to reach this resource, including the resource´s name.
     * 
     * @return The path to reach this resource, including the resource´s name.
     */
    Path getPath();

    /**
     * The path to reach this resource, excluding the resource´s name.
     * 
     * @return The path to reach this resource, excluding the resource´s name.
     */
    Path getParentPath();
}
