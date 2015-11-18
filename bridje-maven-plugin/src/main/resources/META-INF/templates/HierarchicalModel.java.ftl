<#include "utils.ftl"/>

package ${package};

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 */
@XmlRootElement
public class ${name}
{
    <@entityContent />
            
    public static ${name} loadModel(File source) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(${name}.class);
        Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (${name})unmarsh.unmarshal(source);
    }

    public static void generateSchema(File target) throws JAXBException, IOException
    {
        JAXBContext ctx = JAXBContext.newInstance(${name}.class);
        //ctx.generateSchema(new OutputResolver(target));
    }
}