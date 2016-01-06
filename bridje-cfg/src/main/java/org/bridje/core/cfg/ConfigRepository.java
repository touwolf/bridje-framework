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

package org.bridje.core.cfg;

/**
 * An interface that every configuration repository must implements.
 *
 * The ConfigService will use all the config repositories components to find and
 * saves configurations.
 */
public interface ConfigRepository
{
    /**
     * Finds the default configuration object by the given class.
     *
     * @param <T> The type of the configuration class.
     * @param configClass The class of the configuration object to be obtained.
     * @return An instance of configClass class witch represents the default
     * configuration object of that class.
     */
    <T> T findConfig(Class<T> configClass);

    /**
     * Finds the configuration object named by the configName parameter that
     * instantiates the especified class.
     *
     * @param <T> The type of the configuration to be obtained.
     * @param configName The name of the configuration.
     * @param configClass The class of the configuration.
     * @return An instance of configClass class witch represents the
     * configuration object named by the configName parameter.
     */
    <T> T findConfig(String configName, Class<T> configClass);

    /**
     * Saves the default configuration instance to the repository witch may be
     * obtained by the findConig method with the class of the saved object.
     *
     * @param <T> The type of the configuration object.
     * @param newConfig The configuration object to be save as the default
     * instance of the object´s class config.
     * @return The newConfig object passed to this method.
     */
    <T> T saveConfig(T newConfig);

    /**
     * Saves the named configuration instance to the repository witch may be
     * obtained by the findConig method with the name and class of the saved
     * object.
     *
     * @param <T> The type of the configuration object.
     * @param configName The name for this configuration instance.
     * @param newConfig The configuration object to be save as the named
     * instance of the object´s class config.
     * @return The newConfig object passed to this method.
     */
    <T> T saveConfig(String configName, T newConfig);

    /**
     * Especifies when ever this repository allows to save configuration
     * instances or not.
     *
     * @return true this repository allows to save configuration instances,
     * false otherwise.
     */
    boolean canSave();
}
