<#include "utils.ftl" />
/**
 <@javaDocContent model.license!"" />
 */

package ${model.packageName};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.Unmarshaller;

/**
 <@javaDocContent description!"" true />
 */
@XmlAccessorType(XmlAccessType.FIELD)
public <#if customizable >abstract </#if>class ${name}<#if customizable >Base</#if><#if extendsFrom??> extends ${extendsFrom}</#if>
{
    <@entityContent />
    <@parentCode />
}