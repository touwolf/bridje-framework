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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gilberto
 */
@XmlRootElement(name = "hierarchy")
@XmlAccessorType(XmlAccessType.FIELD)
public class HierarchicalModel
{
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "package")
    private String packageName;

    public static HierarchicalModel loadModel(File source) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(HierarchicalModel.class);
        Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (HierarchicalModel)unmarsh.unmarshal(source);
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
}
