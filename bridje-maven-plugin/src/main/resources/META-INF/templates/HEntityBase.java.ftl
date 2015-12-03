<#include "utils.ftl" />
/**
 <@javaDocContent model.license!"" />
 */

package ${model.packageName};

/**
 <@javaDocContent description!"" true />
 */
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public <#if customizable >abstract </#if>class ${name}<#if customizable >Base</#if><#if extendsFrom??> extends ${extendsFrom}</#if>
{
    <@entityContent />
    <@parentCode />
}