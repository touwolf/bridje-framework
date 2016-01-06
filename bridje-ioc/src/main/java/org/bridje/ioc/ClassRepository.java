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

import java.lang.annotation.Annotation;

/**
 * Represents the collection of classes being used by {@link IocContext} to create
 * and manage components.
 */
public interface ClassRepository
{
    /**
     * Navigates through all methods of all component classes registred in this
     * repository, that are annotated with the given annotation. This includes
     * superclasses methods.
     * <p>
     * @param <A>        The type of the annotation methods must be annotated
     *                   with.
     * @param annotation The class of the annotation methods must be annotated
     *                   with.
     * @param navigator  Callback functional interface that handles the
     *                   annotated method.
     */
    public <A extends Annotation> void navigateAnnotMethods(Class<A> annotation, AnnotMethodNavigator<A> navigator);

    /**
     * Navigates through all fields of all component classes registred in this
     * repository, that are annotated with the given annotation. This includes
     * superclasses fields.
     * <p>
     * @param <A>        The type of the annotation fields must be annotated
     *                   with.
     * @param annotation The class of the annotation fields must be annotated
     *                   with.
     * @param navigator  Callback functional interface that handles the
     *                   annotated field.
     */
    public <A extends Annotation> void navigateAnnotFileds(Class<A> annotation, AnnotFieldNavigator<A> navigator);

    /**
     * Navigates through all component classes registred in this repository,
     * that are annotated with the given annotation. This includes superclasses
     * fields.
     * <p>
     * @param <A>        The type of the annotation classes must be annotated
     *                   with.
     * @param annotation The class of the annotation classes must be annotated
     *                   with.
     * @param navigator  Callback functional interface that handles the
     *                   annotated class.
     */
    public <A extends Annotation> void navigateAnnotClasses(Class<A> annotation, AnnotClassNavigator<A> navigator);
}
