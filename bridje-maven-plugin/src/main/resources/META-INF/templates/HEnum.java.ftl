<#include "utils.ftl" />
/**
 <@javaDocContent model.license!"" />
 */
package ${model.packageName};

import javax.xml.bind.annotation.XmlEnum;

/**
 <@javaDocContent description!"" true />
 */
@XmlEnum
public enum ${name}
{
    <#list values as v>
    /**
     * ${description!""}
     */
    ${v.name}<#sep>,</#sep>

    </#list>
    ;
}