<#include "utils.ftl" />

package ${model.package};

import javax.xml.bind.annotation.XmlEnum;

/**
 * 
 */
@XmlEnum
public enum ${name}
{
    <#list values as v>
    /**
     *
     */
    ${v.name}<#sep>,</#sep>

    </#list>
    ;
}