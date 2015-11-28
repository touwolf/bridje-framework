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

package org.bridje.maven.plugin.model.hierarchical;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Base class for all navite fields like HStringField, HBooleanField, etc...
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class HBasicField extends HField
{
    @XmlAttribute(name = "access")
    private HFieldAccess access;

    @Override
    public boolean getIsList()
    {
        return false;
    }
    
    public HFieldAccess getAccess()
    {
        return access;
    }

    public void setAccess(HFieldAccess access)
    {
        this.access = access;
    }

    public abstract String getXmlType();
}
