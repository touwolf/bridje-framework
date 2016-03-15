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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * An interface that every configuration repository must implements.
 *
 * The ConfigService will use all the config repositories components to find and
 * saves configurations.
 */
public interface ConfigRepository
{
    /**
     * Obtains if the repository handle a context
     *
     * @param context
     * @return
     */
    Boolean handleContext(String context);

    /**
     * Obtains a reader to the configuration resource by the given name.
     *
     * @param configName The name of the configuration.
     * @return A Reader from wich to read the configuracion instance.
     * @throws java.io.IOException If any IOException occurs during configuration retreval.
     */
    Reader findConfig(String configName) throws IOException;

    /**
     * Obtains a writer to the configuration resource by the given name.
     *
     * @param configName The name for this configuration instance.
     * @return The writer in wich to put the new configuration instance, 
     * or null if this repository does not allows configuration saving.
     * @throws java.io.IOException If any IOException occurs during configuration saving.
     */
    Writer saveConfig(String configName) throws IOException;

    /**
     * Especifies when ever this repository allows to save configuration
     * instances or not.
     *
     * @return true this repository allows to save configuration instances,
     * false otherwise.
     */
    boolean canSave();
}
