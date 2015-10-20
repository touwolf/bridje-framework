
<#function findRelationWhereFields relation entity dm dm>
    <#assign fieldWheresList = [] />
    <#list relation.fields as rfield> 
        <#assign fieldWheresList = fieldWheresList + [rfield.relatedField] />
    </#list>
    <#return fieldWheresList /> 
</#function>

<#function findRelationParamFields relation entity dm>
    <#assign fieldParamsList = [] />
    <#list relation.fields as rfield> 
        <#assign fieldParamsList = fieldParamsList + [rfield.field] />
    </#list>
    <#return fieldParamsList /> 
</#function>

