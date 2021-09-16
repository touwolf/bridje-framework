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

package org.bridje.web.view;

import java.util.Set;
import org.bridje.web.view.controls.UIEvent;
import org.bridje.web.view.controls.UIFileExpression;
import org.bridje.web.view.controls.UIInputExpression;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are immutable so once defined they will
 * stay the same at runtime.
 */
public interface WebView extends AbstractView
{
    /**
     * The title for this view.
     *
     * @return The title for this view.
     */
    public String getTitle();

    /**
     * The name of this view.
     *
     * @return The name of this view.
     */
    String getName();

    /**
     * Finds the set of resources used in this view by all the controls defined
     * in it.
     *
     * @return A set with all the names of the resources.
     */
    Set<String> getResources();

    /**
     * Gets the set of controls classes used in this view.
     *
     * @return All the controls classes used in this view.
     */
    Set<Class<?>> getControls();

    /**
     * Determines if this view has any file input field.
     *
     * @return true this view has any file input field, false this view has none.
     */
    boolean hasFileInput();

    /**
     * Finds the file upload input expression that match the given string.
     *
     * @param exp The expression.
     * @return The UIInputExpression object that match with the given String if
     * any.
     */
    UIFileExpression findFileInput(String exp);

    /**
     * Finds the input expression that match the given string.
     *
     * @param exp The expression.
     * @return The UIInputExpression object that match with the given String if
     * any.
     */
    UIInputExpression findInput(String exp);

    /**
     * Finds the event that match with the given action.
     *
     * @param action The name of the action.
     * @return The UIEvent object that match the given expression.
     */
    UIEvent findEvent(String action);
}
