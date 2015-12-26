
package org.bridje.core.el;

public interface ElTypeConverter
{
    public Class[] getFromClasses();

    public Class[] getToClasses();

    public <T> T convert(Object value, Class<T> type);
}
