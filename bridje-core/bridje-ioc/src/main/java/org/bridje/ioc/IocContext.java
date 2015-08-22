
package org.bridje.ioc;

import java.lang.reflect.Type;
/**
 * Represents a context in witch components are managed.
 * This interfaz is mean to be use (not for implementation), 
 * it brings all the method necesaries for find components and services
 * in the scope that it manages.
 * 
 * An implementetion of this interface can be obtained via the Ioc interface
 * or by injecting {@see org.bridje.ioc.annotations.Inject} it in any component
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

    <T> T[] findAllGeneric(Type service, Class<T> resultCls);

    /**
     * This method finds if a service is provided by a least one component in the 
     * context.
     * 
     * @param service The class of the service to look for.
     * @return true A least one component provides this service, false otherwise.
     */
    boolean exists(Class service);
    
    /**
     * This method finds if the given class is a component of the context.
     * 
     * @param component The class of the component to look for.
     * @return true This class represents a component of the context, false otherwise.
     */
    boolean existsComponent(Class component);
}
