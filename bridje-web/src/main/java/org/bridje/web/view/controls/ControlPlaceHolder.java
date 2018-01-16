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

package org.bridje.web.view.controls;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import org.bridje.web.view.Defines;

/**
 * A control that represents a placeholder for a layout. This controls can be
 * overwritten by the corresponding Defines objects.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ControlPlaceHolder extends Control
{
    @XmlAttribute
    private String name;

    /**
     * The name of the placeholder.
     * 
     * @return The name of the placeholder.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public void doOverride(Map<String, Defines> definesMap)
    {
    }
}
