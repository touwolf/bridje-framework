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

package org.bridje.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a class as a component so it can be managed by the API.
 * <p>
 * This annotation mark any class as an IoC component. if scope of the component
 * is no specified it will be an application scoped component.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component
{
    /**
     * Defines the scope of component.
     * <p>
     * @return String that represents the scope of the component.
     */
    String scope() default "APPLICATION";

    /**
     * If the component must be instantiate eager or lazy
     * <p>
     * @return true the component will be instantiated eager, the default is
     *         false witch means that the component will be instantiated lazy
     *         the first time some one request it.
     */
    boolean instantiate() default false;
}
