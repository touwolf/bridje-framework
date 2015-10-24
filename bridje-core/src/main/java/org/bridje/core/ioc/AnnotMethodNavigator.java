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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Functional interface used by {@link ClassRepository} for navigating annotated 
 * methods in the inversion of control context.
 * <p>
 * @param <A> The annotation that the methods must have in orther to be accept by
 *            the navigator.
 */
@FunctionalInterface
public interface AnnotMethodNavigator<A extends Annotation>
{
    /**
     * This method is call when ever a method in a component if found to have the
     * given annotation.
     * <p>
     * @param method     The method been found.
     * @param component  The class of the component that contains the found
     *                   method.
     * @param annotation The instance of the annotation of the method.
     */
    void accept(Method method, Class component, A annotation);
}
