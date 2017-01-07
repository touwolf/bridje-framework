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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.*;
import org.bridje.web.view.controls.UIEvent;
import org.bridje.web.view.controls.UIInputExpression;
import org.bridje.web.view.controls.Control;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@XmlRootElement(name = "view")
@XmlAccessorType(XmlAccessType.FIELD)
public final class WebView extends AbstractWebView
{
    private static final Logger LOG = Logger.getLogger(WebView.class.getName());

    @XmlTransient
    private String name;

    @XmlAttribute(name = "title")
    private String title;

    @XmlElements(
        @XmlElement(name = "meta", type = MetaTag.class)
    )
    private List<MetaTag> metaTags;

    @XmlTransient
    private Map<String, UIEvent> events;

    @XmlTransient
    private Map<String, UIInputExpression> inputs;

    @XmlTransient
    private Set<Class<?>> controls;

    @XmlTransient
    private Set<String> resources;

    /**
     * Gets a list of meta information tags information to be rendered with this
     * view.
     *
     * @return A list of meta information tags assigned to this view.
     */
    public List<MetaTag> getMetaTags()
    {
        return metaTags;
    }

    /**
     * Adds the given meta tags for this view.
     * 
     * @param metas The list of meta tags to be added.
     */
    protected void updateMetaTags(List<MetaTag> metas)
    {
        if (metas == null || metas.isEmpty())
        {
            return;
        }
        if (metaTags == null)
        {
            metaTags = new ArrayList<>();
        }
        metas.stream()
                .filter((meta) -> !metaTags.contains(meta))
                .forEachOrdered(metaTags::add);
    }

    /**
     * The title for this view.
     *
     * @return The title for this view.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * The name of this view.
     *
     * @return The name of this view.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this view. for internal use of this API only.
     *
     * @param name The name to be set.
     */
    void setName(String name)
    {
        this.name = name;
    }

    /**
     * Finds the set of resources used in this view by all the controls defined 
     * in it.
     *
     * @return A set with all the names of the resources.
     */
    public Set<String> getResources()
    {
        if (resources == null)
        {
            initResources();
        }
        return resources;
    }

    /**
     * Gets the set of controls classes used in this view.
     *
     * @return All the controls classes used in this view.
     */
    public Set<Class<?>> getControls()
    {
        if (controls == null)
        {
            initControls();
        }
        return controls;
    }

    /**
     * Finds the input expression that match the given string.
     *
     * @param exp The expression.
     * @return The UIInputExpression object that match whit the given String if
     * any.
     */
    public UIInputExpression findInput(String exp)
    {
        if (inputs == null)
        {
            initInputs();
        }
        return inputs.get(exp);
    }

    /**
     * Finds the event that match with the given action.
     *
     * @param action The name of the action.
     * @return The UIEvent object that match the given expression.
     */
    public UIEvent findEvent(String action)
    {
        if (events == null)
        {
            initEvents();
        }
        return events.get(action);
    }

    private boolean checkIsValidView()
    {
        if(getRoot() != null)
        {
            return true;
        }
        LOG.log(Level.WARNING, "The view {0} does not have a valid root control, it will be ignored.", getName());
        return false;
    }

    private synchronized void initEvents()
    {
        if (events == null)
        {
            if (checkIsValidView())
            {
                Map<String, UIEvent> eventsMap = new HashMap<>();
                findEvents(getRoot(), eventsMap);
                events = eventsMap;
            }
            else
            {
                events = Collections.emptyMap();
            }
        }
    }

    private synchronized void initControls()
    {
        if (controls == null)
        {
            if (checkIsValidView())
            {
                Set<Class<?>> controlsSet = new HashSet<>();
                findControls(getRoot(), controlsSet);
                controls = controlsSet;
            }
            else
            {
                controls = Collections.emptySet();
            }
        }
    }

    private synchronized void initResources()
    {
        if (resources == null)
        {
            if (checkIsValidView())
            {
                Set<String> resourcesSet = new HashSet<>();
                findResources(getRoot(), resourcesSet);
                resources = resourcesSet;
            }
            else
            {
                resources = Collections.emptySet();
            }
        }
    }

    private synchronized void initInputs()
    {
        if (inputs == null)
        {
            if (checkIsValidView())
            {
                Map<String, UIInputExpression> inputsMap = new HashMap<>();
                findInputs(getRoot(), inputsMap);
                inputs = inputsMap;
            }
            else
            {
                inputs = Collections.emptyMap();
            }
        }
    }

    private void findEvents(Control control, Map<String, UIEvent> eventsMap)
    {
        control.events().forEach((ev) -> eventsMap.put(ev.getExpression(), ev));
        control.childs().forEach((child) -> findEvents(child, eventsMap));
    }

    private void findInputs(Control control, Map<String, UIInputExpression> inputsMap)
    {
        control.inputs().forEach((in) -> inputsMap.put(in.getParameter(), in));
        control.childs().forEach((child) -> findInputs(child, inputsMap));
    }

    private void findResources(Control control, Set<String> resourcesSet)
    {
        control.resources().forEach((r) -> resourcesSet.add(r));
        control.childs().forEach((child) -> findResources(child, resourcesSet));
    }

    private void findControls(Control control, Set<Class<?>> controlsSet)
    {
        controlsSet.add(control.getClass());
        control.childs().forEach((child) -> findControls(child, controlsSet));
    }
}
