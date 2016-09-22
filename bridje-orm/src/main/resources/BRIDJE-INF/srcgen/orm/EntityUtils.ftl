<#assign custTypes = {} />
<#list resources as r>
    <#list r.datatypes.* as e>
        <#assign custTypes = custTypes + {e?node_name: e} />
    </#list>
</#list>

<#assign templatesMap = {} />
<#list doc.model.template as t>
    <#assign templatesMap = templatesMap + {t.@name: t} />
</#list>
<#assign nodeFields = {} />
<#if node.@extends[0]?? && node.@extends != "" >
    <#assign tmpl = templatesMap[node.@extends] />
    <#list tmpl.* as f>
        <#assign nodeFields = nodeFields + {f.@name:f} />
    </#list>
</#if>
<#list node.* as f>
    <#assign nodeFields = nodeFields + {f.@name:f} />
</#list>
<#assign nodeAttrs = {} />
<#list node.@@ as a>
    <#assign nodeAttrs = nodeAttrs + {a?node_name:a[0]} />
</#list>
<#assign newNode = {} />
<#assign newNode = newNode + {"fields":nodeFields} />
<#assign newNode = newNode + {"attrs":nodeAttrs} />
<#assign node = newNode />

<#function findType field>
    <#if field?node_name == "relation">
        <#return field.@with />
    <#elseif field?node_name == "enum">
        <#return field.@type />
    <#elseif custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#return ct.@class />
    <#else>
        <#return field?node_name?cap_first />
    </#if>
</#function>

<#function hasAtLeastOneType type>
    <#list node.fields?keys as fieldName>
        <#assign field = node.fields[fieldName] />
        <#if field?node_name == type>
            <#return true />
        </#if>
    </#list>
    <#return false />
</#function>

<#function hasAtLeastOneSqlType>
    <#list node.fields?keys as fieldName>
        <#assign field = node.fields[fieldName] />
        <#if hasSqlType(field)>
            <#return true />
        </#if>
    </#list>
    <#return false />
</#function>

<#function findKeyField>
    <#list node.fields?keys as fieldName>
        <#assign field = node.fields[fieldName] />
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
    <#elseif custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#if ct.@columnType[0]??>
            <#return ct.@columnType />
        </#if>
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
    <#if entity.attrs.table??>
        <#return findTablePrefix(doc.model) + entity.attrs.table />
    </#if>
    <#return findTablePrefix(doc.model) + toSqlName(entity.attrs.name?uncap_first) />
</#function>

<#function findColumnName field>
    <#if field.@column[0]??>
        <#return field.@column />
    </#if>
    <#return toSqlName(field.@name?uncap_first) />
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
    <#if custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#if (ct.@length[0]??) >
            <#return true />
        </#if>
    </#if>
    <#return false />
</#function>

<#function getLength field>
    <#if (field.@length[0]??) >
        <#return field.@length />
    </#if>
    <#if custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#if (ct.@length[0]??) >
            <#return ct.@length />
        </#if>
    </#if>
    <#return 0 />
</#function>

<#function hasSqlType field>
    <#if (field.@sqlType[0]??) >
        <#return true />
    </#if>
    <#if custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#if (ct.@sqlType[0]??) >
            <#return true />
        </#if>
    </#if>
    <#return false />    
</#function>

<#function getSqlType field>
    <#if (field.@sqlType[0]??) >
        <#return field.@sqlType />
    </#if>
    <#if custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#if (ct.@sqlType[0]??) >
            <#return ct.@sqlType />
        </#if>
    </#if>
    <#return "" />
</#function>

<#function hasAdapter field>
    <#if (field.@adapter[0]??) >
        <#return true />
    </#if>
    <#if custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#if (ct.@adapter[0]??) >
            <#return true />
        </#if>
    </#if>
    <#return false />    
</#function>

<#function getAdapter field>
    <#if (field.@adapter[0]??) >
        <#return field.@adapter />
    </#if>
    <#if custTypes[field?node_name]?? >
        <#assign ct = custTypes[field?node_name] />
        <#if (ct.@adapter[0]??) >
            <#return ct.@adapter />
        </#if>
    </#if>
    <#return "" />
</#function>

<#function toSqlName name>
    <#assign result = name />
    <#assign result = result?replace("A", "_a") />
    <#assign result = result?replace("B", "_b") />
    <#assign result = result?replace("C", "_c") />
    <#assign result = result?replace("D", "_d") />
    <#assign result = result?replace("E", "_e") />
    <#assign result = result?replace("F", "_f") />
    <#assign result = result?replace("G", "_g") />
    <#assign result = result?replace("H", "_h") />
    <#assign result = result?replace("I", "_i") />
    <#assign result = result?replace("J", "_j") />
    <#assign result = result?replace("K", "_k") />
    <#assign result = result?replace("L", "_l") />
    <#assign result = result?replace("M", "_m") />
    <#assign result = result?replace("N", "_n") />
    <#assign result = result?replace("O", "_o") />
    <#assign result = result?replace("P", "_p") />
    <#assign result = result?replace("Q", "_q") />
    <#assign result = result?replace("R", "_r") />
    <#assign result = result?replace("S", "_s") />
    <#assign result = result?replace("T", "_t") />
    <#assign result = result?replace("U", "_u") />
    <#assign result = result?replace("V", "_v") />
    <#assign result = result?replace("W", "_w") />
    <#assign result = result?replace("X", "_x") />
    <#assign result = result?replace("Y", "_y") />
    <#assign result = result?replace("Z", "_z") />
    <#return result />
</#function>

<#function findFieldDescription field>
    <#if field.@description[0]?? >
        <#return field.@description />
    </#if>
    <#if field?parent?parent.@defaultFieldDescription[0]?? >
        <#return field?parent?parent.@defaultFieldDescription />
    </#if>
    <#return "" />
</#function>

<#function findEntityDescription entity>
    <#if entity.attrs.description?? >
        <#return entity.attrs.description />
    </#if>
    <#if doc.model.@defaultEntityDescription[0]?? >
        <#return doc.model.@defaultEntityDescription />
    </#if>
    <#return "" />
</#function>

<#function findTablePrefix model>
    <#if model.@tablePrefix[0]?? >
        <#if model.@tablePrefix != "" >
            <#return model.@tablePrefix + "_" />
        </#if>
    </#if>
    <#return "" />
</#function>
