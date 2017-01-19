
package ${enum.package};
<#if enum.properties?size != 0>

import java.util.Map;
import java.util.HashMap;
</#if>

/**
 * This class represents the ${enum.name} enumerator.
 * ${enum.description!}
 */
public enum ${enum.name}
{
    <#list enum.constants as ct>
    /**
     * The ${ct.name} constant for the ${enum.name} enumerator.<#if ct.description?? && ct.description?has_content>
     * ${ct.description}</#if>
     */
    <#assign hasProps = false />
    <#if enum.properties?size != 0>
    <#assign hasProps = true />
    </#if>
    <#assign hasDescr = false />
    <#if enum.descriptionAsProperty>
    <#assign hasDescr = true />
    </#if>
    ${ct.name}<#if hasDescr || hasProps>(</#if><#if hasDescr>"${ct.description!}"<#if hasProps>, </#if></#if><#list enum.properties![] as prop><#if prop.javaType == "String">"</#if>${ct.propertyValue(prop.name)}<#if prop.javaType == "String">"<#elseif prop.javaType == "Long">L<#elseif prop.javaType == "Double">D</#if><#if prop?has_next>, </#if></#list><#if hasDescr || hasProps>)</#if><#if ct?has_next>,<#else>;</#if>
    </#list>
    <#if hasDescr || hasProps>
    <#if hasDescr>

    private final String description;
    </#if>
    <#list enum.properties as prop>

    private final ${prop.javaType} ${prop.name};

    private final static Map<${prop.javaType}, ${enum.name}> ${prop.name}Map = new HashMap<>();
    static
    {
        <#list enum.constants as ct>
        ${prop.name}Map.put(<#if prop.javaType == "String">"</#if>${ct.propertyValue(prop.name)}<#if prop.javaType == "String">"<#elseif prop.javaType == "Long">L<#elseif prop.javaType == "Double">D</#if>, ${ct.name});
        </#list>
    }

    </#list>
    private ${enum.name}(<#if hasDescr>String description<#if hasProps>, </#if></#if><#list enum.properties as prop>${prop.javaType} ${prop.name}<#if prop?has_next>, </#if></#list>)
    {
        <#if hasDescr>this.description = description;</#if>
        <#list enum.properties as prop>
        this.${prop.name} = ${prop.name};
        </#list>
    }

    <#if hasDescr>
    /**
     * Gets the description for this constant.
     * @return An string representing the description for this constant.
     */
    public String getDescription()
    {
        return description;
    }

    </#if>
    <#list enum.properties as prop>
    /**
     * Gets the value of the ${prop.name} property for this constant.
     * @return The value of the ${prop.name} property.
     */
    public ${prop.javaType} get${prop.name?cap_first}()
    {
        return ${prop.name};
    }

    /**
     * Gets the constant assigned to the given value of the ${prop.name} property.
     * @param ${prop.name} The value of the ${prop.name} property.
     * @return The enum constant for the ${prop.name} property.
     */
    public static ${enum.name} from${prop.name?cap_first}(${prop.javaType} ${prop.name})
    {
        return ${prop.name}Map.get(${prop.name});
    }

    </#list>
    </#if>
}
