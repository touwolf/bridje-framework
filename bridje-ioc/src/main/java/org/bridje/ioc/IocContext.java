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

package org.bridje.ioc;

import java.lang.reflect.Type;

/**
 * Represents a context in witch components are managed. This interface is mean
 * to be use (not for implementation), it brings all the method necessaries for
 * find components and services in the scope that it manages.
 * <p>
 * An implementation of this interface can be obtained via the Ioc interface or
 * by injecting {@link org.bridje.ioc.Inject} it in any component
 * you want.
 * @param <S>
 */
public interface IocContext<S extends Scope>
{
    /**
     * Gets the scope of the current context.
     *
     * @return An string object representing the scope name used for this
     * context.
     */
    public S getScope();

    /**
     * Gets the scope of the current context.
     *
     * @return An string object representing the scope name used for this
     * context.
     */
    Class<S> getScopeClass();

    /**
     * This method finds the highest priority component that provides the given
     * service.
     * <p>
     * @param <T> The generic type of the class of the service that this method
     * should find.
     * @param service The class that represents the service that this method
     * must find.
     * <p>
     * @return An object that extends or implement the class of the service
     * provided, or null if no component provides this services in the context.
     */
    <T> T find(Class<T> service);

    /**
     * This method finds the component that provides the given service with less
     * priority than the priority parameter.
     * <p>
     * @param <T> The generic type of the class of the service that this method
     * should find.
     * @param service The class that represents the service that this method
     * must find.
     * <p>
     * @param priority The given component must have a priority value greater
     * than this parameter.
     * <p>
     * @return An object that extends or implement the class of the service
     * provided, or null if no component provides this services in the context.
     */
    <T> T findNext(Class<T> service, int priority);

    /**
     * This method finds all the components that provides the given service.
     * <p>
     * @param <T> The generic type of the class of the service that this method
     * should find.
     * @param service The class that represents the service that this method
     * must find.
     * <p>
     * @return An array of objects who extends or implement the class of the
     * service provided, or an empty array if no component provides this
     * services in the context.
     */
    <T> T[] findAll(Class<T> service);

    /**
     * This method finds the highest priority component that provides the given
     * generic service.
     * <p>
     * @param service The {@link java.lang.reflect.Type} that represents the
     * service that this method must find.
     * <p>
     * @return An object that extends or implement the service provided, or null
     * if no component provides this services in the context.
     */
    Object findGeneric(Type service);

    /**
     * This method finds the component that provides the given generic service
     * with less priority than the priority parameter.
     * <p>
     * @param service The {@link java.lang.reflect.Type} that represents the
     * service that this method must find.
     * <p>
     * @param priority The given component must have a priority value greater
     * than this parameter.
     * <p>
     * @return An object that extends or implement the service provided, or null
     * if no component provides this services in the context.
     */
    Object findNextGeneric(Type service, int priority);
    
    /**
     * This method finds if a service is provided by a least one component in
     * the context.
     * <p>
     * @param service The type of the service to look for.
     * <p>
     * @return {@literal true} If at least one component provides this service,
     * {@literal false} otherwise.
     */
    boolean exists(Type service);

    /**
     * This method finds if the given class is a component of the context.
     * <p>
     * @param component The class of the component to look for.
     * <p>
     * @return {@literal true} If this class represents a component of the
     * context, {@literal false} otherwise.
     */
    boolean existsComponent(Class component);

    /**
     * The parent of this context.
     * <p>
     * @return The IocContext instance representing the parent of this context,
     * or null if this context has no parent.
     */
    IocContext<?> getParent();

    /**
     * Create a child IocContext of this context.
     * <p>
     * @param <T>
     * @param scope The scope of the new context.
     * @return The new IocContext instance created as child of this context.
     */
    <T extends Scope> IocContext<T> createChild(T scope);

    /**
     * Obtains the class repository associated with this context. that allows to
     * find classes, fields and methods of the components in this context.
     * <p>
     * @return A ClassRepository instance
     */
    ClassRepository getClassRepository();
}
