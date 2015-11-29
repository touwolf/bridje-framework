<#include "utils.ftl"/>
/**
 <@javaDocContent license!"" />
 */

package ${packageName};

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
 <@javaDocContent description!"" true />
 */
<#if !customizable>
@XmlRootElement(name = "${name?lower_case}")
</#if>
@XmlAccessorType(XmlAccessType.FIELD)
public <#if customizable >abstract </#if>class ${name}<#if customizable >Base</#if><#if extendsFrom??> extends ${extendsFrom}</#if>
{
    <@entityContent />
            
    public static ${name} loadModel(File source) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(${name}.class);
        Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (${name})unmarsh.unmarshal(source);
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