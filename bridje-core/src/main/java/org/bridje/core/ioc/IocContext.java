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

package org.bridje.core.ioc;

import java.lang.reflect.Type;
import java.util.Collection;
import org.bridje.core.ioc.impl.Register;

/**
 * Represents a context in witch components are managed.
 * This interfaz is mean to be use (not for implementation), 
 * it brings all the method necesaries for find components and services
 * in the scope that it manages.
 * 
 * An implementetion of this interface can be obtained via the Ioc interface
 * or by injecting {@link org.bridje.core.ioc.annotations.Inject} it in any component
 * you whant.
 * 
 * @author gilberto
 */
public interface IocContext
{
    /**
     * This method finds the highest priority component that provides the 
     * given service.
     * 
     * @param <T> The generic type of the class of the service that this method 
     * should find.
     * @param service The class that represents the service that this method
     * must find.
     * @return An object that extends or implement the class of the service 
     * provided, or null if no component provides this services in the context.
     */
    <T> T find(Class<T> service);
    
    /**
     * This method finds the highest priority component that provides the 
     * given generic service.
     * 
     * @param <T> The generic type of the class of the service that this method 
     * should return.
     * @param service The java.​lang.​reflect.Type that represents the service that this method
     * must find.
     * @param resultCls The expexted return class of this method.
     * @return An object that extends or implement the service provided, 
     * or null if no component provides this services in the context.
     */
    <T> T findGeneric(Type service, Class<T> resultCls);
    
    /**
     * This method finds all the components that provides the given service.
     * 
     * @param <T> The generic type of the class of the service that this method 
     * should find.
     * @param service The class that represents the service that this method
     * must find.
     * @return An array of objects who extends or implement the class of the service 
     * provided, or an empty array if no component provides this services in the context.
     */
    <T> T[] findAll(Class<T> service);

    /**
     * This method finds all the components that provides the given generic service.
     * 
     * @param <T> The generic type of the class of the service that this method 
     * should find.
     * @param service The class that represents the service that this method
     * must find.
     * @param resultCls The expexted return class of this method.
     * @return An array of objects who extends or implement the service provided,
     * or an empty array if no component provides this services in the context.
     */
    <T> T[] findAllGeneric(Type service, Class<T> resultCls);

    /**
     * This method finds if a service is provided by a least one component in the 
     * context.
     * 
     * @param service The type of the service to look for.
     * @return true A least one component provides this service, false otherwise.
     */
    boolean exists(Type service);
    
    /**
     * This method finds if the given class is a component of the context.
     * 
     * @param component The class of the component to look for.
     * @return true This class represents a component of the context, false otherwise.
     */
    boolean existsComponent(Class component);
    
    /**
     * The parent of this context.
     * 
     * @return The IocContext instance representing the parent of this context,
     * or null if this context has no parent.
     */
    IocContext getParent();

    /**
     * Create a child IocContext of this context.
     * 
     * @param scope The scope of the new context.
     * @param register
     * @return The new IocContext instance created as child of this context.
     */
    IocContext createChild(String scope, Register... register);
    
    /**
     * Create a child IocContext of this context.
     * 
     * @param scope The scope of the new context.
     * @param instances A collection of objects that will be components of the 
     * new context.
     * @param register
     * @return The new IocContext instance created as child of this context.
     */
    IocContext createChild(String scope, Collection instances, Register... register);
    
    /**
     * Obtains the class repository associated with this context.
     * that allows to find classes, fields and methods of the components 
     * in this context.
     * 
     * @return A ClassRepository instance 
     */
    ClassRepository getClassRepository();
}
