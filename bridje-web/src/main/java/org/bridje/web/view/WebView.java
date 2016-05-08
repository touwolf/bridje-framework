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
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "view")
public class WebView
{
    @XmlAnyElement(lax = true)
    private WebComponent root;

    private Map<String, UIEvent> events;

    private Map<String, UIInputExpression> inputs;

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
            //TODO find events
            events = new HashMap<>();
        }
    }

    private synchronized void initInputs()
    {
        if(inputs == null)
        {
            //TODO find inputs
            inputs = new HashMap<>();
        }
    }
}
