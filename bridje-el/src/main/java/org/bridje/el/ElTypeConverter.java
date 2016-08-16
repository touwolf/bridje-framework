
package org.bridje.el;

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
     * Determines when ever this convertor can covert the specified value to the
     * specified class.
     *
     * @param <T> The type of the result.
     * @param value The value to convert.
     * @param type The type to convert to.
     * @return true if this converter can convert the given value to the given
     * class.
     */
    public <T> boolean canConvert(Object value, Class<T> type);

    /**
     * Performs the conversion of the given value to the given type.
     *
     * @param <T> The type of the result.
     * @param value The value to convert.
     * @param type The type to convert to.
     * @return The converted value.
     */
    public <T> T convert(Object value, Class<T> type);
}
