package org.bridje.cfg;

import java.io.IOException;

/**
 * This interface define a context for the repositories
 */
public interface ConfigContext
{
    /**
     * Creates a new nested configuration context base on this object.
     *
     * @param context The context path.
     * @return The new created configuration context.
     */
    ConfigContext createContext(String context);

    /**
     * Obtains a configuration by its class.
     * <p>
     * @param <T> The type of the configuration.
     * @param configClass The class of the configuration instance.
     * @return The object with the configuration or {@literal null} if no
     * configuration of the specified type was found.
     * @throws java.io.IOException If any IOException occurs during configuration retreval.
     */
    <T> T findConfig(Class<T> configClass) throws IOException;

    /**
     * Obtains a configuration by its class.
     * <p>
     * @param <T> The type of the configuration.
     * @param configName The name of the representative configuration file.
     * @param configClass The class of the configuration instance.
     * @return The object with the configuration or {@literal null} if no
     * configuration of the specified type was found.
     * @throws java.io.IOException If any IOException occurs during configuration retreval.
     */
    <T> T findConfig(String configName, Class<T> configClass) throws IOException;

    /**
     * Obtains a configuration by its class, or create it if no found.
     * <p>
     * @param <T> The type of the configuration.
     * @param configClass The class of the configuration instance.
     * @param defaultConfig The default configuration if not exists.
     * @return The object with the configuration or {@literal null} if no
     * configuration of the specified type was found.
     * @throws java.io.IOException If any IOException occurs during configuration saving or retreval.
     */
    <T> T findOrCreateConfig(Class<T> configClass, T defaultConfig) throws IOException;

    /**
     * Obtains a configuration by its class, or create it if no found.
     * <p>
     * @param <T> The type of the configuration.
     * @param configName The name of the representative configuration file.
     * @param configClass The class of the configuration instance.
     * @param defaultConfig The default configuration if not exists.
     * @return The object with the configuration or {@literal null} if no
     * configuration of the specified type was found.
     * @throws java.io.IOException If any IOException occurs during configuration saving or retreval.
     */
    <T> T findOrCreateConfig(String configName, Class<T> configClass, T defaultConfig) throws IOException;

    /**
     * Saves the default configuration instance to the highest priority
     * repository with save capability so it may be obtained by the findConig
     * method with the class of the saved object.
     *
     * @param <T> The type of the configuration object.
     * @param newConfig The configuration object to be save as the default
     * instance of the object´s class config.
     * @return The newConfig object passed to this method.
     * @throws java.io.IOException If any IOException occurs during configuration saving.
     */
    <T> T saveConfig(T newConfig) throws IOException;

    /**
     * Saves the named configuration instance to the the highest priority
     * repository with save capability so it may be obtained by the findConig
     * method with the name and class of the saved object.
     *
     * @param <T> The type of the configuration object.
     * @param configName The name for this configuration instance.
     * @param newConfig The configuration object to be save as the named
     * instance of the object´s class config.
     * @return The newConfig object passed to this method.
     * @throws java.io.IOException If any IOException occurs during configuration saving.
     */
    <T> T saveConfig(String configName, T newConfig) throws IOException;
}
