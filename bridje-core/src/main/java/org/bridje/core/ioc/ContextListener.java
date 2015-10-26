/*
 * Copyright 2015 Bridje Framework.
 *
 * Alejandro Ferrandiz (acksecurity[at]hotmail.com)
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

package org.bridje.core.ioc;

/**
 * A listener for the {@link IocContext}. This interfaz is mean to be
 * implemented in order for the container to be call his method upon the evens
 * that take place on the {@link IocContext}
 * <p>
 * The context listener will be any component that implements the interface
 * ContextListener, the framework must search all ContextListener implementations
 * via the standard way of components resolution and call the 3 methods of the
 * interface in the propper place.
 * <p>
 */
public interface ContextListener
{
    /**
     * Will be called before a component is created
     */
    void preCreateComponent();
    
    /**
     * Will be called after the component has been instantiate and before
     * injecting the components dependencies
     */
    void preInitComponent();
    
    /**
     * Will be called after the components dependencies injection.
     */
    void postInitComponent();
}
