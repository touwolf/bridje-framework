
package org.bridje.web.el;

/**
 * This interface defines an expression language type converter that can be use
 * in the ElEnviroment to cast the results of the evaluated expressions. To
 * provide a new converter a component that implements this interface must be
 * created.
 */
public interface ElTypeConverter
{
    /**
     * The list of classes that this converter can convert from.
     * 
     * @return The list of classes that this converter can convert from.
     */
    public Class[] getFromClasses();

    /**
     * The list of classes that this converter can convert to.
     * 
     * @return The list of classes that this converter can convert to.
     */
    public Class[] getToClasses();

    /**
     * Performs the convertion of the given value to the given type.
     * 
     * @param <T> The type of the result.
     * @param value The value to convert.
     * @param type The type to convert to.
     * @return The converted value.
     */
    public <T> T convert(Object value, Class<T> type);
}
