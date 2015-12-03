<#include "utils.ftl"/>
/**
 <@javaDocContent license!"" />
 */

package ${packageName};

/**
 <@javaDocContent description!"" true />
 */
<#if !customizable>
@javax.xml.bind.annotation.XmlRootElement(name = "${name?lower_case}")
</#if>
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public <#if customizable >abstract </#if>class ${name}<#if customizable >Base</#if><#if extendsFrom??> extends ${extendsFrom}</#if>
{
    <@entityContent />
            
    public static ${name} loadModel(java.io.File source) throws javax.xml.bind.JAXBException
    {
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(${name}.class);
        javax.xml.bind.Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (${name})unmarsh.unmarshal(source);
    }

    public static void generateSchema(final java.io.File target) throws javax.xml.bind.JAXBException, java.io.IOException
    {
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(${name}.class);
        ctx.generateSchema(new javax.xml.bind.SchemaOutputResolver()
        {
            @Override
            public javax.xml.transform.Result createOutput(String namespaceUri, String suggestedFileName) throws java.io.IOException
            {
                return new javax.xml.transform.stream.StreamResult(target);
            }
        });
    }
}