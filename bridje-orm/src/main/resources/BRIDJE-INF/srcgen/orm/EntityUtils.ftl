<#function findType field>
    <#if field?node_name == "relation">
        <#return field.@with />
    <#elseif field?node_name == "enum">
        <#return field.@type />
    <#else>
        <#return field?node_name?cap_first />
    </#if>
</#function>

<#function hasAtLeastOneType type>
    <#list node.* as field>
        <#if field?node_name == type>
            <#return true />
        </#if>
    </#list>
    <#return false />
</#function>

<#function hasAtLeastOneSqlType>
    <#list node.* as field>
        <#if hasSqlType(field)>
            <#return true />
        </#if>
    </#list>
    <#return false />
</#function>

<#function findKeyField>
    <#list node.* as field>
        <#if isKey(field)>
            <#return field />
        </#if>
    </#list>
</#function>


<#function findTableColumn field>
    <#if field?node_name == "relation">
        <#return "TableRelationColumn" />
    <#elseif isNumber(field) >
        <#return "TableNumberColumn" />
    <#elseif isString(field) >
        <#return "TableStringColumn" />
    <#elseif isDate(field) >
        <#return "TableDateColumn" />
    </#if>
    <#return "TableColumn" />
</#function>

<#function isDate field>
    <#if field?node_name == "date" >
        <#return true />
    </#if>
    <#return false />
</#function>

<#function isTime field>
    <#if field?node_name == "time" >
        <#return true />
    </#if>
    <#return false />
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

<#function isString field>
    <#if field?node_name == "string" >
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

<#function findTableName entity>
    <#if entity.@table[0]??>
        <#return entity.@table />
    </#if>
    <#return entity.@name?uncap_first />
</#function>

<#function findColumnName field>
    <#if field.@column[0]??>
        <#return field.@column />
    </#if>
    <#return field.@name?uncap_first />
</#function>

<#function isIndex field>
    <#if (field.@index[0]?? && field.@index == "true") >
        <#return true />
    </#if>
    <#if (field.@indexed[0]?? && field.@indexed == "true") >
        <#return true />
    </#if>
    <#return false />
</#function>

<#function hasLength field>
    <#if (field.@length[0]??) >
        <#return true />
    </#if>
    <#return false />
</#function>

<#function hasSqlType field>
    <#if (field.@sqlType[0]??) >
        <#return true />
    </#if>
    <#return false />    
</#function>

<#function getSqlType field>
    <#if (field.@sqlType[0]??) >
        <#return field.@sqlType />
    </#if>
    <#return field.@sqlType />
</#function>

<#function hasAdapter field>
    <#if (field.@adapter[0]??) >
        <#return true />
    </#if>
    <#return false />    
</#function>

<#function getAdapter field>
    <#if (field.@adapter[0]??) >
        <#return field.@adapter />
    </#if>
    <#return field.@adapter />
</#function>
