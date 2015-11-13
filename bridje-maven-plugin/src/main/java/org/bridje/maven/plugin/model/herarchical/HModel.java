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

package org.bridje.maven.plugin.model.herarchical;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gilberto
 */
@XmlRootElement(name = "hierarchy")
@XmlAccessorType(XmlAccessType.FIELD)
public class HModel
{
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "package")
    private String packageName;

    @XmlElementWrapper(name = "entitys")
    @XmlElements(
    {
        @XmlElement(name = "entity", type = HEntity.class)
    })
    private List<HEntity> entitys;
            
    public static HModel loadModel(File source) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(HModel.class);
        Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (HModel)unmarsh.unmarshal(source);
    }

    public static void generateSchema(File target) throws JAXBException, IOException
    {
        JAXBContext ctx = JAXBContext.newInstance(HModel.class);
        ctx.generateSchema(new OutputResolver(target));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPackage()
    {
        return packageName;
    }

    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    public List<HEntity> getEntitys()
    {
        return entitys;
    }

    public void setEntitys(List<HEntity> entitys)
    {
        this.entitys = entitys;
    }
}
