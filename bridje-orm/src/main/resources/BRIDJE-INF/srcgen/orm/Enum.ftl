
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
    ${ct.name}<#if enum.descriptionAsProperty>("${ct.description!}")</#if><#if ct_has_next>,<#else>;</#if>
    </#list>
    <#if enum.descriptionAsProperty>

    private final String description;

    ${enum.name}(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    </#if>
}