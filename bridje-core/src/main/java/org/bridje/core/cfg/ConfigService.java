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
 * A service for access the configurations of applications in multiple environments.
 * <p>
 * Configuration classes must be serializable in xml format.
 */
public interface ConfigService
{
    /**
     * Obtains a configuration by its class.
     * <p>
     * @param <T> The type of the configuration.
     * @param configClass The class of the configuration instance.
     * @return The object with the configuration or {@literal null} if no
     *         configuration of the specified type was found.
     */
    <T> T findConfig(Class<T> configClass);

    /**
     * Obtains a configuration by its class.
     * <p>
     * @param <T> The type of the configuration.
     * @param configName The name of the representative configuration file.
     * @param configClass The class of the configuration instance.
     * @return The object with the configuration or {@literal null} if no
     *         configuration of the specified type was found.
     */
    <T> T findConfig(String configName, Class<T> configClass);

    /**
     * Obtains a configuration by its class, or create it if no found.
     * <p>
     * @param <T> The type of the configuration.
     * @param configClass The class of the configuration instance.
     * @param defaultConfig The default configuration if not exists.
     * @return The object with the configuration or {@literal null} if no
     *         configuration of the specified type was found.
     */
    <T> T findOrCreateConfig(Class<T> configClass, T defaultConfig);

    /**
     * Obtains a configuration by its class, or create it if no found.
     * <p>
     * @param <T> The type of the configuration.
     * @param configName The name of the representative configuration file.
     * @param configClass The class of the configuration instance.
     * @param defaultConfig The default configuration if not exists.
     * @return The object with the configuration or {@literal null} if no
     *         configuration of the specified type was found.
     */
    <T> T findOrCreateConfig(String configName, Class<T> configClass, T defaultConfig);
}
