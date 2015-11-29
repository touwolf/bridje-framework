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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Hierarchical model definition.
 */
@XmlRootElement(name = "hmodel")
@XmlAccessorType(XmlAccessType.FIELD)
public class HModel extends HModelBase
{

    public HEntity findEntity(String type)
    {
        List<HEntity> entitys = getEntitys();
        for (HEntity entity : entitys)
        {
            if(entity.getName().equalsIgnoreCase(type))
            {
                return entity;
            }
        }
        return null;
    }
    
    public HEnum findEnum(String type)
    {
        List<HEnum> entitys = getEnums();
        for (HEnum entity : entitys)
        {
            if(entity.getName().equalsIgnoreCase(type))
            {
                return entity;
            }
        }
        return null;
    }

    @Override
    public HModel getModel()
    {
        return this;
    }
}