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
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "view")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebView
{
    @XmlTransient
    private String name;

    @XmlAnyElement(lax = true)
    private WebComponent root;

    @XmlTransient
    private Map<String, UIEvent> events;

    @XmlTransient
    private Map<String, UIInputExpression> inputs;

    public String getName()
    {
        return name;
    }

    void setName(String name)
    {
        this.name = name;
    }

    public WebComponent getRoot()
    {
        return root;
    }

    public UIInputExpression findInput(String exp)
    {
        if(inputs == null)
        {
            initInputs();
        }
        return inputs.get(exp);
    }

    public UIEvent findEvent(String action)
    {
        if(events == null)
        {
            initEvents();
        }
        return events.get(action);
    }

    private synchronized void initEvents()
    {
        if(events == null)
        {
            Map<String, UIEvent> eventsMap = new HashMap<>();
            findEvents(root, eventsMap);
            events = eventsMap;
        }
    }

    private synchronized void initInputs()
    {
        if(inputs == null)
        {
            Map<String, UIInputExpression> inputsMap = new HashMap<>();
            findInputs(root, inputsMap);
            inputs = inputsMap;
        }
    }

    private void findEvents(WebComponent comp, Map<String, UIEvent> eventsMap)
    {
        comp.events().stream().forEach((ev) -> eventsMap.put(ev.getExpression(), ev) );
        comp.childs().stream().forEach((child) -> findEvents(child, eventsMap) );
    }

    private void findInputs(WebComponent comp, Map<String, UIInputExpression> inputsMap)
    {
        comp.inputs().stream().forEach((in) -> inputsMap.put(in.getParameter(), in) );
        comp.childs().stream().forEach((child) -> findInputs(child, inputsMap) );
    }
}
