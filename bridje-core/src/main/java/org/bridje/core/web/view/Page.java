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

package org.bridje.core.web.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
public class Page extends AbstractView
{
    private static final Logger LOG = Logger.getLogger(Page.class.getName());
    
    @XmlTransient
    private final Map<String, UIExpression> inputExpressions = new LinkedHashMap<>();
    
    @XmlTransient
    private final Map<String, UIEvent> eventExpressions = new LinkedHashMap<>();
    
    public boolean existsInputExpression(String encParam)
    {
        if(inputExpressions == null)
        {
            fillInputExpressionMap();
        }
        return inputExpressions.containsKey(encParam);
    }

    public UIExpression findInputExpression(String encParam)
    {
        if(inputExpressions == null)
        {
            fillInputExpressionMap();
        }
        return inputExpressions.get(encParam);
    }
    
    private synchronized void fillInputExpressionMap()
    {
        if(inputExpressions == null)
        {
            travel((Widget widget) -> {
                if(widget.getInputs() != null)
                {
                    for(UIExpression uiExp : widget.getInputs())
                    {
                        inputExpressions.put(uiExp.getEncodecExpression(), uiExp);
                    }
                }
            });
        }
    }

    public boolean existsEvent(String viewAction)
    {
        if(eventExpressions == null)
        {
            fillEventExpressionMap();
        }
        return eventExpressions.containsKey(viewAction);
    }
    
    public UIEvent findEvent(String encParam)
    {
        if(eventExpressions == null)
        {
            fillEventExpressionMap();
        }
        return eventExpressions.get(encParam);
    }
    
    private synchronized void fillEventExpressionMap()
    {
        if(eventExpressions == null)
        {
            travel((Widget widget) -> {
                if(widget.getEvents() != null)
                {
                    for(UIEvent uiEvent : widget.getEvents())
                    {
                        eventExpressions.put(uiEvent.getEncodecExpression(), uiEvent);
                    }
                }
            });
        }
    }
}
