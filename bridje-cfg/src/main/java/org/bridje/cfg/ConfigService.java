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

package org.bridje.cfg;

import java.io.File;

/**
 * A service for access the configurations of applications in multiple
 * environments.
 * <p>
 * Configuration classes must be serializable in xml format.
 */
public interface ConfigService extends ConfigContext
{
    /**
     * This method allows you to add a new configuration repository to the
     * internal list of repositories so the services can use it to find
     * configurations files on it. This repository will have precedence to the
     * previous added repositories.
     *
     * @param repo The configuration repository to add.
     */
    void addRepository(ConfigRepository repo);

    /**
     * This method allows you to create a new configuration repository base on a
     * file system folder.
     *
     * @param file The file system folder base for the new repository.
     * @return The new created ConfigRepository.
     */
    ConfigRepository createFileRepository(File file);

    /**
     * This method allows you to create a new configuration repository base on a
     * class path string and class.
     * 
     * @param cls The class for finding the class paths resources.
     * @param path The base path for the repository.
     * @return The new created ConfigRepository.
     */
    ConfigRepository createClassPathRepository(Class<?> cls, String path);
}
