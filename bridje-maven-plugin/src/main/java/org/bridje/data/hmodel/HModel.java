
package org.bridje.data.hmodel;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HModel extends HEntityData
{
    @XmlAttribute
    private String packageName;
    
    @XmlElementWrapper(name = "entitys")
    @XmlElements(
    {
        @XmlElement(name = "entity", type = HEntity.class)
    })
    private java.util.List<HEntity> entitys;
    
    @XmlElementWrapper(name = "enums")
    @XmlElements(
    {
        @XmlElement(name = "enum", type = HEnum.class)
    })
    private java.util.List<HEnum> enums;
    
    public String getPackageName()
    {
        return this.packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public java.util.List<HEntity> getEntitys()
    {
        return this.entitys;
    }

    public void setEntitys(java.util.List<HEntity> entitys)
    {
        this.entitys = entitys;
    }

    public java.util.List<HEnum> getEnums()
    {
        return this.enums;
    }

    public void setEnums(java.util.List<HEnum> enums)
    {
        this.enums = enums;
    }

            
    public static HModel loadModel(File source) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(HModel.class);
        Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (HModel)unmarsh.unmarshal(source);
    }

    public static void generateSchema(final File target) throws JAXBException, IOException
    {
        JAXBContext ctx = JAXBContext.newInstance(HModel.class);
        ctx.generateSchema(new SchemaOutputResolver()
        {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException
            {
                return new StreamResult(target);
            }
        });
    }
}