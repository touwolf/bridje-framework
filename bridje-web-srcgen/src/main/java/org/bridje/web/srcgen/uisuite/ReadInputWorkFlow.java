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
 * 
 * @author gilbe
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadInputWorkFlow
{
    @XmlElements(
    {
        @XmlElement(name = "for", type = ForEachData.class),
        @XmlElement(name = "setVar", type = SetEnvVar.class),
        @XmlElement(name = "pop", type = PopFieldInput.class),
        @XmlElement(name = "popAll", type = PopAllFieldInputs.class),
        @XmlElement(name = "get", type = ReadFieldInput.class),
        @XmlElement(name = "getAll", type = ReadAllFieldInputs.class),
        @XmlElement(name = "children", type = ReadChildren.class),
        @XmlElement(name = "childrenAll", type = ReadAllChildren.class)
    })
    private List<ReadInputAction> actions;

    /**
     * The list of actions for this flow.
     * 
     * @return The list of actions for this flow.
     */
    public List<ReadInputAction> getActions()
    {
        return actions;
    }
}
