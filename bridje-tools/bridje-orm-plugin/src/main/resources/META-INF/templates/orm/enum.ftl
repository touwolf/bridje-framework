<#include "orm-common.ftl">

package ${dm.package};

/**
 * ${enum.description!(enum.name)}
 */
public enum ${enum.name}
{
    <#list enum.values as value>
    ${value.name}<#if value_has_next>,<#else>;</#if>
    </#list>
}