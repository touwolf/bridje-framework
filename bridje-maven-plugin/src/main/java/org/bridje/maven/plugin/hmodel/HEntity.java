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

package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class represents an element into the hierarchy.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEntity extends HEntityBase
{
    @XmlTransient
    private HModel model;

    public HModel getModel()
    {
        return model;
    }

    public void setModel(HModel model)
    {
        this.model = model;
    }

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        setModel((HModel)parent);
    }
}
