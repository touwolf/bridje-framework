/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.web.srcgen.uisuite;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * For each statement for the read input flow.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecuteForEachData implements ControlFlowAction
{
    @XmlAttribute
    private String var;

    @XmlAttribute
    private String in;

    @XmlElements(
    {
        @XmlElement(name = "for", type = ForEachData.class),
        @XmlElement(name = "pushVar", type = PushEnvVar.class),
        @XmlElement(name = "popVar", type = PopEnvVar.class),
        @XmlElement(name = "execute", type = ExecuteAllEvents.class),
        @XmlElement(name = "childrenAll", type = ReadAllChildren.class)
    })
    private List<ControlFlowAction> actions;

    /**
     * The var name.
     * 
     * @return The var name.
     */
    public String getVar()
    {
        return var;
    }

    /**
     * The var name.
     * 
     * @param var The var name.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /**
     * The collection field to iterate.
     * 
     * @return The collection field to iterate.
     */
    public String getIn()
    {
        return in;
    }

    /**
     * The collection field to iterate.
     * 
     * @param in The collection field to iterate.
     */
    public void setIn(String in)
    {
        this.in = in;
    }
    
    /**
     * The list of actions.
     * 
     * @return The list of actions.
     */
    public List<ControlFlowAction> getActions()
    {
        return actions;
    }
}
