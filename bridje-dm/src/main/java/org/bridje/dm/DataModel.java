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

package org.bridje.dm;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
 */
@XmlRootElement(name = "datamodel")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataModel
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute(name = "tbprefix")
    private String tablesPrefix;
    
    @XmlAttribute(name = "package")
    private String javaPackage;

    @XmlElementWrapper(name = "types")
    @XmlElements(
    {
        @XmlElement(name = "type", type = DataType.class),
        @XmlElement(name = "enum", type = EnumDataType.class)
    })
    private List<DataTypeBase> types;

    @XmlElementWrapper(name = "entitys")
    @XmlElements(
    {
        @XmlElement(name = "abstract", type = AbstractEntity.class),
        @XmlElement(name = "entity", type = Entity.class)
    })
    private List<EntityBase> entitys;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<DataTypeBase> getTypes()
    {
        if(types == null)
        {
            types = new ArrayList<>();
        }
        return types;
    }
    
    public List<EnumDataType> getEnums()
    {
        return getTypes().stream()
                .filter((edt) -> edt instanceof EnumDataType)
                .map((DataTypeBase dtb) -> (EnumDataType)dtb)
                .collect(Collectors.toList());
    }

    public List<EntityBase> getEntitys()
    {
        if(entitys == null)
        {
            entitys = new ArrayList<>();
        }
        return entitys;
    }
    
    public List<Entity> getConcreteEntitys()
    {
        //Nice java lamdas and collection framework....... :)
        return getEntitys().stream()
                .filter((entity) -> (entity instanceof Entity))
                .map((EntityBase entity) -> (Entity)entity)
                .collect(Collectors.toList());
    }

    public String getTablesPrefix()
    {
        return tablesPrefix;
    }

    public void setTablesPrefix(String tablesPrefix)
    {
        this.tablesPrefix = tablesPrefix;
    }

    public String getPackage()
    {
        return javaPackage;
    }

    public void setPackage(String javaPackage)
    {
        this.javaPackage = javaPackage;
    }
    
    public static DataModel read(InputStream is) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(DataModel.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        Object data = unmarshaller.unmarshal(is);
        if(data instanceof DataModel)
        {
            return (DataModel)data;
        }
        return null;
    }
    
    public static void write(DataModel dataModel, OutputStream os) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(DataModel.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.marshal(dataModel, os);
    }
}
