
package ${enum.package};

/**
 * This class represents the ${enum.name} enumerator.
 * ${enum.description!}
 */
public enum ${enum.name}
{
    <#list enum.constants as ct>
    /**
     * ${ct.description!}
     */
    ${ct.name}<#if ct_has_next>,<#else>;</#if>
    </#list>
}