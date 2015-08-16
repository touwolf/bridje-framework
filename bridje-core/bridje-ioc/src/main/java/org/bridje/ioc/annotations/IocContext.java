
package org.bridje.ioc.annotations;

public interface IocContext
{
    <T> T find(Class<T> cls);
}
