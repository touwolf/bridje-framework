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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * For each statement for the read input flow.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadElseData implements ControlFlowAction
{
    @XmlElements(
    {
        @XmlElement(name = "if", type = ReadIfData.class),
        @XmlElement(name = "else", type = ReadElseData.class),
        @XmlElement(name = "for", type = ReadForEachData.class),
        @XmlElement(name = "pushVar", type = PushEnvVar.class),
        @XmlElement(name = "popVar", type = PopEnvVar.class),
        @XmlElement(name = "pop", type = PopFieldInput.class),
        @XmlElement(name = "popAll", type = PopAllFieldInputs.class),
        @XmlElement(name = "get", type = ReadFieldInput.class),
        @XmlElement(name = "getAll", type = ReadAllFieldInputs.class),
        @XmlElement(name = "executeAll", type = ExecuteAllEvents.class),
        @XmlElement(name = "childrenAll", type = ReadAllChildren.class)
    })
    private List<ControlFlowAction> actions;
    
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
