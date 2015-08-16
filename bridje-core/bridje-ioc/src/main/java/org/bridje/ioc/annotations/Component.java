
package org.bridje.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a class as a component so it can be managed by the API.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component
{
    /**
     * Defines de scope of component.
     * 
     * @return String that represents the scope of the component.
     */
    String scope() default "APPLICATION";

    /**
     * If the component must be instantiate eage or lazy
     * 
     * @return true the component will be instantiated eager.
     */
    boolean instantiate() default false;
}
