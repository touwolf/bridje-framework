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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.*;
import org.bridje.web.view.comp.UIEvent;
import org.bridje.web.view.comp.UIInputExpression;
import org.bridje.web.view.comp.WebComponent;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from components. The views are inmutables so once defined they will
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
    private WebComponent root;

    @XmlTransient
    private Map<String, UIEvent> events;

    @XmlTransient
    private Map<String, UIInputExpression> inputs;

    /**
     *
     * @return
     */
    public List<MetaTag> getMetaTags()
    {
        return metaTags;
    }

    /**
     *
     * @return
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

    void setName(String name)
    {
        this.name = name;
    }

    /**
     * The root component of this view.
     *
     * @return The root component.
     */
    public WebComponent getRoot()
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

    private void findEvents(WebComponent comp, Map<String, UIEvent> eventsMap)
    {
        comp.events().forEach((ev) -> eventsMap.put(ev.getExpression(), ev));
        comp.childs().forEach((child) -> findEvents(child, eventsMap));
    }

    private void findInputs(WebComponent comp, Map<String, UIInputExpression> inputsMap)
    {
        comp.inputs().forEach((in) -> inputsMap.put(in.getParameter(), in));
        comp.childs().forEach((child) -> findInputs(child, inputsMap));
    }
}
