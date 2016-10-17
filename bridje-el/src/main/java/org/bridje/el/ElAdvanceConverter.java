
package org.bridje.el;

/**
 * This interface defines an expression language type converter that can be use
 * in the ElEnviroment to cast the results of the evaluated expressions. To
 * provide a new converter a component that implements this interface must be
 * created.
 */
public interface ElAdvanceConverter
{
    /**
     * Determines when ever this convertor can covert the specified value to the
     * specified class.
     *
     * @param <F> The type of the value be converted.
     * @param <T> The type of the result.
     * @param from The class of the value be converted.
     * @param to The class of the result.
     * @return true if this converter can convert the given value to the given
     * class.
     */
    public <F, T> boolean canConvert(Class<F> from, Class<T> to);

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
