
package ${enum.package};
<#list enum.properties as prop>
<#if prop.mapped>

import java.util.Map;
import java.util.HashMap;
<#break />
</#if>
</#list>

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
    <#assign hasProps = false />
    <#if enum.properties?size != 0>
    <#assign hasProps = true />
    </#if>
    <#assign hasDescr = false />
    <#if enum.descriptionAsProperty>
    <#assign hasDescr = true />
    </#if>
    ${ct.name}<#if hasDescr || hasProps>(</#if><#if hasDescr>"${ct.description!}"<#if hasProps>, </#if></#if><#list enum.properties![] as prop><#if prop.type == "String">"</#if>${ct.properties[prop.name]}<#if prop.type == "String">"</#if><#if prop?has_next>, </#if></#list><#if hasDescr || hasProps>)</#if><#if ct?has_next>,<#else>;</#if>
    </#list>
    <#if hasDescr || hasProps>
    <#if hasDescr>

    private final String description;
    </#if>
    <#list enum.properties as prop>

    private final ${prop.type} ${prop.name};<#if prop.mapped>

    private final static Map<${prop.type}, ${enum.name}> ${prop.name}Map = new HashMap<>();
    static
    {
        <#list enum.constants as ct>
        ${prop.name}Map.put(<#if prop.type == "String">"</#if>${ct.properties[prop.name]}<#if prop.type == "String">"</#if>, ${ct.name});
        </#list>
    }</#if>
    </#list>

    ${enum.name}(<#if hasDescr>String description<#if hasProps>, </#if></#if><#list enum.properties as prop>${prop.type} ${prop.name}<#if prop?has_next>, </#if></#list>)
    {<#if hasDescr>
        this.description = description;</#if>
        <#list enum.properties as prop>
        this.${prop.name} = ${prop.name};
        </#list>
    }
    <#if hasDescr>

    public String getDescription()
    {
        return description;
    }
    </#if>
    <#list enum.properties as prop>

    public ${prop.type} get${prop.name?cap_first}()
    {
        return ${prop.name};
    }<#if prop.mapped>

    public static ${enum.name} from${prop.name?cap_first}(${prop.type} ${prop.name})
    {
        return ${prop.name}Map.get(${prop.name});
    }</#if>
    </#list>
    </#if>
}
