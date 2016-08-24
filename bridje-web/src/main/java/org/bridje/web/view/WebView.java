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

import org.bridje.web.view.widgets.Widget;
import org.bridje.web.view.widgets.UIEvent;
import org.bridje.web.view.widgets.UIInputExpression;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.*;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from widgets. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@XmlRootElement(name = "view")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebView
{
    @XmlTransient
    private String name;

    @XmlAttribute(name = "title")
    private String title;

    @XmlElements(
        @XmlElement(name = "meta", type = MetaTag.class)
    )
    private List<MetaTag> metaTags;

    @XmlAnyElement(lax = true)
    private Widget root;

    @XmlTransient
    private Map<String, UIEvent> events;

    @XmlTransient
    private Map<String, UIInputExpression> inputs;

    /**
     * Gets a list of meta information tags information to be rendered with 
     * this view.
     * 
     * @return A list of meta information tags assigned to this view.
     */
    public List<MetaTag> getMetaTags()
    {
        return metaTags;
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
     * The root widget of this view.
     *
     * @return The root widget.
     */
    public Widget getRoot()
    {
        return root;
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

    private synchronized void initEvents()
    {
        if (events == null)
        {
            Map<String, UIEvent> eventsMap = new HashMap<>();
            findEvents(root, eventsMap);
            events = eventsMap;
        }
    }

    private synchronized void initInputs()
    {
        if (inputs == null)
        {
            Map<String, UIInputExpression> inputsMap = new HashMap<>();
            findInputs(root, inputsMap);
            inputs = inputsMap;
        }
    }

    private void findEvents(Widget widget, Map<String, UIEvent> eventsMap)
    {
        widget.events().forEach((ev) -> eventsMap.put(ev.getExpression(), ev));
        widget.childs().forEach((child) -> findEvents(child, eventsMap));
    }

    private void findInputs(Widget widget, Map<String, UIInputExpression> inputsMap)
    {
        widget.inputs().forEach((in) -> inputsMap.put(in.getParameter(), in));
        widget.childs().forEach((child) -> findInputs(child, inputsMap));
    }
}
