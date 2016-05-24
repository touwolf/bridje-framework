<#function findType field>
    <#if field?node_name == "relation">
        <#return field.@with />
    <#else>
        <#return field?node_name?cap_first />
    </#if>
</#function>

<#function hasType type>
    <#list node.* as field>
        <#if field?node_name == type>
            <#return true />
        </#if>
    </#list>
    <#return false />
</#function>

<#function findTableColumn field>
    <#if field?node_name == "relation">
        <#return "TableRelationColumn" />
    <#elseif isNumber(field) >
        <#return "TableNumberColumn" />
    </#if>
    <#return "TableColumn" />
</#function>

<#function isNumber field>
    <#if field?node_name == "byte" 
            || field?node_name == "short"
            || field?node_name == "integer"
            || field?node_name == "long"
            || field?node_name == "float"
            || field?node_name == "double" >
        <#return true />
    </#if>
    <#return false />
</#function>

<#function isKey field>
    <#if (field.@autoIncrement[0]?? && field.@autoIncrement == "true") 
            || (field.@key[0]?? && field.@key == "true")>
        <#return true />
    </#if>
    <#return false />
</#function>

<#function isAutoIncrement field>
    <#if (field.@autoIncrement[0]?? && field.@autoIncrement == "true") >
        <#return true />
    </#if>
    <#return false />
</#function>