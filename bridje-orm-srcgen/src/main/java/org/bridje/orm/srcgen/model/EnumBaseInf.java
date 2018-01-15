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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

/**
 * This class is the base class for the enumerators information classes.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class EnumBaseInf
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlTransient
    private ModelInf model;

    /**
     * The model that this enumerator belongs to.
     *
     * @return The model that this enumerator belongs to.
     */
    public ModelInf getModel()
    {
        return model;
    }

    /**
     * The name of the enumerator.
     *
     * @return The name of the enumerator.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the enumerator.
     *
     * @param name The name of the enumerator.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Called by JAXB after unmarshall.
     *
     * @param u      The unmarshaller.
     * @param parent The parent object.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        model = (ModelInf) parent;
    }

    /**
     * The package for this entity.
     * 
     * @return The package for this entity.
     */
    public abstract String getPackage();

    /**
     * The full java class name for this enumerator.
     *
     * @return The full java class name for this enumerator.
     */
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
