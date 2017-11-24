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

import java.util.*;
import org.bridje.web.view.controls.UIEvent;
import org.bridje.web.view.controls.UIInputExpression;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
public interface WebView extends AbstractView
{
    /**
     * Gets a list of meta information tags information to be rendered with this
     * view.
     *
     * @return A list of meta information tags assigned to this view.
     */
    List<MetaTag> getMetaTags();

    /**
     * Adds the given meta tags for this view.
     * 
     * @param metas The list of meta tags to be added.
     */
    void updateMetaTags(List<MetaTag> metas);

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
    public String getName();

    /**
     * Sets the name of this view. for internal use of this API only.
     *
     * @param name The name to be set.
     */
    void setName(String name);

    /**
     * Finds the set of resources used in this view by all the controls defined 
     * in it.
     *
     * @return A set with all the names of the resources.
     */
    public Set<String> getResources();

    /**
     * Gets the set of controls classes used in this view.
     *
     * @return All the controls classes used in this view.
     */
    public Set<Class<?>> getControls();

    /**
     * Finds the input expression that match the given string.
     *
     * @param exp The expression.
     * @return The UIInputExpression object that match whit the given String if
     * any.
     */
    public UIInputExpression findInput(String exp);

    /**
     * Finds the event that match with the given action.
     *
     * @param action The name of the action.
     * @return The UIEvent object that match the given expression.
     */
    public UIEvent findEvent(String action);
}
