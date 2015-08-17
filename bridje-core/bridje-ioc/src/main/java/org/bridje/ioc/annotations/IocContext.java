
package org.bridje.ioc.annotations;

/**
 * Represents a context in witch components are managed.
 * This interfaz is mean to be use (not for implementation), 
 * it brings all the method necesaries for find components and services
 * in the scope that it manages.
 */
public interface IocContext
{
    <T> T find(Class<T> service);

    boolean existsComponent(Class component);
}
