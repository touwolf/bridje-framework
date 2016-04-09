/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.jfx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows to add a MenuItem on the specified
 * {@link WorkspacePanel} or the {@link Workspace} that will call the annotated
 * method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MenuAction
{
    /**
     * The title of the MenuItem button.
     *
     * @return An String representing the title that the MenuItem will have.
     */
    String title();

    /**
     * The path to the menu item from the root of the menu bar. The workspace
     * will create all missing menus in the path.
     *
     * @return The menu path with a "/" separator.
     */
    String path();

    /**
     * This attribute will allow you to add an icon to the MenuItem object. The
     * icon name must be a valid java classpath resource accesible from the
     * current class.
     *
     * @return The icon java classpath resource path accesible fom the current
     * class.
     */
    String icon() default "";

    /**
     * The class that this action will be attached to, if this attribute is
     * Object.class the action will be put in the {@link Workspace} if is a
     * generic component or in the current {@link WorkspacePanel}.
     *
     * @return The class of the component that this action will be attached to.
     */
    Class<?> on() default Object.class;
}
