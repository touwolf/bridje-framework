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
package org.bridje.ioc;

/**
 * A listener for the {@link IocContext}. This interfaz is mean to be
 * implemented in order for the container to call his methods upon the events
 * that take place on the {@link IocContext}
 * <p>
 * The context listener will be any component that implements the interface
 * ContextListener, the framework must search all ContextListener
 * implementations via the standard way of components resolution and call the 3
 * methods of the interface in the propper place.
 * </p>
 *
 * @param <T> Component class that you want listen.
 *            <p>
 *            <b>EXAMPLES</b>
 *            <pre>
 *            <code>&#64;Component
 *            class MyContextListener implement ContextListener&lt;MyComponent&gt;
 *            {
 *                 public void preCreateComponent(Class&lt;MyComponent&gt;; component)
 *                 {
 *                     //only is call when MyComponent is pre create
 *                 }
 *
 *                 public void preInitComponent(Class&lt;MyComponent&gt; component)
 *                 {
 *                     //only is call when MyComponent is pre init
 *                 }
 *
 *                 public void postInitComponent(Class&lt;MyComponent&gt; component)
 *                 {
 *                     //only is call when MyComponent is post init
 *                 }
 *            }</code>
 *            </pre>
 *            <p>
 *            If you want to listen to all components use Object
 *            <p>
 *            <pre>
 *            <code>&#64;Component
 *            class MyContextListener implement ContextListener&lt;Object&gt;
 *            {
 *                 public void preCreateComponent(Class&lt;Object&gt; component)
 *                 {
 *                     //is call for each component pre create
 *                 }
 *
 *                 public void preInitComponent(Class&lt;Object&gt; component)
 *                 {
 *                     //is call for each component pre init
 *                 }
 *
 *                 public void postInitComponent(Class&lt;Object&gt; component)
 *                 {
 *                     //is call for each component post init
 *                 }
 *            }</code>
 *            </pre>
 */
public interface ContextListener<T>
{
    /**
     * Will be called before a component is created
     *
     * @param clazz The component class
     */
    void preCreateComponent(Class<T> clazz);

    /**
     * Will be called after the component has been instantiate and before
     * injecting the components dependencies
     *
     * @param clazz    The component class
     * @param instance The component instance
     */
    void preInitComponent(Class<T> clazz, T instance);

    /**
     * Will be called after the components dependencies injection.
     *
     * @param clazz    The component class
     * @param instance The component instance
     */
    void postInitComponent(Class<T> clazz, T instance);
}
