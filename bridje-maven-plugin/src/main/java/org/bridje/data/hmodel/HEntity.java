/**
 * 
 * Copyright 2015 Bridje Framework.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *     
 */

package org.bridje.data.hmodel;

/**
 * Represents an entity of the hierarchical model.
 */
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class HEntity extends HEntityData
{
    private HParentField parent;
    
    /**
     * The parent field of this entity.
     * @return A HParentField object representing the value of the parent field.
     */
    public HParentField getParent()
    {
        return this.parent;
    }

    /**
     * The parent field of this entity.
     * @param parent The new value for the parent field.
     */
    public void setParent(HParentField parent)
    {
        this.parent = parent;
    }

    @javax.xml.bind.annotation.XmlTransient
    private HModel model;

    /**
     * The HModel parent of this object.
     * @return The HModel object representing the parent of this object.
     */
    public HModel getModel()
    {
        return this.model;
    }

    /**
     * The HModel object representing the parent of this object.
     */
    void setModel(HModel model)
    {
        this.model = model;
    }

    /**
     * This method is call by the JAXB Unmarshaller after the object's creation.
     * @param unmarshaller The unmarshaller object being used.
     * @param parent The parent object for this object.
     */
    public void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, Object parent)
    {
        if(parent instanceof HModel)
        {
            setModel((HModel)parent);
        }
    }

}